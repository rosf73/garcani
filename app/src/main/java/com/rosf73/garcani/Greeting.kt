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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.feature.tarot.TarotDeck
import com.rosf73.garcani.feature.tarot.TarotViewModel
import com.rosf73.garcani.feature.wisdom.WisdomDeck
import com.rosf73.garcani.localdata.Speech
import com.rosf73.garcani.ui.anim.Shadow
import com.rosf73.garcani.ui.anim.WaveCircle
import com.rosf73.garcani.ui.core.SpeechBubble
import com.rosf73.garcani.ui.theme.Purple80CC
import kotlinx.coroutines.delay

@Composable
fun Greeting(
    viewModel: MainViewModel,
    tarotViewModel: TarotViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.deckState.collectAsState()
    val model = viewModel.generativeModel

    val textList = remember { mutableStateListOf(Speech(msg = "...")) }

    suspend fun List<String>.speechEachLine() {
        forEachIndexed { i, line ->
            if (line.isNotBlank() && line.trim().isNotEmpty()) {
                textList.add(Speech(msg = line))
                viewModel.speak(line)
                // time for reading
                if (lastIndex != i) {
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
    }

    LaunchedEffect(key1 = model) {
        val greeting = viewModel.getGreeting()

        val response: String
        val isSuccess: Boolean
        if (greeting.isNotBlank()) { // visited
            val result = viewModel.sendGreetingChat(greeting)
            response = result.first
            isSuccess = result.second
        } else {
            val result = viewModel.sendGreetingPrompt()
            response = result.first
            isSuccess = result.second

            if (isSuccess) {
                viewModel.setGreeting(response)
            }
        }

        textList.clear()
        response
            .replace("!", ".")
            .split(Regex("\n"))
            .speechEachLine()

        if (isSuccess) {
            viewModel.updateOdysseyDeckState()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            SoundSetting(
                modifier = Modifier.align(Alignment.TopEnd),
                initialValue = viewModel.getSoundOn(),
                setValue = { on -> viewModel.setSoundOn(on) },
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter),
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                SpeechBubble(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(180.dp),
                    textList = textList,
                )
            }

            CrystalBall(
                modifier = Modifier
                    .align(Alignment.Center),
            )

            if (uiState == DeckUiState.OdysseyDeck) {
                Odyssey(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .aspectRatio(3f / 2f),
                    uiState = uiState,
                    onClickLeft = {
                        viewModel.updateWisdomDeckState()
                    },
                    onClickRight = {
                        viewModel.updateTarotDeckState()
                    }
                )
            }

            if (uiState == DeckUiState.WisdomDeck) {
                WisdomDeck(
                    model = model,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    speech = { text ->
                        text.split("\n").speechEachLine()
                    },
                    onClose = {
                        viewModel.updateOdysseyDeckState()
                    }
                )
            }

            if (uiState == DeckUiState.TarotDeck) {
                TarotDeck(
                    model = model,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    viewModel = tarotViewModel,
                    speech = { text ->
                        text.split("\n").speechEachLine()
                    },
                    onClose = {
                        viewModel.updateOdysseyDeckState()
                    }
                )
            }
        }
    }
}

@Composable
private fun SoundSetting(
    modifier: Modifier = Modifier,
    initialValue: Boolean,
    setValue: (Boolean) -> Unit,
) {
    var soundOn by remember { mutableStateOf(initialValue) }

    if (soundOn) {
        IconButton(modifier = modifier, onClick = {
            setValue(false)
            soundOn = false
        }) {
            Icon(painter = painterResource(id = R.drawable.ic_volume_on), contentDescription = null)
        }
    } else {
        IconButton(modifier = modifier,onClick = {
            setValue(true)
            soundOn = true
        }) {
            Icon(painter = painterResource(id = R.drawable.ic_volume_off), contentDescription = null)
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
            color = MaterialTheme.colorScheme.secondary,
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
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
