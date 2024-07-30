package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.localdata.Tarot
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.CardDisableGradient
import com.rosf73.garcani.ui.theme.CardGradient
import com.rosf73.garcani.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TarotDeck(
    modifier: Modifier = Modifier,
    model: GenerativeModel,
    viewModel: TarotViewModel,
    speech: suspend (String) -> Unit,
    onClose: () -> Unit,
) {
    val uiState by viewModel.tarotState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = model) {
        // 1. question to user
        speech("Please ask your question in sentences of at least 10 characters.")
        delay(500)
        viewModel.updateQuestionTarotState()
    }

    Box(
        modifier = modifier,
    ) {
        when (uiState) {
            TarotUiState.Ready -> {}
            TarotUiState.Question -> { // 2. select spread
                Question(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    onDone = { question ->
                        coroutineScope.launch {
                            val prompt = """
                                You are a Tarot expert.
                                Understand the [Questions] below and select one of the “One Card”, “Three Card” or “Celtic Cross” spreads and mark them after [Spread].
                                And after [Reason], briefly state the reason for your choice in relation to the [Question].
                                Also if the question is not a complete sentence, write “None” after [Spread].
                                [Question]: $question
                                [Spread]:
                                [reason]:
                            """.trimIndent() // TODO : chat style?
                            val response = model.generateContent(prompt)
                            response.text?.let { res ->
                                val (spread, reason) = viewModel.analyzeSpreadParagraph(res)
                                if (spread == null) {
                                    if (reason.isBlank()) {
                                        speech("I'm having trouble understanding your question.")
                                    } else {
                                        speech(reason)
                                    }
                                } else {
                                    viewModel.addSpreadPrompt(prompt)
                                    viewModel.addSpreadResponse(res)
                                    viewModel.updateSpreadTarotState(spread)
                                    speech(reason)
                                }
                            }
                        }
                    }
                )
            }
            is TarotUiState.Spread -> { // 3. waiting for selecting cards
                val cards: Map<String, String>
                when ((uiState as TarotUiState.Spread).type) {
                    SpreadType.ONE_CARD -> {
                        cards = Tarot.majors
                        OneCardSpread(
                            modifier = Modifier.fillMaxSize(),
                            cards = cards,
                        ) { card ->
                            coroutineScope.launch {
                                val chat = model.startChat(history = viewModel.history)
                                val response = chat.sendMessage("""
                                    I drew "$card" card.
                                    Please briefly interpret it in relation to [question].
                                    Add a line break at the end of each sentence.
                                """.trimIndent())
                                if (!response.text.isNullOrBlank()) {
                                    viewModel.updateInterpretationTarotState(response.text!!)
                                } else {
                                    // retry
                                }
                            }
                        }
                    }
                    SpreadType.THREE_CARD -> {
                        cards = Tarot.data
                        ThreeCardSpread(
                            modifier = Modifier.fillMaxSize(),
                            cards = cards,
                        ) { first, second, third ->
                            coroutineScope.launch {
                                val chat = model.startChat(history = viewModel.history)
                                val response = chat.sendMessage("""
                                    I drew "$first" first, "$second" second, and "$third" card third.
                                    After briefly interpreting each card in relation to [question],
                                    kindly write a comprehensive evaluation.
                                    Add a line break at the end of each sentence.
                                """.trimIndent())
                                if (!response.text.isNullOrBlank()) {
                                    viewModel.updateInterpretationTarotState(response.text!!)
                                } else {
                                    // retry
                                }
                            }
                        }
                    }
                    SpreadType.CELTIC_CROSS -> {
                        cards = Tarot.data
                        CelticCrossSpread(
                            modifier = Modifier.fillMaxSize(),
                            cards = cards,
                        ) { cardList ->
                            coroutineScope.launch {
                                val chat = model.startChat(history = viewModel.history)
                                val response = chat.sendMessage("""
                                    I drew the cards $cardList in order.
                                    After briefly interpreting each card in relation to [question],
                                    kindly write a comprehensive evaluation.
                                    Add a line break at the end of each sentence.
                                """.trimIndent())
                                if (!response.text.isNullOrBlank()) {
                                    viewModel.updateInterpretationTarotState(response.text!!)
                                } else {
                                    // retry
                                }
                            }
                        }
                    }
                }
                CardPack(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    cardList = cards.keys.toList(),
                )
            }
            is TarotUiState.Interpretation -> { // 4. interpret of each card and comprehensive evaluation
                val interpretation = (uiState as TarotUiState.Interpretation).result
                Interpretation(
                    modifier = Modifier.fillMaxSize(),
                    result = interpretation,
                )
            }
        }
    }
}

@Composable
private fun CardPack(
    modifier: Modifier = Modifier,
    cardList: List<String>,
) {
    val selectedList = remember { mutableStateListOf<Int>() }

    LazyRow(
        modifier = modifier,
    ) {
        itemsIndexed(cardList, key = { _, it -> it }) { i, data ->
            Card(
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .offset(x = (-20).dp * i),
                data = data,
                enabled = !selectedList.contains(i),
                onClick = {
                    selectedList.add(i)
                },
            )
        }
    }
}

@Composable
private fun Card(
    modifier: Modifier = Modifier,
    data: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    GradientButton(
        modifier = modifier
            .border(
                width = 5.dp,
                color = White,
                shape = MaterialTheme.shapes.medium,
            ),
        containerColor = if (enabled) CardGradient else CardDisableGradient,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 5.dp
        ),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
    ) {
    }
}
