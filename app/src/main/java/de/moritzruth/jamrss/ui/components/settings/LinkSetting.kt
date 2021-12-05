package de.moritzruth.jamrss.ui.components.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import de.moritzruth.jamrss.ui.util.OpenInNew

@Composable
fun LinkSetting(text: String, url: String) {
    val context = LocalContext.current

    Setting(
        text = text,
        onClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW).also { it.data = Uri.parse(url) }
            )
        },
        onClickLabel = text,
        role = Role.Button
    ) {
        Icon(Icons.Outlined.OpenInNew, null, Modifier.alpha(0.8f))
    }
}