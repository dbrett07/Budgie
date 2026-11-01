package com.dbrett.budgie.core.data.source.local

import androidx.room3.ColumnTypeConverter
import java.math.BigDecimal
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Converters {
    @ColumnTypeConverter
    fun fromTimestamp(value : Long?) : Instant? {
        return value?.let { Instant.fromEpochSeconds(it) }
    }

    @ColumnTypeConverter
    fun instantToTimestamp(instant : Instant?) : Long? {
        return instant?.epochSeconds
    }

    @ColumnTypeConverter
    fun stringToBigDecimal(value : String?) : BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @ColumnTypeConverter
    fun bigDecimalToString(value : BigDecimal?) : String? {
        return value?.toPlainString()
    }

    @ColumnTypeConverter
    fun stringToUUID(value : String?) : Uuid? {
        return value?.let { Uuid.Companion.parse(it) }
    }

    @ColumnTypeConverter
    fun uuidToString(value : Uuid?) : String? {
        return value.toString()
    }

}