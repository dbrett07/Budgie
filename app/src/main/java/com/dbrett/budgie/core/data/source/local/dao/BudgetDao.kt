package com.dbrett.budgie.core.data.source.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.dbrett.budgie.core.data.source.local.entity.BudgetEntity
import com.dbrett.budgie.core.model.BudgetPeriodType
import com.dbrett.budgie.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.Instant

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE isDeletedLocally = 0 AND :now BETWEEN `from` AND `to` ORDER BY created ASC")
    fun getBudgets(now : Instant = Clock.System.now()) : Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE category = :category AND isDeletedLocally = 0 ORDER BY created ASC")
    fun getBudgetsByCategory(category : TransactionType) : Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE period = :period AND isDeletedLocally = 0 ORDER BY created ASC")
    fun getBudgetsByPeriod(period : BudgetPeriodType) : Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE id = :id")
    fun getBudgetById(id : String) : Flow<BudgetEntity?>

    @Insert
    suspend fun saveBudget(budget : BudgetEntity)

    @Update
    suspend fun updateBudget(budget : BudgetEntity)

}