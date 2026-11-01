package com.dbrett.budgie.designsystem

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeDialogue(
    modifier : Modifier = Modifier,
    onDateTimePicked : (LocalDateTime) -> Unit,
    onDismiss : () -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var resultDateTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }

    if (!showTimePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            resultDateTime = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                        }
                        showTimePicker = true
                    }
                ) {
                    Text("Next")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    } else {
        TimePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        resultDateTime = LocalDateTime(
                            date = resultDateTime.date,
                            time = LocalTime(timePickerState.hour, timePickerState.minute)
                        )
                        onDateTimePicked(resultDateTime)
                        onDismiss()
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Back") }
            },
            title = { Text("Select Transaction Time") }
        ) { TimePicker(state = timePickerState) }
    }
}