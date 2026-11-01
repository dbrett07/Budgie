package com.dbrett.budgie.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun BudgieCard(
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border : BorderStroke? = null,
    content : @Composable (ColumnScope.() -> Unit)
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = elevation,
        border = border,
        content = content
    )
}