package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.feature.tarot.spread.CelticCrossSpread
import com.rosf73.garcani.feature.tarot.spread.OneCardSpread
import com.rosf73.garcani.feature.tarot.spread.ThreeCardSpread
import com.rosf73.garcani.localdata.Tarot
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.CardDisableGradient
import com.rosf73.garcani.ui.theme.CardGradient
import com.rosf73.garcani.ui.theme.White

@Composable
fun Spread(
    modifier: Modifier = Modifier,
    uiState: TarotUiState.Spread,
    sendMessage: (String) -> Unit,
) {
    val cards = if (uiState.type == SpreadType.ONE_CARD) Tarot.majors
                else Tarot.data

    val selectedCards = remember { mutableStateMapOf<String, String>() }
    var isDoneSelection by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        when (uiState.type) {
            SpreadType.ONE_CARD -> {
                OneCardSpread(
                    modifier = Modifier.align(Alignment.Center),
                    selectedCard = selectedCards.entries.firstOrNull(),
                    onDoneSelecting = {
                        isDoneSelection = true
                    },
                    onInterpret = { card ->
                        sendMessage("""
                            I drew "$card" card.
                            Please briefly interpret it in relation to [question].
                            Add a line break at the end of each sentence.
                        """.trimIndent())
                    }
                )
            }
            SpreadType.THREE_CARD -> {
                ThreeCardSpread(
                    modifier = Modifier.align(Alignment.Center),
                    selectedCards = selectedCards,
                    onDoneSelecting = { _, _, _ ->
                        isDoneSelection = true
                    },
                    onInterpret = { first, second, third ->
                        sendMessage("""
                            I drew "$first" first, "$second" second, and "$third" card third.
                            After briefly interpreting each card in relation to [question],
                            kindly write a comprehensive evaluation.
                            Add a line break at the end of each sentence.
                        """.trimIndent())
                    }
                )
            }
            SpreadType.CELTIC_CROSS -> {
                CelticCrossSpread(
                    modifier = Modifier.align(Alignment.Center),
                    selectedCards = selectedCards,
                    onDoneSelecting = { _ ->
                        isDoneSelection = true
                    },
                    onInterpret = { cardList ->
                        sendMessage("""
                            I drew the cards $cardList in order.
                            After briefly interpreting each card in relation to [question],
                            kindly write a comprehensive evaluation.
                            Add a line break at the end of each sentence.
                        """.trimIndent())
                    }
                )
            }
        }

        if (!isDoneSelection) {
            CardPack(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                cardList = cards.keys.toList().shuffled(),
                onSelect = {
                    selectedCards[it] = cards[it]!!
                }
            )
        }
    }
}

@Composable
private fun CardPack(
    modifier: Modifier = Modifier,
    cardList: List<String>,
    onSelect: (String) -> Unit,
) {
    val selectedList = remember { mutableStateListOf<String>() }

    LazyRow(
        modifier = modifier,
    ) {
        items(cardList, key = { it }) { data ->
            Spacer(modifier = Modifier.width(5.dp))
            Card(
                modifier = Modifier
                    .size(100.dp, 150.dp),
                enabled = !selectedList.contains(data),
                onClick = {
                    selectedList.add(data)
                    onSelect(data)
                },
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Composable
private fun Card(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    GradientButton(
        modifier = modifier
            .border(
                width = 5.dp,
                color = White,
                shape = MaterialTheme.shapes.extraSmall,
            ),
        containerColor = if (enabled) CardGradient else CardDisableGradient,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 5.dp
        ),
        enabled = enabled,
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onClick,
    ) {
    }
}
