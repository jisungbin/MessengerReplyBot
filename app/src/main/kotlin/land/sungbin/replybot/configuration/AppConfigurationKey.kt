// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.configuration

sealed interface AppConfigurationKey<T> {
  val label: String
  fun defaultValue(): T

  data object Engines : AppConfigurationKey<List<String>> {
    override val label = "engines"
    override fun defaultValue(): List<String> = listOf("com.kakao.talk")
  }
}
