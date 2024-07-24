package com.rosf73.garcani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.ui.theme.GArcaniTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            GArcaniTheme {
                Greeting(
                    viewModel = viewModel,
                    modifier = Modifier,
                )
            }
        }
    }
}
