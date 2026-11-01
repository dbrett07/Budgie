package com.dbrett.budgie.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dbrett.budgie.core.model.TransactionType
import com.dbrett.budgie.designsystem.BudgieButton
import com.dbrett.budgie.designsystem.DateTimeSelectBox
import kotlinx.datetime.LocalDateTime

@Composable
fun TransactionFormContent(
    modifier : Modifier,
    titleState : TextFieldState,
    sourceState : TextFieldState,
    amountState : TextFieldState,
    initialTime : LocalDateTime,
    initialType : TransactionType = TransactionType.UNASSIGNED,
    onTimeSelected : (LocalDateTime) -> Unit,
    onTypeSelected : (TransactionType) -> Unit,
    onSubmit : () -> Unit
) {

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = titleState,
            label = { Text("Name") },
            )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = sourceState,
                label = { Text("Source") }
            )

            TransactionTypeSelect(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                initialType = initialType,
                onTypeSelected = onTypeSelected
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = amountState,
                label = { Text("Amount") }
            )

            DateTimeSelectBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = { Text("Time") },
                initialTime = initialTime,
                onDateTimePicked = onTimeSelected
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BudgieButton(
                onClick = {
                    onSubmit()
                },
                enabled = true,
                text = { Text("Submit") }
            )
        }
    }
}