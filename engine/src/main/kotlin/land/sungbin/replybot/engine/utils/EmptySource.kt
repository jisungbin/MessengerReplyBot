/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine.utils

import okio.Buffer
import okio.Source
import okio.Timeout

public object EmptySource : Source {
  override fun read(sink: Buffer, byteCount: Long): Long = -1L
  override fun timeout(): Timeout = Timeout.NONE
  override fun close() {}
}
