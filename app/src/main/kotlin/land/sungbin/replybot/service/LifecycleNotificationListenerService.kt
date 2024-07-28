/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.service

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher

abstract class LifecycleNotificationListenerService : NotificationListenerService(), LifecycleOwner {
  private val dispatcher by lazy { ServiceLifecycleDispatcher(this) }
  override val lifecycle get() = dispatcher.lifecycle

  @CallSuper override fun onListenerConnected() {
    dispatcher.onServicePreSuperOnCreate()
    super.onListenerConnected()
  }

  @CallSuper override fun onListenerDisconnected() {
    dispatcher.onServicePreSuperOnDestroy()
    super.onListenerDisconnected()
  }

  @CallSuper override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    dispatcher.onServicePreSuperOnStart()
    return super.onStartCommand(intent, flags, startId)
  }

  @CallSuper override fun onBind(intent: Intent?): IBinder? {
    dispatcher.onServicePreSuperOnBind()
    return super.onBind(intent)
  }
}
