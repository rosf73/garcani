package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rosf73.garcani.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ThreeCardSpread(
    modifier: Modifier = Modifier,
    selectedCards: SnapshotStateList<Pair<String, String>>,
    onDoneSelecting: (String, String, String) -> Unit,
    onInterpret: (String, String, String) -> Unit,
) {
    var isOpened1 by remember { mutableStateOf(false) }
    var isOpened2 by remember { mutableStateOf(false) }
    var isOpened3 by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    fun checkOpening() {
        if (isOpened1 && isOpened2 && isOpened3) {
            onDoneSelecting(selectedCards[0].first, selectedCards[1].first, selectedCards[2].first)

            coroutineScope.launch {
                delay(1200)

                onInterpret(selectedCards[0].first, selectedCards[1].first, selectedCards[2].first)
                isOpened1 = false
                isOpened2 = false
                isOpened3 = false
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        selectedCards.toList().getOrNull(0)?.let { (card, url) ->
            ImageCard(
                modifier = Modifier.size(120.dp, 180.dp),
                card = card,
                url = url,
                onSuccess = {
                    isOpened1 = true
                    checkOpening()
                }
            )
        }
        selectedCards.toList().getOrNull(1)?.let { (card, url) ->
            ImageCard(
                modifier = Modifier.size(120.dp, 180.dp),
                card = card,
                url = url,
                onSuccess = {
                    isOpened2 = true
                    checkOpening()
                }
            )
        }
        selectedCards.toList().getOrNull(2)?.let { (card, url) ->
            ImageCard(
                modifier = Modifier.size(120.dp, 180.dp),
                card = card,
                url = url,
                onSuccess = {
                    isOpened3 = true
                    checkOpening()
                }
            )
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier,
    card: String,
    url: String,
    onSuccess: () -> Unit,
) {
    var isOpened by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            model = url,
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