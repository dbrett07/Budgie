package com.dbrett.budgie.core.data.source.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import java.math.BigDecimal
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey
    val id : String,
    val title : String,
    val source : String, // E.g. Business name
    val transactionType : TransactionType, // Transaction type, E.g. food, misc, etc
    val amount : BigDecimal,
    val timestamp : Instant, // when the transaction occurred

    val modified : Instant, // when (if) the transaction was last modified
    var syncStatus : SyncStatus = SyncStatus.PENDING,
    var isDeletedLocally : Boolean = false
) {
    fun hasParseError() : Boolean {
        return amount.intValueExact() == -1
    }

    fun toDomainModel() : Transaction {
        return Transaction(this.id, this.title,  this.source, this.transactionType, this.amount, this.timestamp)
    }

    companion object {
        fun fromDomainModel(transaction: Transaction) : TransactionEntity {
            return TransactionEntity(
                id = transaction.id,
                title = transaction.title,
                source = transaction.source,
                transactionType = transaction.transactionType,
                amount = transaction.amount,
                timestamp = transaction.timestamp,

                modified = Clock.System.now(),
                syncStatus = SyncStatus.PENDING,
                isDeletedLocally = false
            )
        }
    }
}

enum class SyncStatus {
    PENDING,
    SYNCED,
    FAILED
}
