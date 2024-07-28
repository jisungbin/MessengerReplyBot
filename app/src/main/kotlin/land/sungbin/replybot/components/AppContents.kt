/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.components

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalContext
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import land.sungbin.replybot.utils.getEditorDirectory
import land.sungbin.replybot.utils.readOrEmpty
import okio.FileSystem

@Composable fun AppContent(
  navigationItem: AppNavigationItem,
  editorType: EditorType,
  editorState: WebViewState,
  editorNavigator: WebViewNavigator,
  fs: FileSystem = FileSystem.SYSTEM,
  modifier: Modifier = Modifier,
  onEditorTabChange: (tab: EditorType) -> Unit,
) {
  val context = LocalContext.current
  val editorDirectory = getEditorDirectory()
  val nativeEditor = remember { Ref<NativeWebView>() }

  DisposableEffect(nativeEditor) {
    nativeEditor.value = NativeWebView(context).also { web ->
      web.webChromeClient = WebChromeClient()
      web.settings.apply {
        builtInZoomControls = false
        displayZoomControls = false
        allowFileAccess = true
        allowContentAccess = true
        cacheMode = WebSettings.LOAD_CACHE_ONLY
        setSupportZoom(false)

        @Suppress("SetJavaScriptEnabled")
        javaScriptEnabled = true
      }
    }
    onDispose { nativeEditor.value = null }
  }

  Box(
    modifier = modifier,
    propagateMinConstraints = true,
  ) {
    when (navigationItem) {
      AppNavigationItem.Editors -> {
        val initialCode by remember(editorDirectory, fs) {
          derivedStateOf { fs.readOrEmpty(editorDirectory.resolve(editorType.filename)) }
        }

        EditorsContent(
          current = editorType,
          state = editorState,
          navigator = editorNavigator,
          nativeView = nativeEditor,
          initialCode = initialCode,
          onTabClick = onEditorTabChange,
        )
      }
      AppNavigationItem.DebugRoom -> {
        DebugRoomContent()
      }
      AppNavigationItem.Log -> {
        LogContent()
      }
      AppNavigationItem.Settings -> {
        SettingsContent()
      }
    }
  }
}
