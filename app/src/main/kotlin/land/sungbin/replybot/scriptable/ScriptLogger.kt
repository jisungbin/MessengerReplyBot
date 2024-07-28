/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.scriptable

import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.interfaces.IJavetLogger
import timber.log.Timber

class ScriptLogger(private val tag: String) : IJavetLogger {
  @V8Function override fun info(message: String?) {
    Timber.tag(tag).i(message)
  }

  @V8Function override fun debug(message: String?) {
    Timber.tag(tag).d(message)
  }

  @V8Function override fun warn(message: String?) {
    Timber.tag(tag).w(message)
  }

  @V8Function override fun error(message: String?) {
    Timber.tag(tag).e(message)
  }

  @V8Function override fun error(message: String?, cause: Throwable?) {
    Timber.tag(tag).e(cause, message)
  }
}
