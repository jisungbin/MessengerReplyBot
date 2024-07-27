/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine.katalk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.service.notification.StatusBarNotification
import timber.log.Timber

private val cachedIconGetBitmap by lazy {
  runCatching { Icon::class.java.getMethod("getBitmap") }
    .also { Timber.d("Icon::getBitmap is $it") }
    .getOrNull()
}

private val cachedSbnGetContext by lazy {
  runCatching {
    @Suppress("PrivateApi")
    StatusBarNotification::class.java.getDeclaredField("mContext").apply {
      isAccessible = true
    }
  }
    .also { Timber.d("StatusBarNotification::mContext is $it") }
    .getOrNull()
}

internal fun Icon.getBitmap(): Bitmap? = cachedIconGetBitmap?.invoke(this) as? Bitmap

internal fun StatusBarNotification.getContext(): Context? = cachedSbnGetContext?.get(this) as? Context
