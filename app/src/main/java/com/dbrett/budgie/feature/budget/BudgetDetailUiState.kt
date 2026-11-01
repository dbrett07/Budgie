package com.dbrett.budgie.feature.budget

import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import com.dbrett.budgie.core.ui.BudgetStatus

sealed interface BudgetDetailUiState  {
    data object Loading : BudgetDetailUiState

    data class Error(val message : String) : BudgetDetailUiState

    data class Success(
        val id : String,
        val title : String,

        val balanceLabel : String,
        val capLabel : String,
        val remainingLabel : String,
        val progressPercent : Float,
        val overBudget : Boolean,
        val statusType : BudgetStatus,
        val category : TransactionType,
        val timeLeftLabel : String,
        val createdLabel : String,

        val transactions : List<Transaction>
    ) : BudgetDetailUiState
}