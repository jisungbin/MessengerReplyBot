// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.annotations.V8Property
import land.sungbin.replybot.engine.v8.V8Source

public data class Profile(
  @get:V8Property public val name: String,
  @get:V8Property public val id: String?,
  @get:V8Property public val picture: V8Source,
) {
  @V8Function public fun dump(): String = """
    |Profile(
    |  name=$name,
    |  id=$id,
    |  picture=${picture.readBytes().size} bytes
    |)
  """.trimMargin()
}
