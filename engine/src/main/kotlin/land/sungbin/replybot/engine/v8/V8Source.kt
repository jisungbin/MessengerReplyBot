/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine.v8

import com.caoccao.javet.annotations.V8Function
import okio.Source
import okio.buffer

public class V8Source(internal val source: Source) {
  @V8Function public fun readUtf8(): String = source.buffer().use { it.readUtf8() }
  @V8Function public fun readBase64(): String = source.buffer().use { it.readByteString().base64() }
  @V8Function public fun readBytes(): ByteArray = source.buffer().use { it.readByteArray() }
}
