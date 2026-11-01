package com.dbrett.budgie.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbrett.budgie.core.data.repository.TransactionRepository
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val repository: TransactionRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedType : MutableStateFlow<TransactionType?> = MutableStateFlow(null)
    val selectedType = _selectedType.asStateFlow()

    private val _transactionFlow = repository.observeTransactions()

    val uiState : StateFlow<TransactionsUiState> = combine(
        _searchQuery,
        _selectedType,
        _transactionFlow
    ) {
        query, type, transactions ->
            val filteredList = transactions
                .filter { type == null || it.transactionType == type }
                .filter { query.isBlank() || it.source.contains(query, ignoreCase = true) }

            val typeTotal = filteredList.fold(BigDecimal.ZERO) { acc, trans ->
                acc + trans.amount
            }

        TransactionsUiState(filteredList, typeTotal)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionsUiState(null, null)
    )

    fun updateTransactionType(id : String, newType : TransactionType) {
        viewModelScope.launch {
            repository.updateTransactionType(id, newType)
        }
    }

    fun updateTransaction(trans : Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(trans)
        }
    }

    fun deleteTransaction(trans : Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(trans)
        }
    }
}