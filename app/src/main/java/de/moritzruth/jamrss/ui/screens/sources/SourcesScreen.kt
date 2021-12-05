package de.moritzruth.jamrss.ui.screens.sources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.data.FeedSource
import de.moritzruth.jamrss.graph
import de.moritzruth.jamrss.ui.components.EmptyState
import de.moritzruth.jamrss.ui.components.JamTopBar
import de.moritzruth.jamrss.ui.theme.CONTENT_PADDING

@Composable
fun SourcesScreen(navController: NavHostController, addDialogActive: Boolean) {
    fun showAddDialog() = navController.navigate("sources?add=true") { launchSingleTop = true }
    fun hideAddDialog() = navController.navigate("sources") { launchSingleTop = true }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            JamTopBar(navController, stringResource(R.string.screen_sources), listState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog() }, backgroundColor = MaterialTheme.colors.primary) {
                Icon(Icons.Outlined.Add, contentDescription = stringResource(R.string.button_add_source))
            }
        }
    ) { paddingValues ->
        val sources by remember { graph.feed.observeFeedSources() }.collectAsState(initial = emptyList())

        if (addDialogActive) AddSourceDialog(
            onDismissRequest = ::hideAddDialog,
            onCreated = ::hideAddDialog
        )

        Box(Modifier.padding(paddingValues)) {
            if (sources.isEmpty()) EmptyState(stringResource(R.string.message_no_sources))

            LazyColumn(
                state = listState,
                contentPadding = CONTENT_PADDING,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sources, FeedSource::url) {
                    FeedSourceCard(it)
                }
            }
        }
    }
}
