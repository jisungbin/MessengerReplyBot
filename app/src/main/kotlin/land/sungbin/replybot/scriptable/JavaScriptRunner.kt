/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.scriptable

import androidx.annotation.WorkerThread
import com.caoccao.javet.interop.V8Host
import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.values.reference.V8ValueFunction
import java.io.Closeable
import land.sungbin.replybot.components.EditorType
import land.sungbin.replybot.engine.Message

object JavaScriptRunner : Closeable {
  private val proxyConverter by lazy { JavetProxyConverter() }
  private val mainRuntime by lazy {
    V8Host.getV8Instance().createV8Runtime<V8Runtime>().apply {
      converter = proxyConverter
    }
  }

  @WorkerThread fun initializeMain(tsCode: String) {
    mainRuntime.resetContext()
    mainRuntime.getExecutor(ts2js(tsCode, EditorType.Main))
      .setResourceName("main.js")
      .compileV8Script()
      .executeVoid()
  }

  @WorkerThread fun invokeOnNewMessage(message: Message.Normal) {
    val v8Message = mainRuntime.v8Scope.createV8ValueObject().apply {
      bind(message)
    }

    mainRuntime.getExecutor("onNewMessage")
      .execute<V8ValueFunction>()
      .callVoid(mainRuntime.globalObject, v8Message)

    v8Message.unbind(message)
  }

  @WorkerThread fun invokeOnDeletedMessage(message: Message.Deleted) {
    val v8Message = mainRuntime.v8Scope.createV8ValueObject().apply {
      bind(message)
    }

    mainRuntime.getExecutor("onDeletedMessage")
      .execute<V8ValueFunction>()
      .callVoid(mainRuntime.globalObject, v8Message)

    v8Message.unbind(message)
  }

  override fun close() {
    mainRuntime.close()
    System.gc()
    System.runFinalization()
  }
}
