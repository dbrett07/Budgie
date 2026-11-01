package com.dbrett.budgie.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dbrett.budgie.designsystem.BudgieCard
import com.dbrett.budgie.designsystem.BudgieListItem

@Composable
fun BudgetDisplay(
    state : BudgetItemState,
    onClick : (String) -> Unit
) {
    BudgieCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onClick(state.id) }
    ) {
        Column {
            BudgieListItem(
                headlineText = { Text(text = state.title, style = MaterialTheme.typography.titleMedium) },
                supportingText = { Text("Remaining: ${state.remainingLabel}") },
                trailingContent = {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = state.balanceLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (state.overBudget) MaterialTheme.colorScheme.error else Color.Unspecified
                        )
                        Text(text = "of ${state.capLabel}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            )

            LinearProgressIndicator(
                progress = { state.progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                color = when (state.statusType) {
                    BudgetStatus.CRITICAL -> MaterialTheme.colorScheme.error
                    BudgetStatus.WARNING -> Color(0xFFE6A100) // Dark gold/amber for visibility
                    BudgetStatus.NORMAL -> Color(0xFF2E7D32)  // Material green
                },
                trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun BudgetDisplayPreview() {
    BudgetDisplay(
        BudgetItemState(
            "id-id-id",
            "Example budget",
            "£10.00",
            "£20.00",
            "£10.00",
            0.5f,
            false,
            BudgetStatus.NORMAL
        ),
        onClick = { }
    )
}