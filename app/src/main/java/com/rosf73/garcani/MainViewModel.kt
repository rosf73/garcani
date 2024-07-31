package com.rosf73.garcani

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
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
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.LOW_AND_ABOVE),
            )
        )
    }

    fun clearDeckState() {
        _deckState.value = DeckUiState.Ready
    }

    fun updateOdysseyDeckState() {
        _deckState.value = DeckUiState.OdysseyDeck
    }

    fun updateThoughtDeckState() {
        _deckState.value = DeckUiState.ThoughtDeck
    }

    fun updateTarotDeckState() {
        _deckState.value = DeckUiState.TarotDeck
    }
}

sealed interface DeckUiState {
    data object Ready : DeckUiState
    data object OdysseyDeck : DeckUiState
    data object ThoughtDeck : DeckUiState
    data object TarotDeck : DeckUiState
}
