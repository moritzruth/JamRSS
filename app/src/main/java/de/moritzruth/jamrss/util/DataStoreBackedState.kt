package de.moritzruth.jamrss.util

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import de.moritzruth.jamrss.graph
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStoreBackedState<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val state: State<T?>,
    private val initialValue: T
): MutableState<T> {
    override var value: T
        get() = state.value!!
        set(value) {
            graph.applicationScope.launch {
                dataStore.edit {
                    it[key] = value
                }
            }
        }

    override fun component1() = value
    override fun component2(): (T) -> Unit = { value = it }
}

@Composable
@Suppress("NOTHING_TO_INLINE")
inline fun <T> DataStore<Preferences>.state(key: Preferences.Key<T>, initialValue: T): DataStoreBackedState<T> {
    val state = remember(key) { data.map { it[key] } }.collectAsState(initial = initialValue)

    return remember(key) {
        DataStoreBackedState(this, key, state, initialValue)
    }
}
