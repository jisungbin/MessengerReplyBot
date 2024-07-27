/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import android.service.notification.StatusBarNotification

public abstract class EngineFactory {
  /** Notification을 생성한 앱의 패키지명 */
  public abstract val identifier: String
  public abstract fun isDeletedMessageSupported(): Boolean

  public abstract fun createNormalMessage(sbn: StatusBarNotification): Message.Normal?
  public abstract fun createDeletedMessage(sbn: StatusBarNotification): Message.Deleted?
}
