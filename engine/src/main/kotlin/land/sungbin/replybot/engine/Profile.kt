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

@V8Convert(mode = V8ConversionMode.AllowOnly, proxyMode = V8ProxyMode.Class)
public data class Profile(
  @get:[V8Allow V8Getter] public val name: String,
  @get:[V8Allow V8Getter] public val id: String?,
  @get:[V8Allow V8Getter] public val picture: V8Source,
) {
  public constructor(
    name: String,
    id: String?,
    picture: Source,
  ) : this(name, id, V8Source(picture))
}
