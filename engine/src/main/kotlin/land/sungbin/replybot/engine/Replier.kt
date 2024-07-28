/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Function

public interface Replier {
  @V8Function public fun markAsRead(): Boolean
  @V8Function public fun markAsRead(room: String): Boolean

  @V8Function public fun reply(message: String): Boolean
  @V8Function public fun reply(room: String, message: String): Boolean
}
