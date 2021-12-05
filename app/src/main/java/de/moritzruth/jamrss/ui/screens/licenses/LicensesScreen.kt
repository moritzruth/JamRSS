package de.moritzruth.jamrss.ui.screens.licenses

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.ui.components.JamTopBar

@Composable
fun LicensesScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            JamTopBar(navController, stringResource(R.string.screen_licenses), scrollState)
        }
    ) {
        LibrariesContainer()
    }
}