package de.moritzruth.jamrss.data

import android.text.Html
import androidx.room.withTransaction
import de.moritzruth.jamrss.data.room.AppDatabase
import de.moritzruth.jamrss.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val DESCRIPTION_MAX_LENGTH = 400

class Feed(private val applicationScope: CoroutineScope, private val database: AppDatabase) {
    fun observeFeedSources() = database.feedSources().observeAll()

    suspend fun addSource(name: String, url: String, shouldMarkExistingItemsAsRead: Boolean) = database.withTransaction {
        if (database.feedSources().getByUrl(url) == null) {
            database.feedSources().insert(FeedSource(
                name = name,
                url = url,
                isEnabled = true
            ))

            applicationScope.launch {
                fetchFeeds()
                if (shouldMarkExistingItemsAsRead) database.feedItems().markAllFromSourceAsRead(url)
            }
        }
    }

    suspend fun setSourceEnabled(url: String, isEnabled: Boolean) = database.withTransaction {
        val instance = database.feedSources().getByUrl(url) ?: return@withTransaction
        database.feedSources().update(instance.copy(isEnabled = isEnabled))
    }

    suspend fun removeSource(feedSource: FeedSource) {
        database.feedSources().delete(feedSource)
    }

    suspend fun getFeedItems() = database.feedItems().getAll()
    fun observeFeedItemsOrderedNewestFirst() = database.feedItems().observeAllOrderedNewestFirst()

    suspend fun setItemRead(url: String, isRead: Boolean) = database.withTransaction {
        val item = database.feedItems().getByUrl(url) ?: return@withTransaction
        if (item.isRead != isRead) database.feedItems().update(item.copy(isRead = isRead))
    }

    private val _fetchingFeeds = MutableStateFlow(false)
    val fetchingFeeds = _fetchingFeeds.asStateFlow()

    /**
     * The caller should ensure that there is a connection to the Internet.
     */
    suspend fun fetchFeeds() = database.withTransaction {
        _fetchingFeeds.value = true
        val sources = database.feedSources().getAll()
        val oldItems = database.feedItems().getAll()

        val newItems = sources.flatMap { source ->
            val result = fetchAndParseFeed(source.url)
            if (result !is FetchAndParseResult.Success) return@flatMap emptySequence() // TODO: Show an error message

            result.feed.entries.asSequence().filter { it.link != null }.map { entry ->
                val url = entry.link.trim()
                val oldItem = oldItems.find { it.url == url }

                val fullDescription = Html.fromHtml(entry.description.value, Html.FROM_HTML_MODE_COMPACT).toString()
                val description = truncateWithEllipsis(
                    DESCRIPTION_MAX_LENGTH,
                    rewriteWithoutLineBreaks(collapseMultipleLineBreaks(fullDescription).trim())
                )

                FeedItem(
                    title = entry.title.trim(),
                    url = url,
                    description = description,
                    publicationDate = entry.publishedDate,
                    isRead = oldItem?.isRead ?: false,
                    sourceUrl = source.url
                )
            }
        }

        database.feedItems().delete(oldItems)
        database.feedItems().insert(newItems)
        _fetchingFeeds.value = false
    }
}