package de.moritzruth.jamrss.data.room

import androidx.room.*
import de.moritzruth.jamrss.data.FeedSource
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedSources {
    @Query("SELECT * FROM feed_sources")
    fun observeAll(): Flow<List<FeedSource>>

    @Query("SELECT * FROM feed_sources")
    suspend fun getAll(): List<FeedSource>

    @Query("SELECT * FROM feed_sources WHERE url = :url")
    suspend fun getByUrl(url: String): FeedSource?

    @Insert
    suspend fun insert(feedSource: FeedSource)

    @Update
    suspend fun update(feedSource: FeedSource)

    @Delete
    suspend fun delete(feedSource: FeedSource)
}