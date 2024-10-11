// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.annotations.V8Property
import land.sungbin.replybot.engine.v8.V8Source

public data class Message(
  @get:V8Property public val content: String,
  @get:V8Property public val image: V8Source,
  @get:V8Property public val room: Room,
  @get:V8Property public val sender: Profile,
  @get:V8Property public val hasMention: Boolean,
  @get:V8Property public val logId: Long,
  @get:V8Property public val identifier: String,
) : Replier by room.replier {
  @V8Function public fun dump(): String = """
    |Message(
    |  content=$content,
    |  image=${image.readBytes().size} bytes,
    |  room=${room.dump()},
    |  sender=${sender.dump()},
    |  hasMention=$hasMention,
    |  logId=$logId,
    |  identifier=$identifier
    |)
  """.trimMargin()
}
