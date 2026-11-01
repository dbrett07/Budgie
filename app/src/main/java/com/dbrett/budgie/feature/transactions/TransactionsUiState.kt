package com.dbrett.budgie.feature.transactions

import com.dbrett.budgie.core.model.Transaction
import java.math.BigDecimal

data class TransactionsUiState (
        val transactions : List<Transaction>?,
        val transactionsSum : BigDecimal?,
        val loading : Boolean = true
    )
