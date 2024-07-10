/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.components

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class ReplierNavigationItem {
  Home,
}

@Composable fun ReplierNavigationBar(
  selected: ReplierNavigationItem,
  items: ImmutableList<ReplierNavigationItem> = ReplierNavigationItem.entries.toImmutableList(),
  modifier: Modifier = Modifier,
  onItemSelected: (item: ReplierNavigationItem) -> Unit,
) {
  NavigationBar(modifier = modifier) { TODO() }
}
