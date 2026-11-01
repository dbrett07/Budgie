package com.dbrett.budgie.core.ui

import java.math.BigDecimal

data class BudgetItemState(
    val id : String,
    val title : String,

    val balanceLabel : String,
    val capLabel : String,
    val remainingLabel : String,
    val progressPercent : Float,
    val overBudget : Boolean,
    val statusType : BudgetStatus
)

enum class BudgetStatus {
    NORMAL, WARNING, CRITICAL
}
