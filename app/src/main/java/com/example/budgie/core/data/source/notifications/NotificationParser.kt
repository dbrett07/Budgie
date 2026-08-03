package com.example.budgie.core.data.source.notifications


import com.example.budgie.core.data.model.TransactionType
import com.example.budgie.core.data.source.local.TransactionEntity
import java.math.BigDecimal
import java.time.Instant

object NotificationParser {
    fun parse(postTime : Long, notificationText : String) : TransactionEntity {
        val source = ""
        return TransactionEntity(
            timestamp = Instant.ofEpochMilli(postTime),
            modified  = Instant.ofEpochMilli(postTime),
            source = source,
            transactionType = TransactionType.MISC,
            amount = BigDecimal(0)
        )
    }
}