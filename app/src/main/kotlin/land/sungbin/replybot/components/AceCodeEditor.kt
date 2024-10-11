// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewFactoryParam
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.sebaslogen.resaca.rememberScoped

const val AceEditorAssetPath = "file:///android_asset/ace-builds-src-min-noconflict/editor.html"

// TODO dynamic font size and theme
@Composable @NonRestartableComposable
fun AceCodeEditor(
  modifier: Modifier = Modifier,
  state: WebViewState = rememberScoped {
    WebViewState(WebContent.Url(AceEditorAssetPath)).apply {
      webSettings.supportZoom = false
    }
  },
  navigator: WebViewNavigator = rememberWebViewNavigator(),
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
