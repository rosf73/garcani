package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun Question(
    modifier: Modifier = Modifier,
    onDone: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .imePadding()
            .padding(15.dp),
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
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        modifier = modifier.focusRequester(focusRequester),
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
