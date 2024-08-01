package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ThreeCardSpread(
    modifier: Modifier = Modifier,
    selectedCards: SnapshotStateMap<String, String>,
    onDoneSelecting: (String, String, String) -> Unit,
    onInterpret: (String, String, String) -> Unit,
) {
    val uiState = remember { mutableStateOf<ThreeCardState>(ThreeCardState.Ready) }
    var openCount = 0
    var isOpened by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedCards.size) {
        val cards = selectedCards.keys.toList()
        val urls = selectedCards.values.toList()

        if (selectedCards.size > 2) {
            uiState.value = ThreeCardState.ThirdSelected
            delay(800)
            onDoneSelecting(cards[2], cards[1], cards[0])
            uiState.value = ThreeCardState.Opening(urls)
        } else if (selectedCards.size > 1) {
            uiState.value = ThreeCardState.SecondSelected
        } else if (selectedCards.isNotEmpty()) {
            uiState.value = ThreeCardState.FirstSelected
        }
    }

    fun checkOpening() {
        if (openCount > 2) {
            coroutineScope.launch {
                isOpened = true
                delay(1200)

                val cards = selectedCards.keys.toList()
                onInterpret(cards[2], cards[1], cards[0])
            }
        }
    }

    if (!isOpened) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (uiState.value !is ThreeCardState.Ready) {
                SpreadCard(
                    modifier = Modifier.size(120.dp, 180.dp),
                )
                if (uiState.value !is ThreeCardState.FirstSelected) {
                    SpreadCard(
                        modifier = Modifier.size(120.dp, 180.dp),
                    )
                    if (uiState.value !is ThreeCardState.SecondSelected) {
                        SpreadCard(
                            modifier = Modifier.size(120.dp, 180.dp),
                        )
                    }
                }
            }
        }
    }

    if (uiState.value is ThreeCardState.Opening) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            AsyncImage(
                modifier = Modifier.size(120.dp, 180.dp),
                model = (uiState.value as ThreeCardState.Opening).imageUrls[0],
                contentScale = ContentScale.Fit,
                onSuccess = {
                    openCount++
                    checkOpening()
                },
                contentDescription = null,
            )
            AsyncImage(
                modifier = Modifier.size(120.dp, 180.dp),
                model = (uiState.value as ThreeCardState.Opening).imageUrls[1],
                contentScale = ContentScale.Fit,
                onSuccess = {
                    openCount++
                    checkOpening()
                },
                contentDescription = null,
            )
            AsyncImage(
                modifier = Modifier.size(120.dp, 180.dp),
                model = (uiState.value as ThreeCardState.Opening).imageUrls[2],
                contentScale = ContentScale.Fit,
                onSuccess = {
                    openCount++
                    checkOpening()
                },
                contentDescription = null,
            )
        }
    }
}

private sealed interface ThreeCardState {
    data object Ready : ThreeCardState
    data object FirstSelected : ThreeCardState
    data object SecondSelected : ThreeCardState
    data object ThirdSelected : ThreeCardState
    data class Opening(val imageUrls: List<String>) : ThreeCardState
}