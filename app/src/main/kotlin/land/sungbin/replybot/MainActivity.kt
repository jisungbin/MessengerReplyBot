// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.sebaslogen.resaca.rememberScoped
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import land.sungbin.replybot.components.AceEditorAssetPath
import land.sungbin.replybot.components.AppContent
import land.sungbin.replybot.components.AppNavigationBar
import land.sungbin.replybot.components.AppNavigationItem
import land.sungbin.replybot.components.AppTopBar
import land.sungbin.replybot.components.EditorType
import land.sungbin.replybot.components.GlobalAction
import land.sungbin.replybot.configuration.AppConfiguration
import land.sungbin.replybot.scriptable.ScriptRunner
import land.sungbin.replybot.util.getCurrentCode
import land.sungbin.replybot.util.getEditorDirectory
import land.sungbin.replybot.util.hasNotificationAccessPermission
import land.sungbin.replybot.util.requestNotificationAccessPermission
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
    super.onCreate(savedInstanceState)

    AppConfiguration.start(
      context = applicationContext,
      fs = FileSystem.SYSTEM,
      directory = getDir(SETTINGS_DIRECTORY, MODE_PRIVATE).absolutePath.toPath(),
    )

    if (!hasNotificationAccessPermission()) requestNotificationAccessPermission()

    setContent {
      val (navigationItem, updateNavigationItem) = rememberScoped { mutableStateOf<AppNavigationItem>(AppNavigationItem.Editors) }
      val (editorType, updateEditorType) = rememberScoped { mutableStateOf<EditorType>(EditorType.Main) }

      val editorDirectory = getEditorDirectory()
      val editorState = rememberScoped {
        WebViewState(WebContent.Url(AceEditorAssetPath)).apply {
          webSettings.supportZoom = false
        }
      }
      val editorNavigator = rememberWebViewNavigator()

      LaunchedEffect(Unit) {
        editorState.getCurrentCode(editorNavigator).let(::initializeMain)
      }

      MaterialTheme(colorScheme = dynamicThemeScheme()) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            AppTopBar(
              modifier = Modifier.fillMaxWidth(),
              current = navigationItem,
              getCurrentCode = { editorState.getCurrentCode(editorNavigator) },
              onActionClick = { action, code ->
                when (action) {
                  GlobalAction.Reload -> {
                    editorType.saveCode(code, editorDirectory)
                    initializeMain(code)
                  }
                  GlobalAction.Save -> editorType.saveCode(code, editorDirectory)
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
              onItemSelected = updateNavigationItem,
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
            onEditorTabChange = updateEditorType,
          )
        }
      }
    }
  }

  companion object {
    private const val SETTINGS_DIRECTORY = "settings"
  }
}

private fun EditorType.saveCode(
  code: String,
  editorDirectory: Path,
) {
  // Suppresses code save failures caused by the lifecycle termination.
  @OptIn(DelicateCoroutinesApi::class)
  GlobalScope.launch(Dispatchers.IO) {
    FileSystem.SYSTEM.write(editorDirectory.resolve(filename)) {
      writeUtf8(code)
    }
  }
}

private fun initializeMain(code: String) {
  // The code runs in a Service, so it should follow the Process lifecycle.
  @OptIn(DelicateCoroutinesApi::class)
  GlobalScope.launch(Dispatchers.Default) {
    ScriptRunner.initializeMain(code).onFailure {
      it.printStackTrace()
      println(it)
    }
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
