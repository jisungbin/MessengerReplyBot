/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Property
import land.sungbin.replybot.engine.v8.V8Source
import okio.Source

public sealed interface Message {
  public val content: String
  public val logId: Long

  /** @see [EngineFactory.identifier] */
  public val identifier: String

  public data class Normal(
    @get:V8Property override val content: String,
    @get:V8Property public val image: V8Source,
    @get:V8Property public val room: Room,
    @get:V8Property public val sender: Profile,
    @get:V8Property public val hasMention: Boolean,
    @get:V8Property override val logId: Long,
    @get:V8Property override val identifier: String,
  ) : Message {
    @get:V8Property public val replier: Replier = room.replier
  }

  public data class Deleted(
    @get:V8Property override val content: String,
    @get:V8Property public val room: Room?,
    @get:V8Property public val sender: String,
    @get:V8Property override val logId: Long,
    @get:V8Property override val identifier: String,
  ) : Message {
    @get:V8Property public val replier: Replier? = room?.replier
  }

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
      Normal(content, V8Source(image), room, sender, hasMention, logId, identifier)
  }
}
