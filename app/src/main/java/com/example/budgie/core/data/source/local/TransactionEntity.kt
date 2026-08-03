package com.example.budgie.core.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgie.core.data.model.TransactionType
import java.math.BigDecimal
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey val id : String = Uuid.random().toString(),
    val source : String, // E.g. Business name
    val transactionType : TransactionType, // Transaction type, E.g. food, misc, etc
    val amount : BigDecimal,
    val timestamp : Instant, // when the transaction occurred
    val modified : Instant, // when (if) the transaction was last modified
    val syncStatus : SyncStatus = SyncStatus.PENDING
)

enum class SyncStatus {
    PENDING,
    SYNCED,
    FAILED
}
