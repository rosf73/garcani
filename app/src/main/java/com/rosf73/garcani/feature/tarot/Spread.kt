package com.rosf73.garcani.feature.tarot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rosf73.garcani.localdata.Tarot

@Composable
fun OneCardSpread(
    modifier: Modifier = Modifier,
    cards: Map<String, String>,
    onDone: (String) -> Unit,
) {
}

@Composable
fun ThreeCardSpread(
    modifier: Modifier = Modifier,
    cards: Map<String, String>,
    onDone: (String, String, String) -> Unit,
) {
}

@Composable
fun CelticCrossSpread(
    modifier: Modifier = Modifier,
    cards: Map<String, String>,
    onDone: (List<String>) -> Unit,
) {
}
