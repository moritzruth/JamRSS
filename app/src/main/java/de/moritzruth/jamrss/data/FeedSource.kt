package de.moritzruth.jamrss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_sources")
data class FeedSource(
    @PrimaryKey
    val url: String,
    val name: String,
    val isEnabled: Boolean
)