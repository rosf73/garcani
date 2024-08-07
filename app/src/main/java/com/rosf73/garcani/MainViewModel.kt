package com.rosf73.garcani

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.rosf73.garcani.localdata.SharedPreference
import com.rosf73.garcani.localdata.Speech
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel : ViewModel() {

    val textList = mutableStateListOf(Speech(msg = "..."))

    private val _deckState = MutableStateFlow<DeckUiState>(DeckUiState.Ready)
    val deckState = _deckState.asStateFlow()

    val generativeModel: GenerativeModel

    private var preference: SharedPreference? = null
    private var tts: TextToSpeech? = null

    private var lastSentence = ""

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

    fun greeting() {
        viewModelScope.launch {
            val greeting = getGreeting()

            val response: String
            val isSuccess: Boolean
            if (greeting.isNotBlank()) { // visited
                val result = sendGreetingChat(greeting)
                response = result.first
                isSuccess = result.second
            } else {
                val result = sendGreetingPrompt()
                response = result.first
                isSuccess = result.second

                if (isSuccess) {
                    setGreeting(response)
                }
            }

            textList.clear()
            val lines = response
                .replace("!", ".")
                .split(Regex("\n"))
            speechEachLine(lines)

            if (isSuccess) {
                updateOdysseyDeckState()
            }
        }
    }

    suspend fun speechEachLine(list: List<String>) {
        list.forEachIndexed { i, line ->
            if (line.isNotBlank() && line.trim().isNotEmpty()) {
                textList.add(Speech(msg = line))
                speak(line)
                // time for reading
                if (list.lastIndex != i) {
                    if (line.length > 50) {
                        delay(5000)
                    } else if (line.length > 30) {
                        delay(4000)
                    } else {
                        delay(3000)
                    }
                }
            }
        }
    }

    fun setPreference(context: Context) {
        preference = SharedPreference(context)
    }

    fun setGreeting(greeting: String) {
        preference?.setGreeting(greeting)
    }

    fun setSoundOn(soundOn: Boolean) {
        preference?.setSound(soundOn)
        if (!soundOn) {
            tts?.stop()
        } else {
            tts?.speak(lastSentence, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun getGreeting() = preference?.getGreeting() ?: ""
    fun getSoundOn() = preference?.getSound() ?: false

    fun setTTS(context: Context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                setTTSSetting()
            }
        }
    }

    private fun setTTSSetting() = tts?.apply {
        language = Locale.ENGLISH
        voice = voices?.firstOrNull { voice ->
            voice.name.contains("en-US", ignoreCase = true)
                    && voice.quality == Voice.QUALITY_HIGH
        } ?: defaultVoice
        setPitch(0.8f)
    }

    fun speak(message: String) {
        lastSentence = message
        if (getSoundOn()) {
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        }
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

    suspend fun sendGreetingPrompt(): Pair<String, Boolean> {
        val prompt = """
            You are a fortune teller from now on.
            And... your name is GArcani.
            You have the simple Wisdom Cards, which can provide direction for resolving your concerns, and the more complex Tarot Cards.
            Please add a line break at the end of every sentence.
            First talk about destiny,
            then describe each function in two lines or less and ask the other person to choose which one they want.
        """.trimIndent()

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

    suspend fun sendGreetingChat(answer: String): Pair<String, Boolean> {
        val prompt = """
            You are a fortune teller from now on.
            And... your name is GArcani.
            You have the simple Wisdom Cards, which can provide direction for resolving your concerns, and the more complex Tarot Cards.
            Please add a line break at the end of every sentence.
            First talk about destiny,
            then describe each function in two lines or less and ask the other person to choose which one they want.
        """.trimIndent()

        var resultMsg = ""
        var result = false

        try {
            val chat = generativeModel.startChat(
                history = listOf(
                    content(role = "user") { text(prompt) },
                    content(role = "model") { text(answer) }
                )
            )
            val response = chat.sendMessage("Hey, I'm back again. Let's say hello briefly.")
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

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        super.onCleared()
    }
}

sealed interface DeckUiState {
    data object Ready : DeckUiState
    data object OdysseyDeck : DeckUiState
    data object WisdomDeck : DeckUiState
    data object TarotDeck : DeckUiState
}
