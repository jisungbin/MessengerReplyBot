// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine.katalk

import android.app.Notification
import android.app.Person
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import androidx.annotation.NonUiContext
import androidx.core.app.NotificationCompat
import land.sungbin.replybot.engine.EngineFactory
import land.sungbin.replybot.engine.Message
import land.sungbin.replybot.engine.Profile
import land.sungbin.replybot.engine.Replier
import land.sungbin.replybot.engine.Room
import land.sungbin.replybot.engine.v8.V8Source
import okio.Buffer
import okio.Source
import timber.log.Timber

// Copied from...
//  - https://github.com/mooner1022/StarLight/blob/a107d4fe11ed4fcc449ec8af781a80abd133b1fa/app/src/main/java/dev/mooner/starlight/listener/NotificationListener.kt
//  - https://github.com/mooner1022/StarLight/blob/d327b2d10f6cfd617b80d360fbdc81b578297bcf/app/src/main/java/dev/mooner/starlight/listener/specs/AndroidRParserSpec.kt
//
// GPL-3.0 License.
// Original code designed and developed by 2021 mooner1022 (Minki Moon).

// Modified by Ji Sungbin.

public class KakaoTalkEngine(@NonUiContext private val context: Context) : EngineFactory() {
  override val identifier: String get() = "com.kakao.talk"

  override fun createMessage(sbn: StatusBarNotification): Message? {
    if (sbn.packageName != identifier) {
      Timber.tag(TAG).w(
        "[createNormalMessage] '$identifier' is not matched. (sbn.packageName = %s)",
        sbn.packageName,
      )
      return null
    }

    if (sbn.notification.actions?.size != 2) {
      Timber.tag(TAG).w(
        "[createNormalMessage] '$identifier' sbn.notification.actions?.size (= %s) is not 2.",
        sbn.notification.actions?.size,
      )
      return null
    }

    if (sbn.notification.actions.all { action -> action.remoteInputs.isNullOrEmpty() }) {
      Timber.tag(TAG).w("[createNormalMessage] '$identifier' all remoteInputs are null or empty.")
      return null
    }

    val extras = sbn.notification.extras?.takeUnless(Bundle::isEmpty) ?: run {
      Timber.tag(TAG).w("[createNormalMessage] ''$identifier' extras are null or empty.")
      return null
    }

    val message = extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString()
    val messages = extras.getParcelableArrayCompat<Bundle>(NotificationCompat.EXTRA_MESSAGES)
    val messageLogId = extras.getLong("chatLogId")

    val hasMention = extras.getCharSequence(NotificationCompat.EXTRA_TEXT) is SpannableString

    val senderName = extras.getString(NotificationCompat.EXTRA_TITLE).toString()
    val senderObject = messages?.first()?.getParcelableCompat<Person>("sender_person")
    val senderId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) senderObject?.key else null

    val roomId = sbn.tag
    val roomName = extras.getString(NotificationCompat.EXTRA_SUB_TEXT)
      ?: extras.getString(NotificationCompat.EXTRA_SUMMARY_TEXT)
      ?: senderName
    val isGroupChat = roomName != senderName

    val profileBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) senderObject?.icon?.getBitmap() else null
    val imageBitmap = extras.getBundle("android.wearable.EXTENSIONS")?.getParcelableCompat<Bitmap>("background")

    val room = Room(
      id = roomId,
      name = roomName,
      isGroupChat = isGroupChat,
      replier = sbn.replier(),
    )
    val sender = Profile(
      name = senderName,
      id = senderId,
      picture = profileBitmap?.toSource()?.let(::V8Source) ?: V8Source.Empty,
    )

    return Message(
      content = message,
      image = imageBitmap?.toSource()?.let(::V8Source) ?: V8Source.Empty,
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

  private fun Bitmap.toSource(): Source =
    Buffer().apply { compress(CompressFormatCompat.WebpOrPng(), /* quality = */ 100, outputStream()) }

  private fun StatusBarNotification.replier(): Replier {
    var readAction: Notification.Action? = null
    var sendAction: Notification.Action? = null

    notification.actions.forEach { action ->
      val title = action.title.toString()
      when {
        title.contains(ACTION_READ_TITLE) -> readAction = action
        title.contains(ACTION_REPLY_TITLE) -> sendAction = action
      }
    }

    if (readAction == null && sendAction == null)
      return Replier.Unavailable

    fun send(message: String): Result<Unit> = runCatching {
      if (sendAction == null) error("sendAction is null.")

      val results = sendAction.remoteInputs.fold(Bundle()) { acc, input ->
        acc.apply { putCharSequence(input.resultKey, message) }
      }
      val sendingIntent = Intent().apply {
        RemoteInput.addResultsToIntent(sendAction.remoteInputs, this, results)
      }
      sendAction.actionIntent.send(context, /* code = */ 0, sendingIntent)
    }

    fun read(): Result<Unit> = runCatching {
      if (readAction == null) error("readAction is null.")
      readAction.actionIntent.send(context, /* code = */ 1, /* intent = */ null)
    }

    return object : Replier {
      override fun markAsRead(): Boolean = read().isSuccess
      override fun markAsRead(room: String): Boolean =
        Room.findRoomByName(room)?.replier?.markAsRead() == true

      override fun reply(message: String): Boolean = send(message).isSuccess
      override fun reply(room: String, message: String): Boolean =
        Room.findRoomByName(room)?.replier?.reply(message) == true

      override fun toString(): String = REPLIER_NAME + "@" + hashCode().toString(16)
    }
  }

  public companion object {
    private val TAG = KakaoTalkEngine::class.simpleName!!
    private const val REPLIER_NAME = "KakaoTalkReplier"

    private val ACTION_REPLY_TITLE = Regex("reply|답장", RegexOption.IGNORE_CASE)
    private val ACTION_READ_TITLE = Regex("read|읽음", RegexOption.IGNORE_CASE)
  }
}
