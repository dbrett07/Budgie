package com.dbrett.budgie.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.dbrett.budgie.R
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun DateTimeSelectBox(
    modifier : Modifier = Modifier,
    label : (@Composable TextFieldLabelScope.() -> Unit)? = null,
    initialTime: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0,),
    onDateTimePicked : (LocalDateTime) -> Unit
) {
    val formatter = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char(' ')
        hour()
        char(':')
        minute()
    }

    val selectedDateStringState = rememberTextFieldState()
    selectedDateStringState.setTextAndPlaceCursorAtEnd(initialTime.format(formatter))
    var showTimePickerDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = selectedDateStringState,
            enabled = true,
            readOnly = true,
            label = label,
            trailingIcon = { Icon(painterResource(R.drawable.calendar_month), contentDescription = "Edit Date")},
            lineLimits = TextFieldLineLimits.SingleLine
        )

        Box (
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable { showTimePickerDialog = true }
        )
    }
    if (showTimePickerDialog) {
        DateTimeDialogue (
            modifier = Modifier.fillMaxSize(),
            onDismiss = { showTimePickerDialog = false },
            onDateTimePicked = { dt ->
                showTimePickerDialog = false
                onDateTimePicked(dt)
                selectedDateStringState.setTextAndPlaceCursorAtEnd(dt.format(formatter))
            }
        )
    }
}