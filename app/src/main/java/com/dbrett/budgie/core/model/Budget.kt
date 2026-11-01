package com.dbrett.budgie.core.model

import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Budget(
    val id : String = Uuid.random().toString(),
    val title : String,
    val category : TransactionType,
    val from : LocalDateTime,
    val to : LocalDateTime,
    val period : BudgetPeriodType,
    val cap : BigDecimal,
    val alertAt : Float,
    val created : Instant
)
