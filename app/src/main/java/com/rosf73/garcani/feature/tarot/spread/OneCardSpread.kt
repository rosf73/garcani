package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OneCardSpread(
    modifier: Modifier = Modifier,
    selectedCard: Map.Entry<String, String>?,
    onDoneSelecting: (String) -> Unit,
    onInterpret: (String) -> Unit,
) {
    val uiState = remember { mutableStateOf<OneCardState>(OneCardState.Ready) }

    LaunchedEffect(key1 = selectedCard) {
        if (selectedCard != null) {
            uiState.value = OneCardState.Selecting
            delay(800)
            uiState.value = OneCardState.Opening(selectedCard.value) // as only 1 card is needed
            onDoneSelecting(selectedCard.key)
            delay(1200)
            onInterpret(selectedCard.key)
        }
    }

    when (uiState.value) {
        OneCardState.Ready -> {}
        OneCardState.Selecting -> {
            SpreadCard(
                modifier = modifier.size(120.dp, 180.dp),
                isOpened = false,
            )
        }
        is OneCardState.Opening -> {
            SpreadCard(
                modifier = modifier.size(120.dp, 180.dp),
                isOpened = true,
            ) {
                // TODO : add tarot card image
            }
        }
    }
}

private sealed interface OneCardState {
    data object Ready : OneCardState
    data object Selecting : OneCardState
    data class Opening(val imageUrl: String) : OneCardState
}