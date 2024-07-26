/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine.katalk

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable

internal inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? =
  when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
  }

internal inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String): Array<out T>? =
  when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArray(key, T::class.java)
    else -> @Suppress("DEPRECATION", "UNCHECKED_CAST") (getParcelableArray(key) as? Array<T>)
  }

internal object CompressFormatCompat {
  @Suppress("FunctionName")
  fun WebpOrPng(): Bitmap.CompressFormat =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS
    else Bitmap.CompressFormat.PNG
}
