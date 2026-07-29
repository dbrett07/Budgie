package com.example.budgie.core.data

import androidx.room.Dao
import androidx.room.Query
import java.time.Instant

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAll(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE strftime('%m', timestamp, 'unix-epoch') = strftime('%m', :date, 'unix-epoch')")
    fun getTransactionsForMonth(date : Instant)

    @Query("SELECT SUM(amount) FROM transactions WHERE strftime('%m', timestamp, 'unix-epoch') = strftime('%m', :date, 'unix-epoch')")
    fun getSpendForMonth(date : Instant)
}