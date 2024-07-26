/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

public interface Replier {
  public fun markAsRead(): Boolean
  public fun markAsRead(room: String): Boolean

  public fun reply(message: String): Boolean
  public fun reply(room: String, message: String): Boolean
}
