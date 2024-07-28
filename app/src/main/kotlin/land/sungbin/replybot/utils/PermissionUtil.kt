/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings.Secure

fun Context.hasNotificationAccessPermission(): Boolean =
  Secure.getString(applicationContext.contentResolver, "enabled_notification_listeners")
    .contains(applicationContext.packageName)

fun Context.requestNotificationAccessPermission() {
  startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
}
