package com.rosf73.garcani.feature.tarot

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TarotViewModel : ViewModel() {

    private val _tarotState = MutableStateFlow<TarotUiState>(TarotUiState.Ready)
    val tarotState = _tarotState.asStateFlow()

    fun clearTarotState() {
        _tarotState.value = TarotUiState.Ready
    }

    fun updateQuestionTarotState() {
        _tarotState.value = TarotUiState.Question
    }

    fun updateSpreadTarotState(type: SpreadType) {
        _tarotState.value = TarotUiState.Spread(type)
    }

    fun updateInterpretationTarotState(result: String) {
        _tarotState.value = TarotUiState.Interpretation(result)
    }

    fun analyzeSpreadParagraph(paragraph: String): Pair<SpreadType?, String> {
        var spread: SpreadType? = null
        var reason = ""
        paragraph.split("\n").forEach { line ->
            val lowerCase = line.lowercase()

            if (lowerCase.contains("[spread]")) {
                val content = lowerCase
                    .replaceFirst("[spread]", "")
                    .replace(":", "")
                    .trim()
                spread = if (content.contains("one card")) {
                    SpreadType.ONE_CARD
                } else if (content.contains("three card")) {
                    SpreadType.THREE_CARD
                } else if (content.contains("celtic cross")) {
                    SpreadType.CELTIC_CROSS
                } else {
                    null
                }
            }

            if (lowerCase.contains("[reason]")) {
                val content = lowerCase
                    .replaceFirst("[reason]", "")
                    .replace(":", "")
                    .trim()
                reason = content
            }
        }

        return spread to reason
    }
}

sealed interface TarotUiState {
    data object Ready : TarotUiState
    data object Question : TarotUiState
    data class Spread(val type: SpreadType) : TarotUiState
    data class Interpretation(val result: String) : TarotUiState
}

enum class SpreadType {
    ONE_CARD, THREE_CARD, CELTIC_CROSS,
}
