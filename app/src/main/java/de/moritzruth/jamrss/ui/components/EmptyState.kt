package de.moritzruth.jamrss.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moritzruth.jamrss.ui.theme.HORIZONTAL_CONTENT_PADDING

@Composable
fun EmptyState(text: String) {
    Text(
        text,
        Modifier
            .padding(horizontal = HORIZONTAL_CONTENT_PADDING)
            .padding(top = 30.dp, start = 10.dp),
        fontSize = 20.sp
    )
}