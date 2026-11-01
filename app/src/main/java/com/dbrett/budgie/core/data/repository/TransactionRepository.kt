package com.dbrett.budgie.core.data.repository

import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import kotlin.time.Instant

interface TransactionRepository {

    fun observeTransactions() : Flow<List<Transaction>>

    // Sync transactions received from the network so they can be written to the local database.
    // Also, push unsynced local transactions to the backend.
    suspend fun syncTransactions() { }

    suspend fun saveTransaction(trans : Transaction)

    suspend fun updateTransactionType(id : String, newType : TransactionType)

    suspend fun updateTransaction(trans : Transaction)

    suspend fun deleteTransaction(trans : Transaction)
}