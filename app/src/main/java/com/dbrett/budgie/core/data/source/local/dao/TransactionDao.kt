package com.dbrett.budgie.core.data.source.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.dbrett.budgie.core.data.source.local.entity.TransactionCategoryUpdate
import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import kotlin.time.Instant

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE isDeletedLocally = 0 ORDER BY timestamp DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :from AND :to AND isDeletedLocally = 0")
    fun getTransactionsForMonth(from: Instant, to: Instant) : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionType = :category AND timestamp BETWEEN :from AND :to AND isDeletedLocally = 0")
    fun getTransactionsForPeriodCategory(category : TransactionType, from : Instant, to : Instant) : Flow<List<TransactionEntity>>

    @Query("SELECT amount FROM transactions WHERE timestamp BETWEEN :from AND :to")
    fun observeMonthlyAmounts(from: Instant, to: Instant) : Flow<List<BigDecimal>>

    @Insert
    suspend fun insertTransaction(trans : TransactionEntity)

    @Delete
    suspend fun deleteTransaction(trans : TransactionEntity)

    @Update(entity = TransactionEntity::class)
    suspend fun updateCategory(vararg category: TransactionCategoryUpdate)

    @Update
    suspend fun updateTransaction(transaction : TransactionEntity)
}