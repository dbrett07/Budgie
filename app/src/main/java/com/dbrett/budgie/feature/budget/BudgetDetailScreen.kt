package com.dbrett.budgie.feature.budget

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dbrett.budgie.R
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import com.dbrett.budgie.core.model.getIcon
import com.dbrett.budgie.core.ui.BudgetStatus
import com.dbrett.budgie.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailScreen(
    state : BudgetDetailUiState,
    onClose : () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .then(
                if (state is BudgetDetailUiState.Success) {
                    Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                } else {
                    Modifier
                }
            ),
        topBar = {
            if (state is BudgetDetailUiState.Success) {
                MediumTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(getIcon(state.category)),
                                contentDescription = "Transaction Type",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.title
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(painterResource(R.drawable.close), contentDescription = "close")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    scrollBehavior = scrollBehavior
                )
            } else {
                TopAppBar(
                    title = { Text("Budget") },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(painterResource(R.drawable.close), contentDescription = "close")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        when (state) {
            BudgetDetailUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                        trackColor = MaterialTheme.colorScheme.surface,
                    )

                    Spacer(Modifier.size(16.dp))

                    Text("Loading Budget Info...", style = MaterialTheme.typography.bodyMedium)
                }
            }

            is BudgetDetailUiState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.error),
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(Modifier.size(16.dp))

                    Text(
                        "An error occurred while loading budget info.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            is BudgetDetailUiState.Success -> {
                LazyColumn(
                    Modifier.fillMaxSize(),
                ) {
                    item {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                                .padding(top = 16.dp, bottom = 24.dp)
                        )
                        {
                            Spacer(Modifier.height(innerPadding.calculateTopPadding()))
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                text = "${state.remainingLabel} left of ${state.capLabel}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp)
                                    .height(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                LinearProgressIndicator(
                                    progress = { state.progressPercent },
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    color = when (state.statusType) {
                                        BudgetStatus.CRITICAL -> MaterialTheme.colorScheme.error
                                        BudgetStatus.WARNING -> Color(0xFFE6A100) // Dark gold/amber for visibility
                                        BudgetStatus.NORMAL -> Color(0xFF2E7D32)  // Material green
                                    },
                                    trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                                    gapSize = 0.dp,
                                    drawStopIndicator = {}
                                )

                                Text(
                                    text = "${(state.progressPercent * 100).toInt()}%",
                                    color = if (state.progressPercent < 0.5f) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.onPrimary
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(
                                ) {
                                    Text(
                                        text = "Budget Category"
                                    )

                                    Text(
                                        text = "${state.category}"
                                    )
                                }

                                Column(

                                ) {
                                    Text(
                                        text = "Created"
                                    )

                                    Text(
                                        text = state.createdLabel
                                    )
                                }

                                Column() {
                                    Text(
                                        text = "Time Remaining"
                                    )

                                    Text(
                                        text = state.timeLeftLabel
                                    )
                                }
                            }
                        }
                    }

                    items(30) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal= 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Transaction History Card Content #$index",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BudgetDetailPreview() {
    BudgetDetailScreen(BudgetDetailUiState.Success(
        id = "example-id-123",
        title = "Example Budget",
        balanceLabel = "£75.26",
        capLabel = "£150",
        remainingLabel = "£${(150f - 26.75f)}",
        progressPercent = 26.75f / 150f,
        overBudget = false,
        statusType = BudgetStatus.NORMAL,
        category = TransactionType.MISC,
        timeLeftLabel = "5 days",
        createdLabel = "1-1-1970 8:59:00",
        transactions = emptyList()
    ), onClose = {})
}