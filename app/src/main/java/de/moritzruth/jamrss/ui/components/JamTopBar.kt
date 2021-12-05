package de.moritzruth.jamrss.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.ui.theme.HORIZONTAL_CONTENT_PADDING
import de.moritzruth.jamrss.ui.util.isLazyListScrolled

@Composable
fun JamTopBar(navController: NavController, text: String, scrollState: ScrollState) {
    val elevatedTopBar by remember {
        derivedStateOf { scrollState.value != 0 }
    }

    JamTopBar(navController, text, elevatedTopBar)
}

@Composable
fun JamTopBar(navController: NavController, text: String, lazyListState: LazyListState) {
    val elevatedTopBar by remember {
        derivedStateOf { isLazyListScrolled(lazyListState) }
    }

    JamTopBar(navController, text, elevatedTopBar)
}

@Composable
fun JamTopBar(navController: NavController, text: String, elevated: Boolean) {
    val elevation by animateDpAsState(if (elevated) 16.dp else 0.dp)

    Surface(elevation = elevation, color = MaterialTheme.colors.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 10.dp, bottom = 12.dp, end = HORIZONTAL_CONTENT_PADDING),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton({ navController.popBackStack() }, Modifier.padding(end = 5.dp)) {
                Icon(Icons.Outlined.ArrowBack, stringResource(R.string.button_back))
            }

            Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}