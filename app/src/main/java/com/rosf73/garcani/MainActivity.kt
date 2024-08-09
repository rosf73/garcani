package com.rosf73.garcani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import com.rosf73.garcani.feature.tarot.TarotViewModel
import com.rosf73.garcani.feature.wisdom.WisdomViewModel
import com.rosf73.garcani.ui.theme.GArcaniTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val wisdomViewModel by viewModels<WisdomViewModel>()
    private val tarotViewModel by viewModels<TarotViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.isCreated) {
            viewModel.setTTS(this)
            viewModel.setPreference(this)
            viewModel.greeting()
        }

        enableEdgeToEdge()
        setContent {
            GArcaniTheme {
                Greeting(
                    viewModel = viewModel,
                    wisdomViewModel = wisdomViewModel,
                    tarotViewModel = tarotViewModel,
                    modifier = Modifier,
                )
            }
        }
    }
}
