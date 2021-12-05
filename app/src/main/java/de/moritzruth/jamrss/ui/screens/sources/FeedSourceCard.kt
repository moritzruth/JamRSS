package de.moritzruth.jamrss.ui.screens.sources

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.data.FeedSource
import de.moritzruth.jamrss.graph
import kotlinx.coroutines.launch

@Composable
fun FeedSourceCard(source: FeedSource) {
    val scope = rememberCoroutineScope()

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    modifier = Modifier.padding(end = 8.dp),
                    checked = source.isEnabled,
                    onCheckedChange = {
                        graph.applicationScope.launch { graph.feed.setSourceEnabled(source.url, !source.isEnabled) }
                    }
                )

                BoxWithConstraints {
                    Column(
                        Modifier
                            .padding(end = 10.dp)
                            // This is required because otherwise the remove button is pushed offscreen by large URLs or titles
                            .width(this@BoxWithConstraints.maxWidth - 40.dp)
                    ) {
                        Text(
                            source.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            source.url,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = {
                    scope.launch {
                        graph.feed.removeSource(source)
                    }
                }, Modifier.size(25.dp)) {
                    Icon(Icons.Outlined.Delete, stringResource(R.string.button_remove_source))
                }
            }
        }
    }
}