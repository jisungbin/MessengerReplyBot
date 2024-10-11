// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.res.stringResource
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.sebaslogen.resaca.rememberScoped
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import land.sungbin.replybot.R
import land.sungbin.replybot.util.awaitLoaded

enum class EditorType(@StringRes val label: Int, val filename: String) {
  Main(R.string.editor_main, "main.ts"),
  Test(R.string.editor_test, "test.ts"),
  ;

  companion object {
    const val CODE_DIRECTORY_NAME = "editors"
    val Default = EditorType.entries.toImmutableList()
  }
}

// TODO apply throttle to actions clicks
// FIXME screen flickering when switching tabs
@Composable fun EditorsContent(
  current: EditorType,
  modifier: Modifier = Modifier,
  tabs: ImmutableList<EditorType> = EditorType.Default,
  state: WebViewState = rememberScoped {
    WebViewState(WebContent.Url(AceEditorAssetPath)).apply {
      webSettings.supportZoom = false
    }
  },
  navigator: WebViewNavigator = rememberWebViewNavigator(),
  nativeView: Ref<NativeWebView> = remember { Ref() },
  initialCode: String = "",
  onTabClick: (tab: EditorType) -> Unit,
) {
  Column(modifier = modifier) {
    SecondaryTabRow(
      modifier = Modifier.fillMaxWidth(),
      selectedTabIndex = tabs.indexOf(current),
    ) {
      tabs.forEach { tab ->
        Tab(
          selected = tab == current,
          text = { Text(text = stringResource(tab.label)) },
          onClick = { onTabClick(tab) },
        )
      }
    }
    if (current == EditorType.Main) {
      CodeEditor(
        modifier = Modifier.fillMaxSize(),
        state = state,
        navigator = navigator,
        nativeView = nativeView,
        initialCode = initialCode,
      )
    } else {
      Text(text = "TODO: ${current.name}")
    }
  }
}

// TODO quick buttons for undo, redo
@Composable private fun CodeEditor(
  state: WebViewState,
  navigator: WebViewNavigator,
  nativeView: Ref<NativeWebView>,
  modifier: Modifier = Modifier,
  initialCode: String = "",
) {
  LaunchedEffect(initialCode, state, navigator) {
    if (initialCode.isBlank()) return@LaunchedEffect
    state.awaitLoaded()
    navigator.evaluateJavaScript("editor.setValue(`$initialCode`)")
  }

  AceCodeEditor(
    modifier = modifier,
    state = state,
    navigator = navigator,
    factory = nativeView.value?.let { native -> { native } },
  )
}
