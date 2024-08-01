package com.rosf73.garcani

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.ServerException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _deckState = MutableStateFlow<DeckUiState>(DeckUiState.Ready)
    val deckState = _deckState.asStateFlow()

    val generativeModel: GenerativeModel

    init {
        val apiKey = BuildConfig.apiKey
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey,
            safetySettings = listOf(
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
            )
        )
    }

    fun clearDeckState() {
        _deckState.value = DeckUiState.Ready
    }

    fun updateOdysseyDeckState() {
        _deckState.value = DeckUiState.OdysseyDeck
    }

    fun updateWisdomDeckState() {
        _deckState.value = DeckUiState.WisdomDeck
    }

    fun updateTarotDeckState() {
        _deckState.value = DeckUiState.TarotDeck
    }

    suspend fun sendPrompt(prompt: String): Pair<String, Boolean> {
        var resultMsg = ""
        var result = false

        try {
            val response = generativeModel.generateContent(prompt)
            if (response.text.isNullOrBlank()) {
                resultMsg = "There was something wrong with the response.\nPlease wait a moment and try again."
            } else {
                resultMsg = response.text!!
                result = true
            }
        } catch (e: com.google.ai.client.generativeai.type.GoogleGenerativeAIException) {
            resultMsg = "Something is wrong with Gemini.\nPlease wait a moment and try again."
        }

        if (resultMsg.isBlank()) {
            resultMsg = "There was something wrong with the response.\nPlease wait a moment and try again."
        }
        return resultMsg to result
    }
}

sealed interface DeckUiState {
    data object Ready : DeckUiState
    data object OdysseyDeck : DeckUiState
    data object WisdomDeck : DeckUiState
    data object TarotDeck : DeckUiState
}
