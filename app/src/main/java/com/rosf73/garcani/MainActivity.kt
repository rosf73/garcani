package com.rosf73.garcani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.rosf73.garcani.ui.theme.GArcaniTheme
import com.rosf73.garcani.ui.theme.Purple4099
import com.rosf73.garcani.ui.theme.Purple80
import com.rosf73.garcani.ui.theme.Purple8099

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = BuildConfig.apiKey
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey,
        )

        enableEdgeToEdge()
        setContent {
            GArcaniTheme {
                Greeting(
                    model = generativeModel,
                    modifier = Modifier,
                )
            }
        }
    }
}

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
//        val chat = model.startChat(
//            history = listOf(
//                content(role = "user") { text("") },
//                content(role = "model") { text("") }
//            )
//        )
//
//        val response = chat.sendMessage("")
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
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = text,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.TopCenter),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
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
//        WaveSurface(
//            modifier = Modifier.padding(it).fillMaxSize(),
//            initX = 0.35f,
//            color = Purple8099,
//        )
//        WaveSurface(
//            modifier = Modifier.padding(it).fillMaxSize(),
//            targetWaveHeight = 100f,
//            color = Purple80CC,
//        )
    }
}
