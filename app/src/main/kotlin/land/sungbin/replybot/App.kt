/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot

import android.app.Application
import timber.log.Timber

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    // TODO remove this line when release
    Timber.plant(Timber.DebugTree())
  }
}
