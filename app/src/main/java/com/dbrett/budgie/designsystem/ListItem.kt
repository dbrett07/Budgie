package com.dbrett.budgie.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun BudgieListItem(
    headlineText : @Composable () -> Unit,
    modifier : Modifier = Modifier,
    supportingText : (@Composable () -> Unit)? = null,
    leadingContent : (@Composable () -> Unit)? = null,
    trailingContent : (@Composable () -> Unit)? = null,
    onClick : (() -> Unit)? = null,
    containerColor : Color = MaterialTheme.colorScheme.surfaceContainerLow,
    shape : Shape = MaterialTheme.shapes.medium
) {
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = shape,
        color = containerColor,
        modifier = modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = headlineText,
            supportingContent = supportingText,
            leadingContent = leadingContent?.let {
                {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(MaterialTheme.shapes.small),
                        contentAlignment = Alignment.Center
                    ) {
                        it()
                    }
                }
            },
            trailingContent = trailingContent,
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
                supportingColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}