/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import land.sungbin.replybot.engine.utils.ConcurrentWeakHashMap

public data class Room(
  val id: String,
  val name: String,
  val isGroupChat: Boolean,
  val isDebugRoom: Boolean = false,
  val replier: Replier,
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
