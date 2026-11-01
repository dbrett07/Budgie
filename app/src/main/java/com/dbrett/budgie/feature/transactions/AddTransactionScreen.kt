package com.dbrett.budgie.feature.transactions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dbrett.budgie.R
import com.dbrett.budgie.core.ui.TransactionFormContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onClose : () -> Unit,
    viewModel: TransactionFormViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    "Add Transaction",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(painterResource(R.drawable.close), contentDescription = "close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    )
    { innerPad ->
        TransactionFormContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPad),
            titleState = viewModel.titleState,
            sourceState = viewModel.sourceState,
            amountState = viewModel.amountState,
            initialTime = viewModel.timeState,
            onTimeSelected = { dt ->
                viewModel.setTime(dt)
            },
            onTypeSelected = {type ->
                viewModel.setTransactionType(type)
            },
            onSubmit = {
                viewModel.submit()
                viewModel.reset()
                onClose()
            }
        )
    }
}