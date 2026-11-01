package com.dbrett.budgie.core.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import com.dbrett.budgie.R
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import com.dbrett.budgie.designsystem.BudgieListItem
import com.dbrett.budgie.ui.theme.BudgieTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import kotlin.math.roundToInt
import kotlin.time.Clock

enum class TransactionItemDragAnchors {
    Start,
    End
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionListItemWithSwipeMenu(
    transaction: Transaction,
    onDeleteClicked: (Transaction) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val dragState = remember {
        AnchoredDraggableState(
            initialValue = TransactionItemDragAnchors.Start,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.background)
            .clip(MaterialTheme.shapes.medium)
            .clipToBounds(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(72.dp)
                .align(Alignment.CenterEnd)
                .onSizeChanged { size ->
                    val widthPx = size.width.toFloat()
                    val newAnchors = DraggableAnchors {
                        TransactionItemDragAnchors.Start at 0f
                        TransactionItemDragAnchors.End at -widthPx
                    }
                    dragState.updateAnchors(newAnchors)
                }
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onDeleteClicked(transaction)
                        coroutineScope.launch {
                            dragState.animateTo(
                                TransactionItemDragAnchors.Start,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.delete), contentDescription = "")
            }
        }


        Box(
            modifier = Modifier
                .fillMaxHeight()
                .layout { measurable, constraints ->
                    val dragOffset = if (dragState.offset.isNaN()) 0 else dragState.offset.roundToInt()
                    val width = constraints.maxWidth + dragOffset
                    val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))

                    layout(width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                        state = dragState,
                        positionalThreshold = { distance: Float -> distance * 0.3f },
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                )
        ) {
            Box(Modifier.fillMaxSize()) {
                TransactionListItem(
                    transaction = transaction,
                    onClick = {
                        Log.d("com.dbrett.budgie", "CLICKED")
                        onClick()
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionListItem(
    transaction: Transaction,
    modifier : Modifier = Modifier,
    onClick: () -> Unit
) {
    BudgieListItem(
        modifier = modifier,
        onClick = onClick,
        headlineText = { Text(transaction.title) },
        supportingText = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(transaction.source)
                Text(transaction.amount.toString())
                Text(transaction.transactionType.toString())
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TransactionListItemWithSwipeMenuPreview() {
    BudgieTheme {
        LazyColumn(modifier = Modifier) {
            items(count = 1) {
                TransactionListItemWithSwipeMenu(
                    transaction = Transaction(
                        "SuperCoolID", "Blah Blah Purchase", "Blah Blah Industries", TransactionType.MISC,
                        BigDecimal.TEN, Clock.System.now()
                    ),
                    onDeleteClicked = { Log.d(null, "Delete was clicked") },
                    onClick = {},
                    modifier = Modifier
                )
            }
        }
    }
}