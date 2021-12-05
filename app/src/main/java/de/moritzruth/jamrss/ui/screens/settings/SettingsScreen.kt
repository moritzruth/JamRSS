package de.moritzruth.jamrss.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.moritzruth.jamrss.BuildConfig
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.ui.components.JamTopBar
import de.moritzruth.jamrss.ui.components.settings.LinkSetting
import de.moritzruth.jamrss.ui.components.settings.Setting
import de.moritzruth.jamrss.ui.components.settings.SettingsSectionTitle
import de.moritzruth.jamrss.ui.components.settings.SwitchSetting
import de.moritzruth.jamrss.ui.theme.HORIZONTAL_CONTENT_PADDING
import de.moritzruth.jamrss.ui.util.Settings

// Settings:
// - Number of title/description lines
@Composable
fun SettingsScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            JamTopBar(navController, stringResource(R.string.screen_settings), scrollState)
        }
    ) {
        Column(
            Modifier.verticalScroll(scrollState)
        ) {
            SwitchSetting(
                text = stringResource(R.string.settings_show_mark_button),
                dataStoreKey = Settings.SHOW_MARK_BUTTON
            )

            SwitchSetting(
                text = stringResource(R.string.settings_swipe_to_mark),
                dataStoreKey = Settings.SWIPE_TO_MARK
            )

            SettingsSectionTitle("Other")

            LinkSetting(text = stringResource(R.string.settings_donate), url = "https://ko-fi.com/moritzruth")
            LinkSetting(text = stringResource(R.string.settings_view_source_code), url = "https://github.com/moritzruth/JamRSS")
            LinkSetting(text = stringResource(R.string.settings_report_issue), url = "https://moritzruth.de/contact")

            Setting(text = stringResource(R.string.settings_show_licenses), onClick = {
                navController.navigate("licenses")
            })

            Text(
                stringResource(R.string.message_creator, BuildConfig.VERSION_NAME) + "\n" + stringResource(R.string.message_license),
                modifier = Modifier
                    .padding(horizontal = HORIZONTAL_CONTENT_PADDING)
                    .padding(top = 20.dp),
                fontSize = 13.sp,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
            )
        }
    }
}
