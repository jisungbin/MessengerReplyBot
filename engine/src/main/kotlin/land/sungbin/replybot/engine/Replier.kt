/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Allow
import com.caoccao.javet.annotations.V8Convert
import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.enums.V8ConversionMode
import com.caoccao.javet.enums.V8ProxyMode

@V8Convert(mode = V8ConversionMode.AllowOnly, proxyMode = V8ProxyMode.Class)
public interface Replier {
  @[V8Allow V8Function] public fun markAsRead(): Boolean
  @[V8Allow V8Function] public fun markAsRead(room: String): Boolean

  @[V8Allow V8Function] public fun reply(message: String): Boolean
  @[V8Allow V8Function] public fun reply(room: String, message: String): Boolean
}
