/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Property
import java.util.concurrent.ConcurrentHashMap

public data class Room(
  @get:V8Property public val id: String,
  @get:V8Property public val name: String,
  @get:V8Property public val isGroupChat: Boolean,
  @get:V8Property public val isDebugRoom: Boolean = false,
  @get:V8Property public val replier: Replier,
) {
  init {
    idCaches[id] = this
    nameCaches[name] = this
  }

  public companion object {
    private val idCaches = ConcurrentHashMap<String, Room>()
    private val nameCaches = ConcurrentHashMap<String, Room>()

    public fun findRoomById(id: String): Room? = idCaches[id]
    public fun findRoomByName(name: String): Room? = nameCaches[name]

    public fun clearCaches() {
      idCaches.clear()
      nameCaches.clear()
    }
  }
}
