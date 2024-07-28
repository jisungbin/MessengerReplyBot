/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import land.sungbin.replybot.components.AceEditorAssetPath
import land.sungbin.replybot.components.AppContent
import land.sungbin.replybot.components.AppNavigationBar
import land.sungbin.replybot.components.AppNavigationItem
import land.sungbin.replybot.components.AppTopBar
import land.sungbin.replybot.components.EditorType
import land.sungbin.replybot.components.GlobalAction
import land.sungbin.replybot.configuration.AppConfiguration
import land.sungbin.replybot.scriptable.JavaScriptRunner
import land.sungbin.replybot.utils.getCurrentCode
import land.sungbin.replybot.utils.getEditorDirectory
import okio.FileSystem
import okio.Path.Companion.toPath

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
    super.onCreate(savedInstanceState)
    AppConfiguration.start(
      fs = FileSystem.SYSTEM,
      directory = getDir("settings", MODE_PRIVATE).absolutePath.toPath(),
    )

    setContent {
      var navigationItem by remember { mutableStateOf<AppNavigationItem>(AppNavigationItem.Editors) }
      var editorType by remember { mutableStateOf<EditorType>(EditorType.Main) }

      val editorDirectory = getEditorDirectory()
      val editorState = remember {
        WebViewState(WebContent.Url(AceEditorAssetPath)).apply {
          webSettings.supportZoom = false
        }
      }
      val editorNavigator = rememberWebViewNavigator()

      MaterialTheme(colorScheme = dynamicThemeScheme()) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            AppTopBar(
              modifier = Modifier.fillMaxWidth(),
              current = navigationItem,
              getCurrentCode = { getCurrentCode(editorState, editorNavigator) },
              onActionClick = { action, code ->
                when (action) {
                  GlobalAction.Save -> {
                    println("code: $code")
                    FileSystem.SYSTEM.write(editorDirectory.resolve(editorType.filename)) { writeUtf8(code) }
                  }
                  else -> println("TODO: $action")
                }
              },
            )
          },
          bottomBar = {
            AppNavigationBar(
              modifier = Modifier
                .imePadding()
                .fillMaxWidth(),
              selected = navigationItem,
              onItemSelected = { navigationItem = it },
            )
          },
        ) { padding ->
          AppContent(
            modifier = Modifier
              .padding(padding)
              .fillMaxSize(),
            navigationItem = navigationItem,
            editorType = editorType,
            editorState = editorState,
            editorNavigator = editorNavigator,
            onEditorTabChange = { editorType = it },
          )
        }
      }
    }
  }

  override fun onDestroy() {
    JavaScriptRunner.close()
    super.onDestroy()
  }
}

@Composable @ReadOnlyComposable
private fun dynamicThemeScheme(darkTheme: Boolean = isSystemInDarkTheme()): ColorScheme =
  when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> darkColorScheme()
    else -> lightColorScheme()
  }
