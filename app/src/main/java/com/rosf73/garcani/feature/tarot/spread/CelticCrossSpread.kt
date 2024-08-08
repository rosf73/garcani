package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rosf73.garcani.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CelticCrossSpread(
    modifier: Modifier = Modifier,
    selectedCards: SnapshotStateList<Pair<String, String>>,
    onDoneSelecting: (List<String>) -> Unit,
    onInterpret: (List<String>) -> Unit,
) {
    val openList by remember { mutableStateOf(Array(10) { false }) }

    val coroutineScope = rememberCoroutineScope()

    fun checkOpening() {
        if (openList.all { it }) {
            onDoneSelecting(selectedCards.map { it.first })

            coroutineScope.launch {
                delay(1200)

                onInterpret(selectedCards.map { it.first })
            }
        }
    }

    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        ) {
            selectedCards.toList().getOrNull(0)?.let { (card, url) ->
                ImageCard(
                    modifier = Modifier.align(Alignment.Center),
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[0] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(1)?.let { (card, url) ->
                ImageCard(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .rotate(90f),
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[1] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(2)?.let { (card, url) ->
                ImageCard(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[2] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(3)?.let { (card, url) ->
                ImageCard(
                    modifier = Modifier.align(Alignment.CenterStart),
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[3] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(4)?.let { (card, url) ->
                ImageCard(
                    modifier = Modifier.align(Alignment.TopCenter),
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[4] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(5)?.let { (card, url) ->
                ImageCard(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[5] = true
                        checkOpening()
                    },
                )
            }
        }
        Column(modifier = Modifier.fillMaxHeight()) {
            selectedCards.toList().getOrNull(6)?.let { (card, url) ->
                ImageCard(
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[6] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(7)?.let { (card, url) ->
                Spacer(modifier = Modifier.height(10.dp))
                ImageCard(
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[7] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(8)?.let { (card, url) ->
                Spacer(modifier = Modifier.height(10.dp))
                ImageCard(
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[8] = true
                        checkOpening()
                    },
                )
            }
            selectedCards.toList().getOrNull(9)?.let { (card, url) ->
                Spacer(modifier = Modifier.height(10.dp))
                ImageCard(
                    card = card,
                    url = url,
                    onSuccess = {
                        openList[9] = true
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
    card: String,
    url: String,
    onSuccess: () -> Unit,
) {
    var isOpened by remember { mutableStateOf(false) }

    Box(modifier = modifier.size(100.dp, 150.dp)) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            model = url,
            error = painterResource(id = R.drawable.img_error_card),
            contentScale = ContentScale.Fit,
            onSuccess = {
                isOpened = true
                onSuccess()
            },
            contentDescription = stringResource(id = R.string.desc_tarot_card, card)
        )
        if (!isOpened) {
            SpreadCard(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
            )
        }
    }
}