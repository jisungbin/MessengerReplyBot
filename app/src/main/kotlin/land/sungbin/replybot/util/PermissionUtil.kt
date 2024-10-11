// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.util

import android.content.Context
import android.content.Intent
import android.provider.Settings.Secure

fun Context.hasNotificationAccessPermission(): Boolean =
  Secure.getString(applicationContext.contentResolver, "enabled_notification_listeners")
    .contains(applicationContext.packageName)

@Suppress("NOTHING_TO_INLINE") // Syntactic sugar.
inline fun Context.requestNotificationAccessPermission() {
  startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
}
