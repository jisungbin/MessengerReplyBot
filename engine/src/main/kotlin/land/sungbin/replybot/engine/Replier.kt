// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine

import com.caoccao.javet.annotations.V8Function

public interface Replier {
  @V8Function public fun markAsRead(): Boolean
  @V8Function public fun markAsRead(room: String): Boolean

  @V8Function public fun reply(message: String): Boolean
  @V8Function public fun reply(room: String, message: String): Boolean

  public companion object {
    public val Unavailable: Replier = object : Replier {
      override fun markAsRead(): Boolean = false
      override fun markAsRead(room: String): Boolean = false

      override fun reply(message: String): Boolean = false
      override fun reply(room: String, message: String): Boolean = false

      override fun toString(): String = "UnavailableReplier"
    }
  }
}
