package com.dbrett.budgie.core.data.source.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.dbrett.budgie.core.model.Budget
import com.dbrett.budgie.core.model.BudgetPeriodType
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal
import kotlin.time.Clock
import kotlin.time.Instant

/*
An entity representing a single budget period (monthly).
 */
@Entity(tableName = "budgets")
data class BudgetEntity (
    @PrimaryKey val id : String, // UUID
    val title : String,
    val category : TransactionType,
    val from : Instant,
    val to : Instant,
    val period : BudgetPeriodType,
    val cap : BigDecimal,
    val alertAt : Float,
    val created : Instant,

    val modified : Instant,
    var syncStatus: SyncStatus = SyncStatus.PENDING,
    var isDeletedLocally : Boolean = false
) {
    fun toDomainModel() : Budget {
        return Budget(
            id = id,
            title = title,
            category = category,
            from = from.toLocalDateTime(TimeZone.currentSystemDefault()),
            to = to.toLocalDateTime(TimeZone.currentSystemDefault()),
            period = period,
            cap = cap,
            alertAt = alertAt,
            created = created
        )
    }

    companion object  {
        fun fromDomainModel(budget : Budget) : BudgetEntity {
            return BudgetEntity(
                id = budget.id,
                title = budget.title,
                category = budget.category,
                from = budget.from.toInstant(TimeZone.UTC),
                to = budget.to.toInstant(TimeZone.UTC),
                period = budget.period,
                cap = budget.cap,
                alertAt = budget.alertAt,
                created = budget.created,
                modified = Clock.System.now(),
                syncStatus = SyncStatus.PENDING,
                isDeletedLocally = false,
            )
        }
    }
}