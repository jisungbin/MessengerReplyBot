/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewFactoryParam
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator

const val AceEditorAssetPath = "file:///android_asset/ace-builds-src-min-noconflict/editor.html"

// TODO dynamic font size and theme
@Composable @NonRestartableComposable
fun AceCodeEditor(
  path: String = AceEditorAssetPath,
  state: WebViewState = remember(path) {
    WebViewState(WebContent.Url(path)).apply {
      webSettings.supportZoom = false
    }
  },
  navigator: WebViewNavigator = rememberWebViewNavigator(),
  modifier: Modifier = Modifier,
  factory: ((WebViewFactoryParam) -> NativeWebView)? = null,
) {
  WebView(
    modifier = modifier,
    state = state,
    navigator = navigator,
    captureBackPresses = false,
    factory = factory,
  )
}
