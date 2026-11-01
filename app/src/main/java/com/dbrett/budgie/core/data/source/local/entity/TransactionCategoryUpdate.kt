package com.dbrett.budgie.core.data.source.local.entity

import com.dbrett.budgie.core.model.TransactionType
import kotlin.time.Instant

data class TransactionCategoryUpdate (
    val id: String,
    val transactionType: TransactionType,
    val modified : Instant
)