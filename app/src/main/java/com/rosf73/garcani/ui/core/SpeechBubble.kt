package com.rosf73.garcani.ui.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SpeechBubble(
    modifier: Modifier = Modifier,
    textList: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    // TODO : add speech bubble with anim
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollState,
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            items(textList, key = { it }) { item ->
                GArcaniText(text = item)
            }

            coroutineScope.launch {
                scrollState.animateScrollToItem(textList.size)
            }
        }
    }
}