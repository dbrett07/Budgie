package com.dbrett.budgie.feature.budget

import androidx.lifecycle.ViewModel
import com.dbrett.budgie.core.data.repository.BudgetRepository
import com.dbrett.budgie.core.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository : BudgetRepository,
    private val transactionRepository : TransactionRepository
) : ViewModel() {

}