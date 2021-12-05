package de.moritzruth.jamrss.ui.screens.feed

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.data.FeedItem
import de.moritzruth.jamrss.graph
import de.moritzruth.jamrss.ui.components.EmptyState
import de.moritzruth.jamrss.ui.theme.CONTENT_PADDING
import de.moritzruth.jamrss.ui.util.Undo
import de.moritzruth.jamrss.ui.util.checkIsOnline
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(navController: NavHostController) {
    val listState = rememberLazyListState()
    var stopNextScroll by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    val elevatedTopBar by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset != 0 }
    }

    var oldFirstVisibleItem by remember {
        mutableStateOf(Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset))
    }

    var snackbarItem by remember { mutableStateOf<Pair<Boolean, FeedItem>?>(null) }

    LaunchedEffect(snackbarItem) {
        if (snackbarItem != null) {
            delay(5000)
            snackbarItem = null
        }
    }

    fun setIsReadAndFixScroll(index: Int, feedItem: FeedItem, isRead: Boolean) {
        stopNextScroll = index == listState.firstVisibleItemIndex
        oldFirstVisibleItem = Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)

        graph.applicationScope.launch { graph.feed.setItemRead(feedItem.url, isRead) }
    }

    fun setIsReadAndShowSnackbar(index: Int, feedItem: FeedItem, isRead: Boolean) {
        if (feedItem.isRead == isRead) return
        setIsReadAndFixScroll(index, feedItem, isRead)
        snackbarItem = Pair(isRead, feedItem)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { FeedTopBar(elevatedTopBar, navController) }
    ) { padding ->
        Box(Modifier.padding(padding), contentAlignment = Alignment.BottomCenter) {
            val isRefreshing by graph.feed.fetchingFeeds.collectAsState()
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
            var offlineDialogActive by remember { mutableStateOf(false) }

            val itemsPair by remember {
                graph.feed.observeFeedItemsOrderedNewestFirst().map { items ->
                    items.filter { it.source.await().isEnabled }.partition { it.isRead }
                }
            }.collectAsState(initial = null)

            val sources by remember {
                graph.feed.observeFeedSources()
            }.collectAsState(initial = null)

            SwipeRefresh(
                swipeRefreshState,
                onRefresh = {
                    graph.applicationScope.launch {
                        if (checkIsOnline()) graph.feed.fetchFeeds()
                        else offlineDialogActive = true
                    }
                },
                Modifier.fillMaxSize()
            ) {
                Crossfade(
                    itemsPair == null || sources == null,
                    animationSpec = tween(600, if (itemsPair == null) 0 else 200)
                ) { isLoading ->
                    if (isLoading) FeedListSkeleton()
                    else {
                        if (itemsPair!!.first.size + itemsPair!!.second.size == 0) {
                            val message = when {
                                sources!!.isEmpty() -> stringResource(R.string.message_no_sources)
                                sources!!.none { it.isEnabled } -> stringResource(R.string.message_all_sources_disabled)
                                else -> stringResource(R.string.message_no_feed_entries)
                            }

                            EmptyState(message)
                        } else FeedList(
                            itemsPair!!,
                            stopNextScroll,
                            listState,
                            oldFirstVisibleItem,
                            ::setIsReadAndShowSnackbar
                        )
                    }
                }
            }

            AnimatedContent(
                targetState = snackbarItem,
                transitionSpec = {
                    (slideInVertically(tween()) + fadeIn(tween())) with
                            (fadeOut(tween(500)))
                }
            ) { state ->
                if (state != null) {
                    val (currentIsRead, feedItem) = state

                    Snackbar(
                        Modifier.padding(CONTENT_PADDING),
                        action = {
                            IconButton(onClick = {
                                val index =
                                    if (currentIsRead) itemsPair!!.first.indexOfFirst { it.url == feedItem.url } + itemsPair!!.second.size
                                    else itemsPair!!.second.indexOfFirst { it.url == feedItem.url }

                                setIsReadAndFixScroll(index, feedItem, !currentIsRead)
                                snackbarItem = null
                            }) {
                                Icon(
                                    Icons.Outlined.Undo,
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = stringResource(R.string.button_undo)
                                )
                            }
                        }
                    ) {
                        if (currentIsRead) Text(stringResource(R.string.message_marked_as_read))
                        else Text(stringResource(R.string.message_marked_as_unread))
                    }
                }
            }

            if (offlineDialogActive) {
                AlertDialog(
                    onDismissRequest = { offlineDialogActive = false },
                    confirmButton = {
                        TextButton(onClick = { offlineDialogActive = false }) {
                            Text(stringResource(R.string.button_okay))
                        }
                    },
                    title = { Text(stringResource(R.string.message_offline)) },
                    text = { Text(stringResource(R.string.message_refresh_needs_online)) }
                )
            }
        }
    }
}