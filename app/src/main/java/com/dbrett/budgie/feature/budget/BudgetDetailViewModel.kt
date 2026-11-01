package com.dbrett.budgie.feature.budget

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbrett.budgie.core.data.repository.BudgetRepository
import com.dbrett.budgie.core.model.Budget
import com.dbrett.budgie.core.ui.BudgetStatus
import com.dbrett.budgie.navigation.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class BudgetDetailViewModel @AssistedInject constructor(
    @Assisted val navKey : Route.BudgetDetail,
    val budgetRepository: BudgetRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Route.BudgetDetail) : BudgetDetailViewModel
    }

    val id = navKey.budgetId

    val numDateFormatter = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char(' ')
        hour()
        char(':')
        minute()
    }

    val englishDateFormatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(',')
        char(' ')
        day()
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        year()
    }

    val uiState = budgetRepository.getBudgetById(id)
        .map { budget ->
            if (budget == null ) {
                BudgetDetailUiState.Error("Could not find budget with the specified id.")
            } else {
                BudgetDetailUiState.Success(
                    id = budget.budget.id,
                    title = budget.budget.title,
                    balanceLabel = "£$budget.spend",
                    capLabel = "£${budget.budget.cap}",
                    remainingLabel = "£${(budget.budget.cap - budget.spend)}",
                    progressPercent = (budget.spend / budget.budget.cap).toFloat(),
                    overBudget = (budget.spend > budget.budget.cap),
                    statusType = BudgetStatus.NORMAL,
                    category = budget.budget.category,
                    createdLabel = englishDateFormatter.format(budget.budget.created.toLocalDateTime(TimeZone.currentSystemDefault()).date),
                    timeLeftLabel = "${budget.budget.to.toInstant(TimeZone.currentSystemDefault()) - Clock.System.now()}",
                    transactions = budget.transactions
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BudgetDetailUiState.Loading
        )
}