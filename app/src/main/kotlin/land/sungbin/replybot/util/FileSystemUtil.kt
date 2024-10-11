// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.util

import okio.Buffer
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.buffer

@Suppress("NOTHING_TO_INLINE") // Syntactic sugar.
inline fun FileSystem.readOrEmpty(path: Path): BufferedSource =
  if (!exists(path)) Buffer() else source(path).buffer()

@Suppress("NOTHING_TO_INLINE") // Syntactic sugar.
inline fun FileSystem.readOrDefault(path: Path, defaultValue: () -> String): BufferedSource =
  if (!exists(path)) Buffer().writeUtf8(defaultValue()) else source(path).buffer()
