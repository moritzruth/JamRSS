package de.moritzruth.jamrss

import android.app.Application
import androidx.datastore.preferences.core.edit
import de.moritzruth.jamrss.ui.util.Settings
import de.moritzruth.jamrss.ui.util.checkIsOnline
import kotlinx.coroutines.launch

class JamRssApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.create(this)

        // Initial load
        graph.applicationScope.launch {
            graph.settingsDataStore.edit {
                if (!it.contains(Settings.SHOW_MARK_BUTTON)) it[Settings.SHOW_MARK_BUTTON] = false
                if (!it.contains(Settings.SWIPE_TO_MARK)) it[Settings.SWIPE_TO_MARK] = true
            }
        }

        graph.applicationScope.launch {
            if (graph.feed.getFeedItems().isEmpty() && checkIsOnline()) graph.feed.fetchFeeds()
        }
    }
}