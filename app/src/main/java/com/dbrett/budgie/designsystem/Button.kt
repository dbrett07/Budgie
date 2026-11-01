package com.dbrett.budgie.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BudgieButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    contentPadding : PaddingValues = ButtonDefaults.ContentPadding,
    content : @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors( containerColor = MaterialTheme.colorScheme.primary ),
        content = content
    )
}

@Composable
fun BudgieButton(
    onClick : () -> Unit,
    modifier : Modifier = Modifier,
    enabled : Boolean,
    text: @Composable () -> Unit,
    icon : @Composable (() -> Unit)? = null
) {
    BudgieButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (icon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        }
    ) {
        BudgieButtonContent(
            text = text,
            icon = icon
        )
    }
}

@Composable
fun BudgieButtonContent(
    text : @Composable () -> Unit,
    icon : @Composable (() -> Unit)? = null
) {
    if (icon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            icon()
        }
    }
    Box(
        Modifier
            .padding(
                start = if (icon != null) {
                    ButtonDefaults.IconSpacing
                } else {
                    0.dp
                }
            )
    ) {
        text()
    }
}