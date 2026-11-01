package com.dbrett.budgie.core.data.repository

import com.dbrett.budgie.core.model.Budget
import com.dbrett.budgie.core.model.BudgetWithTransactions
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    fun observeActiveBudgets() : Flow<List<BudgetWithTransactions>>

    fun getBudgetById(id : String) : Flow<BudgetWithTransactions?>

}