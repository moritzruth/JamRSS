package de.moritzruth.jamrss.ui.util

import androidx.datastore.preferences.core.booleanPreferencesKey

object Settings {
    val SHOW_MARK_BUTTON = booleanPreferencesKey("show_mark_button")
    val SWIPE_TO_MARK = booleanPreferencesKey("swipe_to_mark")
}