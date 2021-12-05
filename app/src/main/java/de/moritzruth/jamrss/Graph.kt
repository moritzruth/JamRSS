package de.moritzruth.jamrss

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import de.moritzruth.jamrss.data.Feed
import de.moritzruth.jamrss.data.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient

class Graph private constructor(context: Context) {
    companion object {
        fun create(context: Context) {
            graph = Graph(context)
        }
    }

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val okHttpClient = OkHttpClient()

    val database = Room.databaseBuilder(context, AppDatabase::class.java, "data.db").build()
    val feed = Feed(applicationScope, database)

    val settingsDataStore = PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("settings") }
}

lateinit var graph: Graph
    private set
