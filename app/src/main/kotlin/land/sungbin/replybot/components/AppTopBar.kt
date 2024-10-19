// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.components

import androidx.annotation.DrawableRes
import androidx.annotation.WorkerThread
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import land.sungbin.replybot.R

enum class GlobalAction(
  @DrawableRes val icon: Int,
  val requiresCode: Boolean = false,
  val availableOn: Set<AppNavigationItem>,
) {
  Reload(
    R.drawable.ic_round_refresh_24,
    requiresCode = true,
    availableOn = setOf(AppNavigationItem.Editors),
  ),
  Save(
    R.drawable.ic_round_save_24,
    requiresCode = true,
    availableOn = setOf(AppNavigationItem.Editors),
  ),
  GitPush(
    R.drawable.ic_round_cloud_upload_24,
    requiresCode = true,
    availableOn = setOf(AppNavigationItem.Editors),
  ),
  GitPull(
    R.drawable.ic_round_cloud_download_24,
    availableOn = setOf(AppNavigationItem.Editors),
  ),
}

@Composable fun AppTopBar(
  current: AppNavigationItem,
  @WorkerThread getCurrentCode: suspend () -> String,
  @WorkerThread onActionClick: (action: GlobalAction, code: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  TopAppBar(
    modifier = modifier,
    title = { Text(text = stringResource(current.label)) },
    actions = {
      GlobalAction.entries.forEach action@{ action ->
        if (current !in action.availableOn) return@action
        IconButton(
          onClick = {
            scope.launch {
              onActionClick(action, if (action.requiresCode) getCurrentCode() else "")
            }
          },
        ) {
          Icon(
            painter = painterResource(action.icon),
            contentDescription = GlobalAction::class.simpleName!! + "." + action.name,
          )
        }
      }
    },
  )
}
