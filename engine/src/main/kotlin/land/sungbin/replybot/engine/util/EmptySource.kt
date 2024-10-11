// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine.util

import okio.Buffer
import okio.Source
import okio.Timeout

public object EmptySource : Source {
  override fun read(sink: Buffer, byteCount: Long): Long = -1L
  override fun timeout(): Timeout = Timeout.NONE
  override fun close() {}
}
