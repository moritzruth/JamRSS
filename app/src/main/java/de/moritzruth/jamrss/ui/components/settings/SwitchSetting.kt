package de.moritzruth.jamrss.ui.components.settings

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import de.moritzruth.jamrss.graph
import de.moritzruth.jamrss.util.state

@Composable
fun SwitchSetting(text: String, dataStoreKey: Preferences.Key<Boolean>) {
    var value by graph.settingsDataStore.state(dataStoreKey, false)

    Setting(text = text, onClick = { value = !value }) {
        Switch(checked = value, onCheckedChange = { value = it })
    }
}