/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.service

import android.service.notification.StatusBarNotification
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import land.sungbin.replybot.configuration.AppConfiguration
import land.sungbin.replybot.engine.Room
import land.sungbin.replybot.scriptable.JavaScriptRunner
import timber.log.Timber

class NotificationListener : LifecycleNotificationListenerService() {
  override fun onListenerConnected() {
    Timber.tag(TAG).i("Connected.")
    super.onListenerConnected()
  }

  override fun onListenerDisconnected() {
    Timber.tag(TAG).i("Disconnected.")
    super.onListenerDisconnected()
  }

  override fun onNotificationPosted(sbn: StatusBarNotification) {
    Timber.tag(TAG).i("[onNotificationPosted] %s", sbn)
    val messages = AppConfiguration.engines.mapNotNull { engine -> engine.createNormalMessage(sbn) }
    lifecycleScope.launch(Dispatchers.Default) { launch { messages.forEach(JavaScriptRunner::invokeOnNewMessage) } }
  }

  override fun onNotificationRemoved(sbn: StatusBarNotification, rankingMap: RankingMap, reason: Int) {
    Timber.tag(TAG).i("[onNotificationRemoved] %s , %d", sbn, reason)
    val messages = AppConfiguration.engines.mapNotNull { engine ->
      engine.createDeletedMessage(sbn = sbn, reason = reason)
    }
    lifecycleScope.launch(Dispatchers.Default) { launch { messages.forEach(JavaScriptRunner::invokeOnDeletedMessage) } }
  }

  override fun onDestroy() {
    JavaScriptRunner.close()
    Room.clearCaches()
    AppConfiguration.close()
    super.onDestroy()
  }

  companion object {
    private val TAG = NotificationListener::class.simpleName!!
  }
}
