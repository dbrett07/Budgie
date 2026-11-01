package com.dbrett.budgie.core.data.repository

import com.dbrett.budgie.core.data.source.local.LocalBudgetDataSource
import com.dbrett.budgie.core.data.source.local.LocalTransactionDataSource
import com.dbrett.budgie.core.model.Budget
import com.dbrett.budgie.core.model.BudgetWithTransactions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.collections.emptyList

class BudgetRepositoryImpl @Inject constructor(
    val transactionDataSource: LocalTransactionDataSource,
    val budgetDataSource: LocalBudgetDataSource
) : BudgetRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeActiveBudgets() : Flow<List<BudgetWithTransactions>> {
        return budgetDataSource.observeBudgets().flatMapLatest { budgets ->
            if (budgets.isEmpty()) {
                kotlinx.coroutines.flow.flowOf<List<BudgetWithTransactions>>(emptyList())
            } else {

                val budgetFlows = budgets.map { budget ->
                    transactionDataSource.getTransactionsPeriodCategory(
                        budget.category,
                        budget.from,
                        budget.to
                    ).map { transactions ->
                        val spend = transactions.fold(BigDecimal.ZERO) { acc, trans ->
                            acc.add(trans.amount)
                        }


                        BudgetWithTransactions(
                            budget.toDomainModel(),
                            transactions.map { it.toDomainModel() },
                            spend
                        )
                    }
                }

                combine(budgetFlows) { it.toList() }
            }
        }
            .flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBudgetById(id: String): Flow<BudgetWithTransactions?> {
        return budgetDataSource.getBudgetById(id).flatMapLatest { budget ->
            if (budget == null) {
                flowOf(null)
            } else {
            transactionDataSource.getTransactionsPeriodCategory(budget.category, budget.to, budget.from).map { transactions ->
                val spend = transactions.fold(BigDecimal.ZERO) { acc, trans ->
                    acc.add(trans.amount)
                }

                BudgetWithTransactions(
                    budget.toDomainModel(),
                    transactions.map { it.toDomainModel() },
                    spend
                )
            }
            }
        }
            .flowOn(Dispatchers.IO)
    }
}