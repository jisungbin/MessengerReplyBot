// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.scriptable

import androidx.annotation.WorkerThread
import com.caoccao.javet.interop.V8Host
import com.caoccao.javet.interop.V8Runtime
import com.caoccao.javet.interop.converters.JavetProxyConverter
import com.caoccao.javet.swc4j.Swc4j
import com.caoccao.javet.swc4j.enums.Swc4jEsVersion
import com.caoccao.javet.swc4j.enums.Swc4jMediaType
import com.caoccao.javet.swc4j.enums.Swc4jSourceMapOption
import com.caoccao.javet.swc4j.options.Swc4jTransformOptions
import com.caoccao.javet.swc4j.options.Swc4jTranspileOptions
import com.caoccao.javet.values.reference.V8ValueFunction
import java.io.Closeable
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import land.sungbin.replybot.engine.Message
import timber.log.Timber

object ScriptRunner : Closeable {
  private val TAG = ScriptRunner::class.simpleName!!

  private val swc by lazy { Swc4j() }
  private val proxyConverter by lazy { JavetProxyConverter() }

  // Ensures ES3 compatibility.
  private val tsLoweringOptions = Swc4jTransformOptions()
    .setMinify(false)
    .setSpecifier(URL("file:///ts-main.ts"))
    .setMediaType(Swc4jMediaType.TypeScript)
    .setSourceMap(Swc4jSourceMapOption.None)
    .setTarget(Swc4jEsVersion.ES3)

  private val ts2jsOptions = Swc4jTranspileOptions()
    .setSpecifier(URL("file:///ts-main.ts"))
    .setMediaType(Swc4jMediaType.TypeScript)
    .setSourceMap(Swc4jSourceMapOption.None)

  private val mainRuntime by lazy {
    V8Host.getV8Instance().createV8Runtime<V8Runtime>().also { runtime ->
      runtime.logger = ScriptLogger.V8Runner
      runtime.converter = proxyConverter
    }
  }

  @WorkerThread suspend fun initializeMain(code: String): Result<Unit> = suspendCoroutine { cont ->
    runCatching {
      // An NPE will occur if the runtime instance is not already alive. Ignore this NPE.
      try {
        mainRuntime.resetContext()
        Timber.tag(TAG).i("Main runtime reset.")
      } catch (_: NullPointerException) {
      }

      val loweredTs = swc.transform(code, tsLoweringOptions).code
      Timber.tag(TAG).i("Lowered TypeScript:\n%s", loweredTs)

      val jsMainCode = swc.transpile(loweredTs, ts2jsOptions).code
      Timber.tag(TAG).i("Transpiled JavaScript:\n%s", jsMainCode)

      val logger = mainRuntime.v8Scope.createV8ValueObject().apply { bind(ScriptLogger.Main) }
      mainRuntime.globalObject.set("logger", logger)

      mainRuntime.getExecutor(jsMainCode)
        .setResourceName("main.js")
        .compileV8Script()
        .executeVoid()

      Timber.tag(TAG).i("Main runtime initialized.")
    }
      .let(cont::resume)
  }

  @WorkerThread suspend fun invokeOnNewMessage(message: Message): Result<Unit> = suspendCoroutine { cont ->
    runCatching {
      val v8Message = mainRuntime.v8Scope.createV8ValueObject().apply { bind(message) }

      mainRuntime.getExecutor("onNewMessage")
        .execute<V8ValueFunction>()
        .callVoid(mainRuntime.globalObject, v8Message)

      Unit.also { v8Message.unbind(message) }
    }
      .let(cont::resume)
  }

  override fun close() {
    mainRuntime.close()

    // https://www.caoccao.com/Javet/reference/resource_management/memory_management.html#system-gc-and-system-runfinalization
    System.gc()
    System.runFinalization()
  }
}
