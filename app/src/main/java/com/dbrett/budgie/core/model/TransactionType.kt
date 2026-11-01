package com.dbrett.budgie.core.model

import com.dbrett.budgie.R

enum class TransactionType(val displayName : String) {
    UNASSIGNED("None"),
    MISC("Misc."),
    FOOD("Food"),
    GROCERY("Grocery"),
    RENT("Rent"),
    UTILITY("Bills")
}

fun getIcon(transactionType: TransactionType) : Int {
    return when (transactionType) {
        TransactionType.UTILITY -> R.drawable.utility
        TransactionType.MISC -> R.drawable.miscpurchase
        TransactionType.FOOD -> R.drawable.foodout
        TransactionType.GROCERY -> R.drawable.grocery
        TransactionType.RENT -> R.drawable.rent
        TransactionType.UNASSIGNED -> R.drawable.money
    }
}