package com.rosf73.garcani

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.ui.core.GradientButton

@Composable
fun Odyssey(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp),
    ) {
        GradientButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            onClick = { /*TODO*/ },
        ) {
            Text(text = "Thought Card")
        }

        Spacer(modifier = Modifier.width(15.dp))

        GradientButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            onClick = { /*TODO*/ },
        ) {
            Text(text = "Tarot")
        }
    }
}
