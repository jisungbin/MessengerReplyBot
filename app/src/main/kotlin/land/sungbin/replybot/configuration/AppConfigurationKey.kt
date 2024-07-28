/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.configuration

sealed interface AppConfigurationKey<T> {
  val label: String
  fun defaultValue(): T

  data object Engines : AppConfigurationKey<List<String>> {
    override val label = "engines"
    override fun defaultValue(): List<String> = listOf("com.kakao.talk")
  }
}
