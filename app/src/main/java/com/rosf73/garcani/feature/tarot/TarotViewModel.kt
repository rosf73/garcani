package com.rosf73.garcani.feature.tarot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TarotViewModel : ViewModel() {

    private val _tarotState = MutableStateFlow<TarotUiState>(TarotUiState.Ready(false))
    val tarotState = _tarotState.asStateFlow()

    private val _history = mutableListOf<Content>()
    val history: List<Content> = _history

    fun clearTarotState(isFailure: Boolean) {
        _tarotState.value = TarotUiState.Ready(isFailure)
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

    fun addSpreadPrompt(prompt: String) {
        _history.add(content(role = "user") { text(prompt) })
    }

    fun addSpreadResponse(response: String) {
        _history.add(content(role = "model") { text(response) })
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

    suspend fun sendSpreadPrompt(model: GenerativeModel, question: String): String {
        var result = ""
        try {
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

            if (response.text.isNullOrBlank()) { // fail
                clearTarotState(true)
            } else { // success
                addSpreadPrompt(prompt)
                addSpreadResponse(response.text!!)
                result = response.text!!
            }
        } catch (e: com.google.ai.client.generativeai.type.GoogleGenerativeAIException) { // fail
            clearTarotState(true)
        }

        return result
    }

    fun sendSpreadChat(model: GenerativeModel, message: String) {
        viewModelScope.launch {
            try {
                val chat = model.startChat(history = history)
                val response = chat.sendMessage(message)

                if (response.text.isNullOrBlank()) { // fail
                    clearTarotState(true)
                } else { // success
                    updateInterpretationTarotState(response.text!!)
                }
            } catch (e: com.google.ai.client.generativeai.type.GoogleGenerativeAIException) { // fail
                clearTarotState(true)
            }
        }
    }
}

sealed interface TarotUiState {
    data class Ready(val isFailure: Boolean) : TarotUiState
    data object Question : TarotUiState
    data class Spread(val type: SpreadType) : TarotUiState
    data class Interpretation(val result: String) : TarotUiState
}

enum class SpreadType {
    ONE_CARD, THREE_CARD, CELTIC_CROSS,
}
