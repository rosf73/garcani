package com.rosf73.garcani.feature.thought

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.ui.anim.animateFloatAsState
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.White
import kotlinx.coroutines.delay

// Gemini pick random 10 quotes
@Composable
fun Deck(
    modifier: Modifier = Modifier,
    model: GenerativeModel,
) {
    // anim
    val init = remember { mutableFloatStateOf(1.5f) }
    val target = remember { mutableFloatStateOf(0f) }

    CircularDeck(
        modifier = modifier,
        initOffset = init.floatValue,
        targetOffset = target.floatValue,
    )
}

@Composable
private fun CircularDeck(
    modifier: Modifier = Modifier,
    initOffset: Float,
    targetOffset: Float,
) {
    val upDuration = 2000
    val cardCount = 10

    val containerOffset by animateFloatAsState(
        initOffset,
        targetOffset,
        duration = upDuration,
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = size.height * containerOffset
            },
    ) {
        repeat(cardCount) {
            ThoughtCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.4f)
                    .aspectRatio(3f / 2f),
                totalCount = cardCount,
                index = it,
                doBefore = {
                    delay(upDuration.toLong())
                }
            )
        }
    }
}

@Composable
private fun ThoughtCard(
    modifier: Modifier = Modifier,
    totalCount: Int,
    index: Int,
    doBefore: suspend () -> Unit = {},
) {
    val firstValue = -135f
    val lastValue = -45f
    val angle = remember { Animatable(firstValue) }

    LaunchedEffect(key1 = true) {
        doBefore()
        angle.animateTo(
            targetValue = lastValue - (lastValue - firstValue) / (totalCount - 1) * (totalCount - index - 1),
            animationSpec = tween(1200),
        )
    }

    GradientButton(
        modifier = modifier
            .rotate(angle.value)
            .border(
                width = 5.dp,
                color = White,
                shape = MaterialTheme.shapes.medium,
            ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = MaterialTheme.shapes.medium,
        onClick = {},
    ) {

    }
}