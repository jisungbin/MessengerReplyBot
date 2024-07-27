/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Allow
import com.caoccao.javet.annotations.V8Convert
import com.caoccao.javet.annotations.V8Getter
import com.caoccao.javet.enums.V8ConversionMode
import com.caoccao.javet.enums.V8ProxyMode
import land.sungbin.replybot.engine.v8.V8Source
import okio.Source

public sealed interface Message {
  public val content: String
  public val logId: Long

  /** @see [EngineFactory.identifier] */
  public val identifier: String

  @V8Convert(mode = V8ConversionMode.AllowOnly, proxyMode = V8ProxyMode.Class)
  public data class Normal(
    @get:[V8Allow V8Getter] override val content: String,
    @get:[V8Allow V8Getter] public val image: V8Source,
    @get:[V8Allow V8Getter] public val room: Room,
    @get:[V8Allow V8Getter] public val sender: Profile,
    @get:[V8Allow V8Getter] public val hasMention: Boolean,
    @get:[V8Allow V8Getter] override val logId: Long,
    @get:[V8Allow V8Getter] override val identifier: String,
  ) : Message

  @V8Convert(mode = V8ConversionMode.AllowOnly, proxyMode = V8ProxyMode.Class)
  public data class Deleted(
    @get:[V8Allow V8Getter] override val content: String,
    @get:[V8Allow V8Getter] public val room: Room?,
    @get:[V8Allow V8Getter] public val sender: String,
    @get:[V8Allow V8Getter] override val logId: Long,
    @get:[V8Allow V8Getter] override val identifier: String,
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
      Normal(content, V8Source(image), room, sender, hasMention, logId, identifier)
  }
}
