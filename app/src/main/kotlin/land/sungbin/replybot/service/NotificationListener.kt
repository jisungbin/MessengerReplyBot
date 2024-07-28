/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import land.sungbin.replybot.configuration.AppConfiguration
import land.sungbin.replybot.scriptable.JavaScriptRunner

class NotificationListener : NotificationListenerService() {
  override fun onNotificationPosted(sbn: StatusBarNotification) {
    val messages = AppConfiguration.engines.mapNotNull { engine -> engine.createNormalMessage(sbn) }
    messages.forEach(JavaScriptRunner::invokeOnNewMessage)
  }

  override fun onNotificationRemoved(sbn: StatusBarNotification) {
    val messages = AppConfiguration.engines.mapNotNull { engine -> engine.createDeletedMessage(sbn) }
    messages.forEach(JavaScriptRunner::invokeOnDeletedMessage)
  }
}
