package com.dbrett.budgie.core.model

import java.math.BigDecimal

data class BudgetWithTransactions(
    val budget : Budget,
    val transactions : List<Transaction>,
    val spend : BigDecimal
)