package com.example.budgie.core.data

import java.time.Instant
import androidx.room.TypeConverter
import java.math.BigDecimal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Converters {
    @TypeConverter
    fun fromTimestamp(value : Long?) : Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun instantToTimestamp(instant : Instant?) : Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun stringToBigDecimal(value : String?) : BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @TypeConverter
    fun bigDecimalToString(value : BigDecimal?) : String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun stringToUUID(value : String?) : Uuid? {
        return value?.let { Uuid.parse(it) }
    }

    @TypeConverter
    fun uuidToString(value : Uuid?) : String? {
        return value.toString()
    }
}