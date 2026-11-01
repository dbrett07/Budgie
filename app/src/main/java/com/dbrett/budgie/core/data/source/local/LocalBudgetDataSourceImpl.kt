package com.dbrett.budgie.core.data.source.local

import com.dbrett.budgie.core.data.source.local.dao.BudgetDao
import com.dbrett.budgie.core.data.source.local.entity.BudgetEntity
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalBudgetDataSourceImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : LocalBudgetDataSource {

    override fun observeBudgets(): Flow<List<BudgetEntity>> {
       return budgetDao.getBudgets()
    }

    override fun observeBudgetsOfCategory(category: TransactionType): Flow<List<BudgetEntity>> {
       return budgetDao.getBudgetsByCategory(category)
    }

    override fun getBudgetById(id: String): Flow<BudgetEntity?> {
        return budgetDao.getBudgetById(id)
    }

    override suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    override suspend fun deleteBudget(budget: BudgetEntity) {
        budget.isDeletedLocally = true
        updateBudget(budget)
    }
}