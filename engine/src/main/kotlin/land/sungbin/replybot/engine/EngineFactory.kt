// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.engine

import android.service.notification.StatusBarNotification

public abstract class EngineFactory {
  /** [StatusBarNotification]을 생성한 앱의 패키지명 */
  public abstract val identifier: String

  public abstract fun createMessage(sbn: StatusBarNotification): Message?
}
