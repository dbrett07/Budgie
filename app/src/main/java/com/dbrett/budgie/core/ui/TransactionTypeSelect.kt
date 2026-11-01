package com.dbrett.budgie.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dbrett.budgie.core.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeSelect(
    modifier : Modifier = Modifier,
    initialType : TransactionType = TransactionType.UNASSIGNED,
    onTypeSelected : (TransactionType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(initialType) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = selected.displayName,
            readOnly = true,
            onValueChange = {},
            label = { Text("") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TransactionType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelected(type)
                        selected = type
                        expanded = false
                    }
                )
            }
        }
    }
}