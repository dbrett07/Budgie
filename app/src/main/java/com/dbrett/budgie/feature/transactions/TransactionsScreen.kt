package com.dbrett.budgie.feature.transactions

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dbrett.budgie.R
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.ui.TransactionFormContent
import com.dbrett.budgie.core.ui.TransactionListItemWithSwipeMenu
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.format.format
import kotlin.text.format

@Composable
fun TransactionScreen(
    viewModel : TransactionsViewModel = hiltViewModel(),
    onFabClick : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TransactionScreen(
        transactions = uiState.transactions,
        onTransactionDelete = { transaction ->
            viewModel.deleteTransaction(transaction)
        },
        onFabClick = onFabClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    transactions : List<Transaction>?,
    onTransactionDelete : (Transaction) -> Unit,
    onFabClick: () -> Unit,
    formViewModel: TransactionFormViewModel = hiltViewModel()
) {
    var sheetShowing by remember { mutableStateOf(false) }
    var currentEditTransaction : Transaction? by remember { mutableStateOf(null) }
    val sheetState = rememberModalBottomSheetState()
    formViewModel.setMode(FormMode.EDIT)

    val formatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(',')
        char(' ')
        day()
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        year()
    }

    val groupedTransactions = transactions?.groupBy { transaction ->
        transaction.timestamp
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Add Transaction"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding.plus(PaddingValues(horizontal = 8.dp))),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Transactions", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(thickness = 4.dp)
            if (groupedTransactions == null) {
                // TODO
                1
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        ,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedTransactions.forEach { (date, transactions) ->
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(text = formatter.format(date), modifier = Modifier.padding(8.dp))
                                HorizontalDivider(thickness = 1.dp)
                            }


                        }
                        items(items = transactions) { transaction ->
                            Log.d("com.dbrett.budgie", "Transaction item created")
                            TransactionListItemWithSwipeMenu(
                                transaction = transaction,
                                onClick = {
                                    sheetShowing = true

                                    formViewModel.setId(transaction.id)
                                    formViewModel.titleState.setTextAndPlaceCursorAtEnd(transaction.title)
                                    formViewModel.sourceState.setTextAndPlaceCursorAtEnd(transaction.source)
                                    formViewModel.amountState.setTextAndPlaceCursorAtEnd(transaction.amount.toString())
                                    formViewModel.setTransactionType(transaction.transactionType)
                                    formViewModel.setTime(transaction.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()))

                                },
                                onDeleteClicked = onTransactionDelete
                            )
                        }
                    }
                }

            }
        }

        if (sheetShowing) {
            ModalBottomSheet(
                modifier = Modifier,
                onDismissRequest = {
                    sheetShowing = false
                    currentEditTransaction = null
                },

                sheetState = sheetState
            ) {
                TransactionFormContent(
                    modifier = Modifier,
                    titleState = formViewModel.titleState,
                    sourceState = formViewModel.sourceState,
                    amountState = formViewModel.amountState,
                    initialTime = formViewModel.timeState,
                    initialType = formViewModel.typeState,
                    onTypeSelected = { type ->
                        formViewModel.setTransactionType(type)
                    },
                    onTimeSelected = { time ->
                        formViewModel.setTime(time)
                    },
                    onSubmit = {
                        formViewModel.submit()
                        formViewModel.reset()
                        sheetShowing = false
                        currentEditTransaction = null
                    },
                )
            }
        }
    }
}
