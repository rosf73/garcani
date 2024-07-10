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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.rosf73.garcani.ui.theme.Purple8099

@Composable
fun Greeting(
    model: GenerativeModel,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("Waiting...") }

    LaunchedEffect(key1 = model) {
        val prompt = """
            You are a fortune teller from now on.
            And... your name is GArcani.
        """.trimIndent()
        val response = model.generateContent(prompt)
        text = response.text ?: ""
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
                    text = text,
                )
            }

            CrystalBall(
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun SpeechBubble(
    modifier: Modifier = Modifier,
    text: String,
) {
    // TODO : add speech bubble with anim
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(0.8f)
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = text,
                textAlign = TextAlign.Center,
            )
        }
    }
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
            backgroundColor = Purple8099,
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
