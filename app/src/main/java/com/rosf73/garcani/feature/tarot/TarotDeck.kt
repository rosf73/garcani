package com.rosf73.garcani.feature.tarot

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
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
    val uiState = viewModel.tarotState.collectAsState()
    val selectedList = remember { mutableStateListOf<String>() }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = model) {
        // 1. question to user
        speech("Please ask your question in sentences of at least 10 characters.")
        delay(500)
        viewModel.updateQuestionTarotState()

        // 2. select spread

        // 3. waiting for selecting cards

        // 4. interpret of each card

        // 5. say comprehensive evaluation
    }

    Box(
        modifier = modifier,
    ) {
        when (uiState.value) {
            TarotUiState.Ready -> {}
            TarotUiState.Question -> {
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
                                Log.d("testest", res)
                                val (spread, reason) = viewModel.analyzeSpreadParagraph(res)
                                if (spread == null) {
                                    Log.d("testest", "sentence is wrong. $question")
                                } else {
                                    viewModel.updateSpreadTarotState(spread)
                                    speech(reason)
                                }
                            }
                        }
                    }
                )
            }
            is TarotUiState.Spread -> {

            }
            TarotUiState.Interpretation -> {

            }
        }
    }
}

@Composable
private fun Question(
    modifier: Modifier = Modifier,
    onDone: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(15.dp),
    ) {
        // TODO : mic

        QuestionField(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            onTextChanged = {
                text = it
            },
            onDone = {
                onDone(text)
                text = ""
            },
        )
    }
}

@Composable
private fun QuestionField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onDone: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = text,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDone()
            },
        ),
        onValueChange = onTextChanged,
    )
}
