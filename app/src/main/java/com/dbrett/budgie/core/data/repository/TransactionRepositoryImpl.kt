package com.dbrett.budgie.core.data.repository

import android.util.Log
import com.dbrett.budgie.core.data.source.local.LocalTransactionDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Instant

class TransactionRepositoryImpl @Inject constructor(
    private val localTransactionDataSource: LocalTransactionDataSource,
) : TransactionRepository {

    val transactions = localTransactionDataSource.observeTransactions()

    override fun observeTransactions() : Flow<List<Transaction>> {
        return localTransactionDataSource.observeTransactions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    // Sync transactions received from the network so they can be written to the local database.
    // Also, push unsynced local transactions to the backend.
    override suspend fun syncTransactions() { }

    override suspend fun saveTransaction(trans : Transaction) {
        try {
            localTransactionDataSource.saveTransaction(TransactionEntity.fromDomainModel(trans))
        } catch (e : Exception) {
            Log.e("com.dbrett.budgie", "SUPER EVIL DATABASE ERROR: $e")
        }
    }

    override suspend fun updateTransactionType(id : String, newType : TransactionType) {
        localTransactionDataSource.updateTransactionType(id, newType)
    }

    override suspend fun updateTransaction(trans : Transaction) {
        localTransactionDataSource.updateTransaction(TransactionEntity.fromDomainModel(trans))
    }

    override suspend fun deleteTransaction(trans: Transaction) {
        val entity = TransactionEntity.fromDomainModel(trans)
        entity.isDeletedLocally = true
        localTransactionDataSource.updateTransaction(entity)
    }

}