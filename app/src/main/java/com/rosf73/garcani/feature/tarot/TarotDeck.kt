package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
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
        speech("Please tell me your concerns.\nThe more detailed the content, the deeper the interpretation.")
        delay(500)
        viewModel.updateQuestionTarotState()
    }

    LaunchedEffect(key1 = uiState) {
        if (uiState is TarotUiState.Ready && (uiState as TarotUiState.Ready).isFailure) {
            speech("Something is wrong with Gemini.\nPlease wait a moment and try again.")
            onClose()
        }
    }

    Box(
        modifier = modifier,
    ) {
        when (uiState) {
            is TarotUiState.Ready -> {}
            TarotUiState.Question -> { // 2. select spread
                Question(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    onDone = { question ->
                        coroutineScope.launch {
                            val response = viewModel.sendSpreadPrompt(model, question)
                            if (response.isNotBlank()) {
                                val (spread, reason) = viewModel.analyzeSpreadParagraph(response)
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
                Spread(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState as TarotUiState.Spread,
                    sendMessage = { viewModel.sendSpreadChat(model, it) }
                )
            }
            is TarotUiState.Interpretation -> { // 4. interpret of each card and comprehensive evaluation
                val interpretation = (uiState as TarotUiState.Interpretation).result
                Interpretation(
                    modifier = Modifier.align(Alignment.Center)
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.75f),
                    result = interpretation,
                    onClickClose = onClose,
                )
            }
        }
    }
}
