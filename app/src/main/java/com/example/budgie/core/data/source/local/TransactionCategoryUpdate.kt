package com.example.budgie.core.data.source.local

import com.example.budgie.core.data.model.TransactionType
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TransactionCategoryUpdate (
    val id: Uuid,
    val source: String,
    val transactionType: TransactionType,
    val modified : Instant
)