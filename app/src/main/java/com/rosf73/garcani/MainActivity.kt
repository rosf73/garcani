package com.rosf73.garcani

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import com.rosf73.garcani.feature.tarot.TarotViewModel
import com.rosf73.garcani.ui.theme.GArcaniTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val tarotViewModel by viewModels<TarotViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setTTS(this)

        enableEdgeToEdge()
        setContent {
            GArcaniTheme {
                Greeting(
                    viewModel = viewModel,
                    tarotViewModel = tarotViewModel,
                    modifier = Modifier,
                )
            }
        }
    }
}
