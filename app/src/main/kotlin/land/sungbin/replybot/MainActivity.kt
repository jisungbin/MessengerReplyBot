/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import land.sungbin.replybot.components.ReplierMainScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { TODO() },
      ) { padding ->
        ReplierMainScreen(modifier = Modifier.padding(padding))
      }
    }
  }
}
