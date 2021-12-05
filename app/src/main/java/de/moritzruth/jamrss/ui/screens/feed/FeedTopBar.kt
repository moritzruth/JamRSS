package de.moritzruth.jamrss.ui.screens.feed

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.moritzruth.jamrss.R

@Composable
fun FeedTopBar(elevated: Boolean, navController: NavHostController) {
    val elevation by animateDpAsState(if (elevated) 12.dp else 0.dp)

    Surface(elevation = elevation, color = MaterialTheme.colors.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 18.dp)
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "JamRSS \uD83C\uDF53",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )

            Row {
                IconButton(modifier = Modifier.offset(x = 3.dp), onClick = { navController.navigate("sources?add=true") { launchSingleTop = true } }) {
                    Icon(
                        Icons.Outlined.Add, modifier = Modifier.size(28.dp), contentDescription = stringResource(
                            R.string.button_add_source
                        )
                    )
                }

                var menuExpanded by remember { mutableStateOf(false) }

                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        Icons.Outlined.MoreVert,
                        modifier = Modifier.size(28.dp),
                        contentDescription = stringResource(R.string.button_more)
                    )
                }

                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(onClick = {
                        navController.navigate("sources")
                        menuExpanded = false
                    }) {
                        Text(stringResource(R.string.screen_sources))
                    }

                    DropdownMenuItem(onClick = {
                        navController.navigate("settings")
                        menuExpanded = false
                    }) {
                        Text(stringResource(R.string.screen_settings))
                    }
                }
            }
        }
    }
}