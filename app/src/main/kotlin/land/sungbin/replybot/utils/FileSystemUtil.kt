/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.utils

import okio.FileSystem
import okio.Path

@Suppress("NOTHING_TO_INLINE") // Syntactic sugar.
inline fun FileSystem.readOrEmpty(path: Path): String =
  if (!exists(path)) "" else read(path) { readUtf8() }
