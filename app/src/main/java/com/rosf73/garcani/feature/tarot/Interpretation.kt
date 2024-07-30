package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Interpretation(
    modifier: Modifier = Modifier,
    result: String,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = "Result")

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = result)
    }
}