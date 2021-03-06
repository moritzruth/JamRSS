package de.moritzruth.jamrss.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.moritzruth.jamrss.data.FeedItem
import de.moritzruth.jamrss.data.FeedSource
import de.moritzruth.jamrss.util.RoomDateConverter

@Database(
    entities = [FeedSource::class, FeedItem::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(RoomDateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun feedSources(): FeedSources
    abstract fun feedItems(): FeedItems
}