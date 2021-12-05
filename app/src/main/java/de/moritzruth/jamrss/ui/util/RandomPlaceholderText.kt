package de.moritzruth.jamrss.ui.util

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import kotlin.random.Random

@Composable
fun RandomPlaceholderText(modifier: Modifier = Modifier, minLength: Int = 10, maxLength: Int = 100) {
    Text(
        " ".repeat(Random.nextInt(minLength, maxLength)),
        modifier = modifier.placeholder(true, highlight = PlaceholderHighlight.fade())
    )
}