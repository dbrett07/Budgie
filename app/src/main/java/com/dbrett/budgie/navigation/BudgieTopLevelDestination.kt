package com.dbrett.budgie.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.dbrett.budgie.R

data class BudgieTopLevelDestination (
    val route : Route,
    val icon : Int, // Reference to drawable asset
    val iconText : String,
)

val TOP_LEVEL_DESTINATIONS = listOf(
    BudgieTopLevelDestination(
        route = Route.Budget,
        icon = R.drawable.budget_donut,
        iconText = "Budget",
        ),
    BudgieTopLevelDestination(
        Route.Transactions,
        icon = R.drawable.transactions,
        iconText = "Transactions"
    ),
    BudgieTopLevelDestination(
        Route.Settings,
        icon = R.drawable.settings,
        iconText = "Settings"
    )
)