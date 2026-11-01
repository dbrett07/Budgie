package com.dbrett.budgie.core.data.source.notifications


import com.dbrett.budgie.core.model.TransactionType
import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity
import com.dbrett.budgie.core.model.Transaction
import java.math.BigDecimal
import kotlin.time.Instant

object NotificationParser {
    val genericTitles = listOf("Google Wallet", "Google Play", "Google Play Store", "Google Pay")
    fun parse(postTime : Long, title : String, notificationText : String) : TransactionEntity {
        val amountRegex = """£\s*?([0-9.]+)""".toRegex()
        val amountMatch = amountRegex.find(notificationText) ?: amountRegex.find(title)
        if (amountMatch == null) {
            return TransactionEntity(
                id = "0",
                title = "ERROR",
                timestamp = Instant.fromEpochMilliseconds(postTime),
                modified  = Instant.fromEpochMilliseconds(postTime),
                source = "PARSE ERROR",
                transactionType = TransactionType.UNASSIGNED,
                amount = BigDecimal(-1)
            )
        }

        val parsedSource = if (title in genericTitles) when {
            notificationText.contains(" at ") -> notificationText.substringAfter(" at ").trim()
            notificationText.contains(" to ") -> notificationText.substringAfter(" to ").substringBefore(" has ").trim()
            else -> "Google Digital Purchase"
        } else {
            title
        }

        return TransactionEntity.fromDomainModel(Transaction(
            title = parsedSource,
            timestamp = Instant.fromEpochMilliseconds(postTime),
            source = parsedSource,
            transactionType = TransactionType.UNASSIGNED,
            amount = BigDecimal(amountMatch.toString())
        )).copy(modified = Instant.fromEpochMilliseconds(postTime))
    }
}