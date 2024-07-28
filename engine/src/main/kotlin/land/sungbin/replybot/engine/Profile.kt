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

public data class Profile(
  @get:V8Property public val name: String,
  @get:V8Property public val id: String?,
  @get:V8Property public val picture: V8Source,
) {
  public constructor(
    name: String,
    id: String?,
    picture: Source,
  ) : this(name, id, V8Source(picture))
}
