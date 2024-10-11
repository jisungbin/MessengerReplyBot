// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import land.sungbin.replybot.R

enum class AppNavigationItem(@StringRes val label: Int, @DrawableRes val icon: Int) {
  Editors(R.string.navigation_editors, R.drawable.ic_round_code_24),
  DebugRoom(R.string.navigation_debug, R.drawable.ic_round_adb_24),
  Log(R.string.navigation_log, R.drawable.ic_round_message_24),
  Settings(R.string.navigation_settings, R.drawable.ic_round_settings_24),
  ;

  companion object {
    val Default = AppNavigationItem.entries.toImmutableList()
  }
}

@Composable fun AppNavigationBar(
  selected: AppNavigationItem,
  modifier: Modifier = Modifier,
  items: ImmutableList<AppNavigationItem> = AppNavigationItem.Default,
  onItemSelected: (item: AppNavigationItem) -> Unit,
) {
  NavigationBar(modifier = modifier) {
    items.forEach { item ->
      NavigationBarItem(
        selected = item == selected,
        label = { Text(text = stringResource(item.label)) },
        icon = {
          Icon(
            painter = painterResource(item.icon),
            contentDescription = stringResource(item.label),
          )
        },
        onClick = { onItemSelected(item) },
      )
    }
  }
}
