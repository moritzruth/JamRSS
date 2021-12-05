package de.moritzruth.jamrss.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import de.moritzruth.jamrss.graph
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import java.util.*

@Entity(
    tableName = "feed_items",
    foreignKeys = [
        ForeignKey(
            entity = FeedSource::class,
            parentColumns = arrayOf("url"),
            childColumns = arrayOf("sourceUrl"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("sourceUrl"),
        Index("publicationDate")
    ]
)
data class FeedItem(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String,
    val publicationDate: Date,
    val isRead: Boolean,
    val sourceUrl: String
) {
    @Transient
    val source = graph.applicationScope.async(start = CoroutineStart.LAZY) { graph.database.feedSources().getByUrl(sourceUrl)!! }
}