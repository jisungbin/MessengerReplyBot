// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine.v8

import com.caoccao.javet.annotations.V8Function
import land.sungbin.replybot.engine.util.EmptySource
import okio.Source
import okio.buffer

@JvmInline public value class V8Source(internal val source: Source) {
  @V8Function public fun readUtf8(): String = source.buffer().use { it.readUtf8() }
  @V8Function public fun readBase64(): String = source.buffer().use { it.readByteString().base64() }
  @V8Function public fun readBytes(): ByteArray = source.buffer().use { it.readByteArray() }

  public companion object {
    public val Empty: V8Source = V8Source(EmptySource)
  }
}
