package com.dbrett.budgie.feature.budget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dbrett.budgie.core.model.Budget


@Composable
fun BudgetScreen(
    modifier : Modifier = Modifier,
    viewModel : BudgetViewModel = hiltViewModel()
) {
    BudgetScreen(
        modifier = modifier.fillMaxSize(),
        budgets = emptyList()
    )
}

@Composable
fun BudgetScreen(
    modifier : Modifier = Modifier,
    budgets : List<Budget>
) {
    Box(
        modifier = modifier
    ) {
        Text("Budget")
    }
}