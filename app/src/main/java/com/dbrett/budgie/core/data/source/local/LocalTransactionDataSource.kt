package com.dbrett.budgie.core.data.source.local

import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import kotlin.time.Instant

interface LocalTransactionDataSource {
    suspend fun saveTransaction(trans: TransactionEntity)

    suspend fun updateTransactionType(id : String, transactionType: TransactionType)

    suspend fun updateTransaction(trans : TransactionEntity)

    suspend fun deleteTransaction(trans : TransactionEntity)

    fun observeTransactions() : Flow<List<TransactionEntity>>

    fun observeMonthlyAmounts(from: Instant, to: Instant) : Flow<List<BigDecimal>>

    fun getTransactionsPeriodCategory(category : TransactionType, from : Instant, to: Instant) : Flow<List<TransactionEntity>>
}