package de.moritzruth.jamrss.ui.components.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moritzruth.jamrss.ui.theme.HORIZONTAL_CONTENT_PADDING

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(horizontal = HORIZONTAL_CONTENT_PADDING)
            .padding(top = 24.dp, bottom = 15.dp)
    )

    Divider()
}