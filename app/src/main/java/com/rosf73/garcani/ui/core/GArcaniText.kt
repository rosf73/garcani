package com.rosf73.garcani.ui.core

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun GArcaniText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
    )
}