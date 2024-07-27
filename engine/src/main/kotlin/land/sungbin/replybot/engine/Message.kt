/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import okio.Source

public sealed interface Message {
  public val content: String
  public val logId: Long

  /** @see [EngineFactory.identifier] */
  public val identifier: String

  public data class Normal(
    override val content: String,
    public val image: Source,
    public val room: Room,
    public val sender: Profile,
    public val hasMention: Boolean,
    override val logId: Long,
    override val identifier: String,
  ) : Message

  public data class Deleted(
    override val content: String,
    public val room: Room?,
    public val sender: String,
    override val logId: Long,
    override val identifier: String,
  ) : Message

  public companion object {
    public operator fun invoke(
      content: String,
      image: Source,
      room: Room,
      sender: Profile,
      hasMention: Boolean,
      logId: Long,
      identifier: String,
    ): Normal =
      Normal(content, image, room, sender, hasMention, logId, identifier)
  }
}
