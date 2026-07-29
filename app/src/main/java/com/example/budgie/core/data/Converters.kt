package com.example.budgie.core.data

import java.time.Instant
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromTimestamp(value : Long?) : Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun instantToTimestamp(instant : Instant?) : Long? {
        return instant?.toEpochMilli()
    }
}