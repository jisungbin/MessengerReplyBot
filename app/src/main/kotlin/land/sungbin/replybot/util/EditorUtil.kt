// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.util

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import land.sungbin.replybot.components.EditorType
import okio.Path
import okio.Path.Companion.toPath
import timber.log.Timber

suspend fun WebViewState.awaitLoaded() {
  snapshotFlow { loadingState }.first { state -> state == LoadingState.Finished }
}

@WorkerThread
suspend fun WebViewState.getCurrentCode(navigator: WebViewNavigator): String {
  awaitLoaded()
  return withContext(Dispatchers.IO) {
    suspendCoroutine { cont ->
      navigator.evaluateJavaScript("editor.getValue()") { code ->
        val result = code.removeSurrounding("\"").replace("\\n", "\n")
        cont.resume(result.also { Timber.tag("EditorCode").i("%s", it) })
      }
    }
  }
}

@[Composable ReadOnlyComposable]
fun getEditorDirectory(): Path =
  LocalContext.current
    .getDir(EditorType.CODE_DIRECTORY_NAME, Context.MODE_PRIVATE)
    .absolutePath
    .toPath()
