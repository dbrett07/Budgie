package com.dbrett.budgie.core.model

import java.math.BigDecimal
import kotlin.time.Instant
import kotlin.uuid.Uuid

// Domain model of a Transaction (which is used in UI, not the data layer).
data class Transaction (
    val id : String = Uuid.random().toString(),
    val title : String,
    val source : String, // E.g. Business name
    var transactionType : TransactionType, // Transaction type, E.g. food, misc, etc
    val amount : BigDecimal,
    val timestamp : Instant, // when the transaction occurred
)