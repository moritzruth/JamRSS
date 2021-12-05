package de.moritzruth.jamrss.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.moritzruth.jamrss.ui.theme.CONTENT_PADDING
import de.moritzruth.jamrss.ui.theme.HORIZONTAL_CONTENT_PADDING
import de.moritzruth.jamrss.ui.theme.VERTICAL_CONTENT_PADDING
import de.moritzruth.jamrss.ui.util.RandomPlaceholderText

@Composable
fun FeedListSkeleton() {
    Column(
        modifier = Modifier.padding(CONTENT_PADDING),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(6) {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                elevation = 2.dp
            ) {
                Column(Modifier.padding(vertical = 12.dp, horizontal = 14.dp)) {
                    RandomPlaceholderText(Modifier.padding(bottom = 5.dp), 10, 100)
                    RandomPlaceholderText(minLength = 25, maxLength = 100)
                }
            }
        }
    }
}