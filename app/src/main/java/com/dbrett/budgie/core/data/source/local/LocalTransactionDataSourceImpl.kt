package com.dbrett.budgie.core.data.source.local

import com.dbrett.budgie.core.data.source.local.dao.TransactionDao
import com.dbrett.budgie.core.data.source.local.entity.TransactionCategoryUpdate
import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity
import com.dbrett.budgie.core.model.TransactionType
import kotlin.time.Instant

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Clock

class LocalTransactionDataSourceImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : LocalTransactionDataSource {

    override suspend fun saveTransaction(trans: TransactionEntity) {
        transactionDao.insertTransaction(trans)
    }

    override suspend fun updateTransactionType(id : String, transactionType: TransactionType) {
        transactionDao.updateCategory(TransactionCategoryUpdate(id, transactionType, Clock.System.now()))
    }

    override suspend fun updateTransaction(trans : TransactionEntity) {
        transactionDao.updateTransaction(trans)
    }

    override suspend fun deleteTransaction(trans: TransactionEntity) {
        transactionDao.deleteTransaction(trans)
    }

    override fun observeTransactions() : Flow<List<TransactionEntity>> = transactionDao.observeTransactions()

    override fun observeMonthlyAmounts(from: Instant, to: Instant) : Flow<List<BigDecimal>> = transactionDao.observeMonthlyAmounts(from, to)

    override fun getTransactionsPeriodCategory(category : TransactionType, from : Instant, to: Instant) : Flow<List<TransactionEntity>> = transactionDao.getTransactionsForPeriodCategory(category, from, to)
}