/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine.katalk

import android.app.Person
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import androidx.annotation.NonUiContext
import androidx.core.app.NotificationCompat
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import land.sungbin.replybot.engine.EngineFactory
import land.sungbin.replybot.engine.Message
import land.sungbin.replybot.engine.Profile
import land.sungbin.replybot.engine.Replier
import land.sungbin.replybot.engine.Room
import land.sungbin.replybot.engine.utils.EmptySource
import okio.Buffer
import okio.Source
import timber.log.Timber

// Copied from...
//  - https://github.com/mooner1022/StarLight/blob/d327b2d10f6cfd617b80d360fbdc81b578297bcf/app/src/main/java/dev/mooner/starlight/listener/specs/AndroidRParserSpec.kt
//  - https://github.com/mooner1022/StarLight/blob/a107d4fe11ed4fcc449ec8af781a80abd133b1fa/app/src/main/java/dev/mooner/starlight/listener/NotificationListener.kt
//
// Edited by Ji Sungbin.

public class KakaoTalkEngine(@NonUiContext private val context: Context) : EngineFactory() {
  private val latestRepliedMessageLogId = ConcurrentHashMap<String, AtomicLong>()

  override val identifier: String = "com.kakao.talk"
  override fun isDeletedMessageSupported(): Boolean = true

  override fun createNormalMessage(sbn: StatusBarNotification): Message.Normal? {
    if (sbn.packageName != identifier) return null
    if (sbn.notification.actions?.size != 2) return null
    if (sbn.notification.actions.all { action -> action.remoteInputs.isNullOrEmpty() }) {
      Timber.tag(TAG).w("[createNormalMessage] ''$identifier' remoteInputs are null or empty.")
      return null
    }

    val extras = sbn.notification.extras?.takeUnless { it.isEmpty } ?: run {
      Timber.tag(TAG).w("[createNormalMessage] ''$identifier' extras are null or empty.")
      return null
    }

    val message = extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString()
    val messages = extras.getParcelableArrayCompat<Bundle>(NotificationCompat.EXTRA_MESSAGES)

    val senderName = extras.getString(NotificationCompat.EXTRA_TITLE).toString()
    val senderObject = messages?.first()?.getParcelableCompat<Person>("sender_person")
    val senderId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) senderObject?.key else null

    val roomName = extras.getString(NotificationCompat.EXTRA_SUB_TEXT)
      ?: extras.getString(NotificationCompat.EXTRA_SUMMARY_TEXT)
      ?: senderName
    val roomId = sbn.tag

    val isGroupChat = roomName != senderName
    val hasMention = extras.getCharSequence(NotificationCompat.EXTRA_TEXT) is SpannableString

    val messageLogId = extras.getLong("chatLogId")

    val profileBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) senderObject?.icon?.getBitmap() else null
    val imageBitmap = extras.getBundle("android.wearable.EXTENSIONS")?.getParcelableCompat<Bitmap>("background")

    val replier = sbn.getReplier(roomId = roomId, messageId = messageLogId)

    val room = Room(
      id = roomId,
      name = roomName,
      isGroupChat = isGroupChat,
      replier = replier,
    )
    val sender = Profile(
      name = senderName,
      id = senderId,
      picture = profileBitmap?.toSource() ?: EmptySource,
    )

    return Message(
      content = message,
      image = imageBitmap?.toSource() ?: EmptySource,
      room = room,
      sender = sender,
      hasMention = hasMention,
      logId = messageLogId,
      identifier = identifier,
    )
      .also {
        Timber.tag(TAG).i("[createNormalMessage] 'Created a message: %s", it)
      }
  }

  override fun createDeletedMessage(sbn: StatusBarNotification, reason: Int): Message.Deleted? {
    if (!isDeletedMessageSupported()) return null
    if (sbn.packageName != identifier) return null
    if (sbn.notification.actions?.size != 2) return null
    if (reason != NotificationListenerService.REASON_APP_CANCEL) return null
    return sbn.toDeletedMessage()
      .takeIf { message ->
        val room = message.room
        if (room != null) latestRepliedMessageLogId[room.id]?.get() != message.logId
        else true
      }
      ?.also { Timber.tag(TAG).i("[createDeletedMessage] Created a deleted message: %s", it) }
  }

  private fun Bitmap.toSource(): Source {
    val buffer = Buffer()
    compress(CompressFormatCompat.WebpOrPng(), /* quality = */ 100, buffer.outputStream())
    return buffer
  }

  private fun StatusBarNotification.toDeletedMessage(): Message.Deleted {
    val extras = notification.extras!!

    val message = extras.getString(NotificationCompat.EXTRA_TEXT).toString()
    val sender = extras.getString(NotificationCompat.EXTRA_TITLE).toString()

    val roomId = tag
    val room = Room.findRoomById(roomId)

    val messageLogId = extras.getLong("chatLogId")

    return Message.Deleted(
      content = message,
      room = room,
      sender = sender,
      logId = messageLogId,
      identifier = identifier,
    )
  }

  private fun StatusBarNotification.getReplier(roomId: String, messageId: Long): Replier {
    val (readAction, sendAction) = notification.actions

    fun send(message: String): Result<Unit> = runCatching {
      val results = sendAction.remoteInputs.fold(Bundle()) { acc, input ->
        acc.apply { putCharSequence(input.resultKey, message) }
      }
      val sendingIntent = Intent().apply {
        RemoteInput.addResultsToIntent(sendAction.remoteInputs, this, results)
      }
      sendAction.actionIntent.send(context, /* code = */ 0, sendingIntent)

      latestRepliedMessageLogId.putIfAbsent(roomId, AtomicLong())
      latestRepliedMessageLogId[roomId]!!.set(messageId)
    }

    fun read(): Result<Unit> = runCatching {
      readAction.actionIntent.send(context, /* code = */ 1, /* intent = */ null)
    }

    return object : Replier {
      override fun markAsRead(): Boolean = read().isSuccess
      override fun markAsRead(room: String): Boolean = Room.findRoomByName(room)?.replier?.markAsRead() == true
      override fun reply(message: String): Boolean = send(message).isSuccess
      override fun reply(room: String, message: String): Boolean = Room.findRoomByName(room)?.replier?.reply(message) == true
    }
  }

  private companion object {
    val TAG = KakaoTalkEngine::class.simpleName!!
  }
}
