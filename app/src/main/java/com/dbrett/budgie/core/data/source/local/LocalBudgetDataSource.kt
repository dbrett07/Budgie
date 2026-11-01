package com.dbrett.budgie.core.data.source.local

import com.dbrett.budgie.core.data.source.local.entity.BudgetEntity
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface LocalBudgetDataSource {

    fun observeBudgets() : Flow<List<BudgetEntity>>

    fun observeBudgetsOfCategory(category : TransactionType) : Flow<List<BudgetEntity>>

    fun getBudgetById(id : String) : Flow<BudgetEntity?>

    suspend fun updateBudget(budget : BudgetEntity)

    suspend fun deleteBudget(budget: BudgetEntity)
}