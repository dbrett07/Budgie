package com.dbrett.budgie.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    sealed interface Auth : Route {
        @Serializable
        data object Login : Auth

        @Serializable
        data object Register : Auth
    }

    @Serializable
    data object Budget : Route

    @Serializable
    data object Transactions : Route

    @Serializable
    data object AddTransaction : Route

    @Serializable
    data class BudgetDetail(val budgetId : String) : Route

    @Serializable
    data object Settings : Route
}