package de.moritzruth.jamrss.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moritzruth.jamrss.ui.theme.HORIZONTAL_CONTENT_PADDING

@Composable
fun Setting(
    text: String,
    onClick: () -> Unit,
    onClickLabel: String? = null,
    role: Role? = null,
    enabled: Boolean = true,
    content: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(
                enabled,
                onClickLabel,
                role,
                onClick
            )
            .padding(horizontal = HORIZONTAL_CONTENT_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 15.sp)
        content?.let {
            Box(Modifier.width(40.dp), contentAlignment = Alignment.Center) {
                it()
            }
        }
    }

    Divider()
}