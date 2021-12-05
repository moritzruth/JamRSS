package de.moritzruth.jamrss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.moritzruth.jamrss.ui.screens.feed.FeedScreen
import de.moritzruth.jamrss.ui.screens.licenses.LicensesScreen
import de.moritzruth.jamrss.ui.screens.settings.SettingsScreen
import de.moritzruth.jamrss.ui.screens.sources.SourcesScreen
import de.moritzruth.jamrss.ui.theme.JamRSSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JamRSSTheme {
                val navController = rememberNavController()

                NavHost(navController, "feed") {
                    composable("feed") { FeedScreen(navController) }

                    composable(
                        "sources?add={add}",
                        listOf(navArgument("add") { defaultValue = false })
                    ) { entry -> SourcesScreen(navController, entry.arguments?.getBoolean("add") ?: false) }

                    composable("settings") { SettingsScreen(navController) }

                    composable("licenses") { LicensesScreen(navController) }
                }
            }
        }
    }
}
