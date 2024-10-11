// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.service

import android.service.notification.StatusBarNotification
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import land.sungbin.replybot.configuration.AppConfiguration
import land.sungbin.replybot.scriptable.ScriptRunner
import timber.log.Timber

class ReplyableNotificationListener : LifecycleNotificationListenerService() {
  override fun onListenerConnected() {
    Timber.tag(TAG).i("Connected.")
    super.onListenerConnected()
  }

  override fun onListenerDisconnected() {
    Timber.tag(TAG).i("Disconnected.")
    super.onListenerDisconnected()
  }

  override fun onNotificationPosted(sbn: StatusBarNotification) {
    Timber.tag(TAG).i("[onNotificationPosted] %s", sbn)

    if (AppConfiguration.engines.isEmpty())
      Timber.tag(TAG).w("No engines.")

    val messages = AppConfiguration.engines.mapNotNull { engine ->
      engine.createMessage(sbn).also { message ->
        Timber.tag(TAG).i("[createMessage] %s", message)
      }
    }

    lifecycleScope.launch(Dispatchers.Default) {
      messages.fastForEach { message ->
        launch {
          ScriptRunner.invokeOnNewMessage(message).onFailure {
            it.printStackTrace()
            Timber.tag(TAG).e(it, "[invokeOnNewMessage] %s", message)
          }
        }
      }
    }
  }

  companion object {
    private val TAG = ReplyableNotificationListener::class.simpleName!!
  }
}
