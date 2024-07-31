package com.rosf73.garcani.feature.tarot

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Interpretation(
    modifier: Modifier = Modifier,
    result: String,
    onClickClose: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .shadow(10.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
            .verticalScroll(scrollState),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = "Result",
                style = MaterialTheme.typography.titleLarge,
            )
            IconButton(onClick = onClickClose) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = result)
    }
}
