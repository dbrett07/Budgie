package com.dbrett.budgie.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.dbrett.budgie.core.data.repository.AuthState
import com.dbrett.budgie.core.model.User
import com.dbrett.budgie.feature.auth.AuthViewModel
import com.dbrett.budgie.feature.auth.LoginScreen
import com.dbrett.budgie.feature.auth.RegisterScreen
import com.dbrett.budgie.feature.budget.BudgetDetailScreen
import com.dbrett.budgie.feature.budget.BudgetDetailViewModel
import com.dbrett.budgie.feature.budget.BudgetScreen
import com.dbrett.budgie.feature.budget.BudgetViewModel
import com.dbrett.budgie.feature.settings.SettingsScreen
import com.dbrett.budgie.feature.transactions.AddTransactionScreen
import com.dbrett.budgie.feature.transactions.TransactionScreen

@Composable
fun BudgieNavHost(
    modifier: Modifier = Modifier,
    topLevelDestinations : List<BudgieTopLevelDestination>,
    authViewModel : AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is AuthState.Unauthenticated -> {
            UnauthenticatedContent(modifier = modifier)
        }

        is AuthState.Authenticated -> {
            AuthenticatedContent(
                modifier = modifier,
                topLevelDestinations
            )
        }
    }
}

@Composable
fun UnauthenticatedContent(
    modifier : Modifier = Modifier
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Auth.Login,
        topLevelRoutes = setOf(Route.Auth.Login)
    )

    val navigator = remember { Navigator(navigationState) }

    NavDisplay(
        modifier = modifier,
        onBack = { navigator.goBack() },
        entries = navigationState.toEntries(
            entryProvider {
                entry<Route.Auth.Login> {
                    LoginScreen(
                        onRegisterClick = { navigator.navigate(Route.Auth.Register) }
                    )
                }

                entry<Route.Auth.Register> {
                    RegisterScreen(

                    )
                }
            }
        )
    )
}

@Composable
fun AuthenticatedContent(
    modifier : Modifier = Modifier,
    topLevelDestinations: List<BudgieTopLevelDestination>
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Budget,
        topLevelRoutes = topLevelDestinations
            .map { it.route }
            .toSet()
    )

    val navigator = remember {
        Navigator(navigationState)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BudgieNavigationBar(
                destinations = topLevelDestinations,
                selectedKey = navigationState.topLevelRoute,
                onSelectKey = {
                    navigator.navigate(it)
                }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onBack = {
                navigator.goBack()
            },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Budget> {
                        BudgetScreen(Modifier)
                    }

                    entry<Route.Transactions> {
                        TransactionScreen(
                            onFabClick = {
                                navigator.navigate(Route.AddTransaction)
                            }
                        )
                    }

                    entry<Route.Settings> {
                        SettingsScreen()
                    }

                    entry<Route.AddTransaction> {
                        AddTransactionScreen(onClose = { navigator.goBack() } )
                    }

                    entry<Route.BudgetDetail> { key ->
                        val viewModel = hiltViewModel<BudgetDetailViewModel, BudgetDetailViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(key)
                            }
                        )

                        val state by viewModel.uiState.collectAsStateWithLifecycle()
                        BudgetDetailScreen(
                            state = state,
                            onClose = { navigator.goBack() }
                        )
                    }
                }
            )
        )
    }
}