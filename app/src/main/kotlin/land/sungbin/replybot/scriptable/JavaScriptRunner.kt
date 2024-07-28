/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.scriptable

import android.content.Context
import androidx.annotation.WorkerThread
import com.caoccao.javet.interop.V8Host
import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.values.reference.V8ValueFunction
import java.io.Closeable
import land.sungbin.replybot.engine.Message
import org.intellij.lang.annotations.Language
import timber.log.Timber

object JavaScriptRunner : Closeable {
  private val TAG = JavaScriptRunner::class.simpleName!!
  private val proxyConverter by lazy { JavetProxyConverter() }

  private val ts2jsRuntime by lazy { V8Host.getV8Instance().createV8Runtime<V8Runtime>() }
  private val mainRuntime by lazy {
    V8Host.getV8Instance().createV8Runtime<V8Runtime>().also { runtime ->
      runtime.logger = ScriptLogger("v8-runner")
      runtime.converter = proxyConverter
    }
  }

  @WorkerThread fun initializeTs2Js(context: Context) {
    val code = context.assets.open("ts2js/typescript.js").bufferedReader().use { it.readText() }

    runCatching { ts2jsRuntime.resetContext() }
    ts2jsRuntime.getExecutor(code)
      .setResourceName("ts2js.js")
      .compileV8Script()
      .executeVoid()

    Timber.tag(TAG).i("Ts2Js runtime initialized.")
  }

  @WorkerThread fun initializeMain(tsCode: String) {
    @Language("json") val ts2jsCompileOptions = """
    { 
      "compilerOptions": {
        "module": "NONE",
        "target": "ES3",
        "moduleResolution": "classic"
      }
    }
    """
      .trimIndent()
      .replace("\n", " ")

    val jsCode = ts2jsRuntime.getExecutor("ts.transpileModule(`$tsCode`, $ts2jsCompileOptions).outputText")
      .executeString()
      .also { Timber.tag("ts2js").i(it) }

    runCatching { mainRuntime.resetContext() }
    val logger = mainRuntime.v8Scope.createV8ValueObject().apply { bind(ScriptLogger("main.js")) }
    mainRuntime.globalObject.set("logger", logger)
    mainRuntime.getExecutor(jsCode)
      .setResourceName("main.js")
      .compileV8Script()
      .executeVoid()

    Timber.tag(TAG).i("Main runtime initialized.")
  }

  @WorkerThread fun invokeOnNewMessage(message: Message.Normal) {
    val v8Message = mainRuntime.v8Scope.createV8ValueObject().apply { bind(message) }

    mainRuntime.getExecutor("onNewMessage")
      .execute<V8ValueFunction>()
      .also { Timber.tag(TAG).i("onNewMessage: %s", it) }
      .callVoid(mainRuntime.globalObject, v8Message)

    v8Message.unbind(message)
  }

  @WorkerThread fun invokeOnDeletedMessage(message: Message.Deleted) {
    val v8Message = mainRuntime.v8Scope.createV8ValueObject().apply { bind(message) }

    mainRuntime.getExecutor("onDeletedMessage")
      .execute<V8ValueFunction>()
      .also { Timber.tag(TAG).i("onDeletedMessage: %s", it) }
      .callVoid(mainRuntime.globalObject, v8Message)

    v8Message.unbind(message)
  }

  override fun close() {
    ts2jsRuntime.close()
    mainRuntime.close()

    // https://www.caoccao.com/Javet/reference/resource_management/memory_management.html#system-gc-and-system-runfinalization
    System.gc()
    System.runFinalization()
  }
}
