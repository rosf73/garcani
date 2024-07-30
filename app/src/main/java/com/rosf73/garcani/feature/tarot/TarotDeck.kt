package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.ai.client.generativeai.GenerativeModel
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
                                    viewModel.updateSpreadTarotState(spread)
                                    speech(reason)
                                }
                            }
                        }
                    }
                )
            }
            is TarotUiState.Spread -> { // 3. waiting for selecting cards
                when ((uiState as TarotUiState.Spread).type) {
                    SpreadType.ONE_CARD ->
                        OneCardSpread(
                            modifier = Modifier.fillMaxSize(),
                        ) { card ->

                        }
                    SpreadType.THREE_CARD ->
                        ThreeCardSpread(
                            modifier = Modifier.fillMaxSize(),
                        ) { first, second, third ->

                        }
                    SpreadType.CELTIC_CROSS ->
                        CelticCrossSpread(
                            modifier = Modifier.fillMaxSize(),
                        ) { cardList ->

                        }
                }
            }
            is TarotUiState.Interpretation -> { // 4. interpret of each card and comprehensive evaluation

            }
        }
    }
}
