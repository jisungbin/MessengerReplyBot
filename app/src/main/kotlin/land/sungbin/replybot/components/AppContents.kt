// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.replybot.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable fun AppContent(
  navigationItem: AppNavigationItem,
  modifier: Modifier = Modifier,
) {
  when (navigationItem) {
    AppNavigationItem.Dashboard -> {
      EditorsContent(
        modifier = modifier,
        current = editorType,
        state = editorState,
        navigator = editorNavigator,
        nativeView = nativeEditor,
        onTabClick = onEditorTabChange,
      )
    }
    AppNavigationItem.DebugRoom -> {
      DebugRoomContent(modifier = modifier)
    }
    AppNavigationItem.Log -> {
      LogContent(modifier = modifier)
    }
    AppNavigationItem.Settings -> {
      SettingsContent(modifier = modifier)
    }
  }
}
