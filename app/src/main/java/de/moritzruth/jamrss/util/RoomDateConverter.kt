package de.moritzruth.jamrss.util

import androidx.room.TypeConverter
import java.util.*

class RoomDateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}