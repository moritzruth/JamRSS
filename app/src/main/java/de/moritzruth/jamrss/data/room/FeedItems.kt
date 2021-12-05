package de.moritzruth.jamrss.data.room

import androidx.room.*
import de.moritzruth.jamrss.data.FeedItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedItems {
    @Query("SELECT * FROM feed_items")
    suspend fun getAll(): List<FeedItem>

    @Query("SELECT * FROM feed_items")
    fun observeAll(): Flow<List<FeedItem>>

    @Query("SELECT * FROM feed_items ORDER BY publicationDate DESC")
    fun observeAllOrderedNewestFirst(): Flow<List<FeedItem>>

    @Query("SELECT * FROM feed_items WHERE url = :url")
    suspend fun getByUrl(url: String): FeedItem?

    @Insert
    suspend fun insert(feedItem: FeedItem)

    @Insert
    suspend fun insert(feedItem: List<FeedItem>)

    @Update
    suspend fun update(feedItem: FeedItem): Int

    @Delete
    suspend fun delete(feedItem: FeedItem)

    @Delete
    suspend fun delete(feedItem: List<FeedItem>)

    @Query("UPDATE feed_items SET isRead = 1 WHERE sourceUrl = :sourceUrl")
    suspend fun markAllFromSourceAsRead(sourceUrl: String)
}