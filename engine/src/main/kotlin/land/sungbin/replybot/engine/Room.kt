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
import land.sungbin.replybot.engine.utils.ConcurrentWeakHashMap

@V8Convert(mode = V8ConversionMode.AllowOnly, proxyMode = V8ProxyMode.Class)
public data class Room(
  @get:[V8Allow V8Getter] public val id: String,
  @get:[V8Allow V8Getter] public val name: String,
  @get:[V8Allow V8Getter] public val isGroupChat: Boolean,
  @get:[V8Allow V8Getter] public val isDebugRoom: Boolean = false,
  @get:[V8Allow V8Getter] public val replier: Replier,
) {
  init {
    idCaches[id] = this
    nameCaches[name] = this
  }

  public companion object {
    private val idCaches = ConcurrentWeakHashMap<String, Room>()
    private val nameCaches = ConcurrentWeakHashMap<String, Room>()

    public fun findRoomById(id: String): Room? = idCaches[id]
    public fun findRoomByName(name: String): Room? = nameCaches[name]
  }
}
