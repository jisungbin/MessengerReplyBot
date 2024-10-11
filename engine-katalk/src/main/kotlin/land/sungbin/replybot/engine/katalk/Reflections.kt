// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine.katalk

import android.graphics.Bitmap
import android.graphics.drawable.Icon
import timber.log.Timber

private val cachedIconGetBitmap by lazy {
  runCatching { Icon::class.java.getMethod("getBitmap") }
    .also { Timber.d("Icon::getBitmap is $it") }
    .getOrNull()
}

internal fun Icon.getBitmap(): Bitmap? = cachedIconGetBitmap?.invoke(this) as? Bitmap
