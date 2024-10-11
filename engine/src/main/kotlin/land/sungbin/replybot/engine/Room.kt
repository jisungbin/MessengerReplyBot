// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.annotations.V8Property
import java.util.concurrent.ConcurrentHashMap

public data class Room(
  @get:V8Property public val id: String,
  @get:V8Property public val name: String,
  @get:V8Property public val isGroupChat: Boolean,
  @get:V8Property public val isDebugRoom: Boolean = false,
  @get:V8Property public val replier: Replier,
) : Replier by replier {
  @V8Function public fun dump(): String = """
    |Room(
    |  id=$id,
    |  name=$name,
    |  isGroupChat=$isGroupChat,
    |  isDebugRoom=$isDebugRoom,
    |  replier=$replier
    |)
  """.trimMargin()

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
