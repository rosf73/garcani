package com.rosf73.garcani

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.ui.anim.Shadow
import com.rosf73.garcani.ui.anim.WaveCircle
import com.rosf73.garcani.ui.theme.Purple4099
import com.rosf73.garcani.ui.theme.Purple80
import com.rosf73.garcani.ui.theme.Purple80CC
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Greeting(
    model: GenerativeModel,
    modifier: Modifier = Modifier,
) {
    val textList = remember { mutableStateListOf("...") }
    var isDoneToSpeech by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = model) {
        val prompt = """
            You are a fortune teller from now on.
            And... your name is GArcani.
            Please add a line break at the end of every sentence.
        """.trimIndent()
        val response = model.generateContent(prompt)
        val lines = response.text?.replace("!", ".")?.split(Regex("\n"))
        lines?.forEachIndexed { i, line ->
            if (line.isNotBlank() && line.trim().isNotEmpty()) {
                textList.add(line)
                // time for reading
                if (lines.lastIndex != i) {
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
        isDoneToSpeech = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter),
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                SpeechBubble(
                    textList = textList,
                )
            }

            CrystalBall(
                modifier = Modifier
                    .align(Alignment.Center),
            )

            if (isDoneToSpeech) {
                Odyssey(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .aspectRatio(3f / 2f),
                )
            }
        }
    }
}

@Composable
private fun SpeechBubble(
    modifier: Modifier = Modifier,
    textList: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    // TODO : add speech bubble with anim
    Column(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(180.dp),
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

@Composable
private fun GArcaniText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun CrystalBall(
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        WaveCircle(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f),
            color = Purple80,
            backgroundColor = Purple80CC,
        )
        Spacer(modifier = Modifier.height(30.dp))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(60.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            val aspectRatio = maxWidth / maxHeight
            Shadow(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(maxOf(aspectRatio, 1f), maxOf(1 / aspectRatio, 1f)),
                color = Purple4099,
            )
        }
    }
}
