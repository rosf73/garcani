package com.rosf73.garcani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.ui.theme.GArcaniTheme

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
