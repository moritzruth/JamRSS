package de.moritzruth.jamrss.ui.screens.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.data.FeedItem
import de.moritzruth.jamrss.ui.theme.CONTENT_PADDING

@Composable
fun FeedList(
    itemsPair: Pair<List<FeedItem>, List<FeedItem>>,
    stopNextScroll: Boolean,
    listState: LazyListState,
    oldFirstVisibleItem: Pair<Int, Int>,
    setIsRead: (index: Int, feedItem: FeedItem, value: Boolean) -> Unit
) {
    val (readItems, unreadItems) = itemsPair

    LaunchedEffect(itemsPair) {
        // Prevents the list from scrolling when isRead is changed
        if (stopNextScroll) listState.scrollToItem(oldFirstVisibleItem.first, oldFirstVisibleItem.second)
    }

    LazyColumn(
        state = listState,
        contentPadding = CONTENT_PADDING,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(unreadItems, { _, item -> item.url }) { index, item ->
            val currentIndex by rememberUpdatedState(index)

            FeedItemCard(item, setIsRead = { value ->
                setIsRead(currentIndex, item, value)
            })
        }

        stickyHeader {
            AnimatedVisibility(
                visible = readItems.isNotEmpty(),
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                val isScrolledPast by derivedStateOf {
                    val index = unreadItems.size
                    listState.firstVisibleItemIndex > index || (listState.firstVisibleItemIndex == index && listState.firstVisibleItemScrollOffset > 35)
                }

                val floatProgress by animateFloatAsState(if (isScrolledPast) 1f else 0f, tween(400))

                Surface(
                    color = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(15.dp * floatProgress),
                    modifier = Modifier.offset(y = 12.dp * floatProgress),
                    elevation = 8.dp * floatProgress
                ) {
                    val horizontalPadding by animateDpAsState(if (isScrolledPast) 15.dp else 8.dp, tween(400))
                    val verticalPadding by animateDpAsState(if (isScrolledPast) 10.dp else 12.dp, tween(400))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontalPadding, verticalPadding)
                            .clip(RoundedCornerShape(5.dp)),
                        Arrangement.spacedBy(15.dp),
                        Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.heading_marked_as_read), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Divider()
                    }
                }
            }
        }

        itemsIndexed(readItems, { _, it -> it.url }) { index, item ->
            val currentIndex by rememberUpdatedState(index)
            val currentItem by rememberUpdatedState(item)

            FeedItemCard(item, setIsRead = { value ->
                setIsRead(currentIndex + unreadItems.size, currentItem, value)
            })
        }
    }
}