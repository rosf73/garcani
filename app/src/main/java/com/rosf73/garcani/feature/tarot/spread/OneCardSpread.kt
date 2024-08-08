package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rosf73.garcani.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OneCardSpread(
    modifier: Modifier = Modifier,
    selectedCard: Map.Entry<String, String>?,
    onDoneSelecting: (String) -> Unit,
    onInterpret: (String) -> Unit,
) {
    val uiState = remember { mutableStateOf<OneCardState>(OneCardState.Ready) }
    var isOpened by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedCard) {
        if (selectedCard != null) {
            uiState.value = OneCardState.Selecting
            delay(800)
            uiState.value = OneCardState.Opening(selectedCard.key, selectedCard.value) // as only 1 card is needed
            onDoneSelecting(selectedCard.key)
        }
    }

    if (uiState.value != OneCardState.Ready && !isOpened) {
        SpreadCard(
            modifier = modifier.size(120.dp, 180.dp),
        )
    }

    if (uiState.value is OneCardState.Opening) {
        val opening = uiState.value as OneCardState.Opening
        AsyncImage(
            modifier = modifier.size(120.dp, 180.dp),
            model = opening.imageUrl,
            contentScale = ContentScale.Fit,
            onSuccess = {
                isOpened = true
                coroutineScope.launch {
                    delay(1200)
                    onInterpret(selectedCard!!.key)
                }
            },
            contentDescription = stringResource(id = R.string.desc_tarot_card, opening.imageName)
        )
    }
}

private sealed interface OneCardState {
    data object Ready : OneCardState
    data object Selecting : OneCardState
    data class Opening(val imageName: String, val imageUrl: String) : OneCardState
}