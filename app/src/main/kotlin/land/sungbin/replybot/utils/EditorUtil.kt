/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.flow.first
import land.sungbin.replybot.components.EditorTab
import okio.Path
import okio.Path.Companion.toPath

suspend fun WebViewState.awaitLoaded() {
  snapshotFlow { loadingState }.first { state -> state == LoadingState.Finished }
}

suspend fun getCurrentCode(state: WebViewState, navigator: WebViewNavigator): String {
  state.awaitLoaded()
  return suspendCoroutine { cont ->
    navigator.evaluateJavaScript("editor.getValue()") { code ->
      cont.resume(code.removeSurrounding("\""))
    }
  }
}

@Composable @ReadOnlyComposable
fun getEditorDirectory(): Path =
  LocalContext.current
    .getDir(EditorTab.CODE_DIRECTORY_NAME, Context.MODE_PRIVATE)
    .absolutePath
    .toPath()
