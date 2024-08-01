package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CelticCrossSpread(
    modifier: Modifier = Modifier,
    selectedCards: SnapshotStateMap<String, String>,
    onDoneSelecting: (List<String>) -> Unit,
    onInterpret: (List<String>) -> Unit,
) {
    val uiState = remember { mutableStateOf<CelticCrossState>(CelticCrossState.Ready) }
    var openCount = 0
    var isOpened by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedCards.size) {
        val cards = selectedCards.keys.toList()
        val urls = selectedCards.values.toList()

        if (selectedCards.size == 10) {
            delay(800)
            onDoneSelecting(cards)
            uiState.value = CelticCrossState.Opening(urls)
        }
    }

    fun checkOpening() {
        if (openCount == 10) {
            coroutineScope.launch {
                isOpened = true
                delay(1200)

                val cards = selectedCards.keys.toList()
                onInterpret(cards)
            }
        }
    }

    if (!isOpened) {
        Row(
            modifier = modifier,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
            ) {
                if (selectedCards.size >= 1)
                    SpreadCard(
                        modifier = Modifier
                            .size(100.dp, 150.dp)
                            .align(Alignment.Center),)
                if (selectedCards.size >= 2)
                    SpreadCard(
                        modifier = Modifier
                            .size(100.dp, 150.dp)
                            .align(Alignment.Center)
                            .rotate(90f),)
                if (selectedCards.size >= 3)
                    SpreadCard(
                        modifier = Modifier
                            .size(100.dp, 150.dp)
                            .align(Alignment.BottomCenter),)
                if (selectedCards.size >= 4)
                    SpreadCard(
                        modifier = Modifier
                            .size(100.dp, 150.dp)
                            .align(Alignment.CenterStart),)
                if (selectedCards.size >= 5)
                    SpreadCard(
                        modifier = Modifier
                            .size(100.dp, 150.dp)
                            .align(Alignment.TopCenter),)
                if (selectedCards.size >= 6)
                    SpreadCard(
                        modifier = Modifier
                            .size(100.dp, 150.dp)
                            .align(Alignment.CenterEnd),)
            }
            Column(modifier = Modifier.fillMaxHeight()) {
                if (selectedCards.size >= 7)
                    SpreadCard(
                        modifier = Modifier.size(100.dp, 150.dp),)
                if (selectedCards.size >= 8) {
                    Spacer(modifier = Modifier.height(10.dp))
                    SpreadCard(
                        modifier = Modifier.size(100.dp, 150.dp),)
                }
                if (selectedCards.size >= 9) {
                    Spacer(modifier = Modifier.height(10.dp))
                    SpreadCard(
                        modifier = Modifier.size(100.dp, 150.dp),)
                }
                if (selectedCards.size >= 10) {
                    Spacer(modifier = Modifier.height(10.dp))
                    SpreadCard(
                        modifier = Modifier.size(100.dp, 150.dp),)
                }
            }
        }
    }

    if (uiState.value is CelticCrossState.Opening) {
        Row(
            modifier = modifier,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
            ) {
                ImageCard(
                    modifier = Modifier.align(Alignment.Center),
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[0],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                ImageCard(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .rotate(90f),
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[1],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                ImageCard(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[2],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                ImageCard(
                    modifier = Modifier.align(Alignment.CenterStart),
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[3],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                ImageCard(
                    modifier = Modifier.align(Alignment.TopCenter),
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[4],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                ImageCard(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[5],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
            }
            Column(modifier = Modifier.fillMaxHeight()) {
                ImageCard(
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[6],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                ImageCard(
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[7],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                ImageCard(
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[8],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                ImageCard(
                    url = (uiState.value as CelticCrossState.Opening).imageUrls[9],
                    onSuccess = {
                        openCount++
                        checkOpening()
                    },
                )
            }
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    url: String,
    onSuccess: () -> Unit,
) {
    AsyncImage(
        modifier = modifier.size(100.dp, 150.dp),
        model = url,
        contentScale = ContentScale.Fit,
        onSuccess = {
            onSuccess()
        },
        contentDescription = null,
    )
}

private sealed interface CelticCrossState {
    data object Ready : CelticCrossState
    data object Selected : CelticCrossState
    data class Opening(val imageUrls: List<String>) : CelticCrossState
}