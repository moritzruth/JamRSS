package de.moritzruth.jamrss.ui.screens.feed

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.data.FeedItem
import de.moritzruth.jamrss.graph
import de.moritzruth.jamrss.ui.util.Settings
import de.moritzruth.jamrss.util.state
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
private fun ItemCard(feedItem: FeedItem, setIsRead: (value: Boolean) -> Unit) {
    val context = LocalContext.current
    val dateFormat = remember {
        android.text.format.DateFormat.getMediumDateFormat(context)
    }

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp
    ) {
        Box(
            Modifier
                .clickable(
                    onClickLabel = stringResource(R.string.button_view_feed_item),
                    onClick = {
                        graph.applicationScope.launch {
                            context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(feedItem.url) })
                            setIsRead(true)
                        }
                    }
                )
                .padding(12.dp)
        ) {
            val sourceName by remember { flow { emit(feedItem.source.await().name) } }.collectAsState(initial = "")

            val color by animateColorAsState(
                MaterialTheme.colors.onBackground.copy(alpha = if (feedItem.isRead) 0.6f else 1f)
            )

            val showButton by graph.settingsDataStore.state(Settings.SHOW_MARK_BUTTON, false)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BoxWithConstraints {
                    Column(Modifier.let { if (showButton) it.width(maxWidth - 50.dp) else it }) {
                        Text(
                            feedItem.title,
                            modifier = Modifier.padding(bottom = 6.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = color,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            feedItem.description,
                            Modifier.height(IntrinsicSize.Min),
                            fontSize = 14.sp,
                            color = color,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text("${dateFormat.format(feedItem.publicationDate)} • ", fontSize = 11.sp, color = color)

                            Text(
                                sourceName,
                                fontSize = 11.sp,
                                color = color,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }

                if (showButton) IconButton(onClick = { setIsRead(!feedItem.isRead) }) {
                    ActionIcon(feedItem.isRead)
                }
            }
        }
    }
}

@Composable
private fun ActionIcon(isRead: Boolean, modifier: Modifier = Modifier) {
    Crossfade(targetState = if (isRead) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.Check) {
        Icon(it, null, modifier)
    }
}

@Composable
fun LazyItemScope.FeedItemCard(feedItem: FeedItem, setIsRead: (value: Boolean) -> Unit) {
    val currentFeedItem by rememberUpdatedState(feedItem)

    val dismissState = rememberDismissState(DismissValue.Default) {
        if (it !== DismissValue.Default) {
            setIsRead(!currentFeedItem.isRead)
        }

        false
    }

    val swipeToMark by graph.settingsDataStore.state(key = Settings.SWIPE_TO_MARK, initialValue = false)

    if (swipeToMark) {
        SwipeToDismiss(
            dismissState,
            Modifier.animateItemPlacement(),
            directions = setOf(DismissDirection.EndToStart),
            background = {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    Arrangement.End,
                    Alignment.CenterVertically
                ) {
                    ActionIcon(
                        isRead = feedItem.isRead,
                        modifier = Modifier
                            .size(26.dp)
                            .scale((dismissState.progress.fraction * 1.2f) + 0.6f)
                    )
                }
            }
        ) {
            ItemCard(feedItem, setIsRead)
        }
    } else {
        ItemCard(feedItem, setIsRead)
    }
}