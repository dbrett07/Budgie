package com.example.budgie.core.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.Instant

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE strftime('%m', timestamp, 'unix-epoch') = strftime('%m', :date, 'unix-epoch')")
    suspend fun getTransactionsForMonth(date : Instant) : List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM transactions WHERE strftime('%m', timestamp, 'unix-epoch') = strftime('%m', :date, 'unix-epoch')")
    suspend fun getSpendForMonth(date : Instant) : BigDecimal

    @Insert
    suspend fun insertTransaction(trans : TransactionEntity)

    @Delete
    suspend fun deleteTransaction(trans : TransactionEntity)

    @Update(entity = TransactionEntity::class)
    suspend fun updateCategory(vararg category: TransactionCategoryUpdate)
}