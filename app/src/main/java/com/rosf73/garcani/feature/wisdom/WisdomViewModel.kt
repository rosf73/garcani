package com.rosf73.garcani.feature.wisdom

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WisdomViewModel : ViewModel() {

    val quoteList = mutableStateListOf<String>()
    private var isDoneRequest = false

    fun sendWisdomPrompt(
        model: GenerativeModel,
        speech: suspend (String) -> Unit,
        onError: () -> Unit,
    ) {
        viewModelScope.launch {
            awaitAll(
                async {
                    delay(1100)
                    if (!isDoneRequest) {
                        speech("I'm selecting 10 out of 30 sentences... Just a moment.") // TODO: temp
                    }
                },
                async {
                    try {
                        val prompt = """
                        Please select 10 short quotes of less than 60 characters
                        and write 10 lines in the format of [number]:[content] without any additional words.
                    """.trimIndent()
                        val response = model.generateContent(prompt)

                        isDoneRequest = true
                        if (response.text.isNullOrBlank()) { // fail
                            speech("Something is wrong with Gemini.\nPlease wait a moment and try again.")
                            onError()
                        } else { // success
                            quoteList.addAll(
                                response.text?.split("\n")?.map {
                                    it.split(":").last().trim()
                                } ?: emptyList()
                            )
                            speech("Alright! Now, take your pick.") // TODO: temp
                        }
                    } catch (e: com.google.ai.client.generativeai.type.GoogleGenerativeAIException) { // fail
                        isDoneRequest = true
                        speech("Something is wrong with Gemini.\nPlease wait a moment and try again.")
                        onError()
                    }
                }
            )
        }
    }
}