package com.rosf73.garcani.feature.thought

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay

// Gemini pick random 10 quotes
@Composable
fun Deck(
    modifier: Modifier = Modifier,
    model: GenerativeModel,
    speech: suspend (String) -> Unit,
) {
    val quoteList = remember { mutableStateListOf<String>() }
    var isDoneRequest = false
    LaunchedEffect(key1 = model) {
        awaitAll(
            async {
                delay(1600)
                if (!isDoneRequest) {
                    speech("I'm selecting 10 out of 30 sentences... Just a moment.") // TODO: temp
                }
            },
            async {
                val prompt = """
                    Please select 30 short quotes
                    and write 30 lines in the format of [number]:[content] without any additional words.
                """.trimIndent()
                val response = model.generateContent(prompt)
                isDoneRequest = true
                quoteList.addAll(
                    response.text?.split("\n")?.map {
                        it.split(":").last().trim()
                    } ?: emptyList()
                )
                speech("Alright! Now, take your pick.") // TODO: temp
            }
        )
    }

    // anim
    val init = remember { mutableFloatStateOf(1.5f) }
    val target = remember { mutableFloatStateOf(0f) }

    if (quoteList.isNotEmpty()) {
        CircularDeck(
            modifier = modifier,
            list = quoteList,
            initY = init.floatValue,
            targetY = target.floatValue,
        )
    }
}

@Composable
private fun CircularDeck(
    modifier: Modifier = Modifier,
    list: List<String>,
    initY: Float,
    targetY: Float,
) {
    val upDuration = 2000
    val cardCount = 10

    val containerOffset by animateFloatAsState(
        initY,
        targetY,
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
                },
            ) {
                Text(text = list[it])
            }
        }
    }
}

@Composable
private fun ThoughtCard(
    modifier: Modifier = Modifier,
    totalCount: Int,
    index: Int,
    doBefore: suspend () -> Unit = {},
    doAfter: suspend () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val firstAngle = -135f
    val lastAngle = -45f

    val firstX = (-90).dp
    val lastX = -firstX

    val weight = remember { Animatable(1f) }

    LaunchedEffect(key1 = true) {
        doBefore()
        weight.animateTo(
            targetValue = (totalCount.toFloat() - index - 1) / (totalCount - 1),
            animationSpec = tween(1200),
        )
        doAfter()
    }

    GradientButton(
        modifier = modifier
            .offset(x = lastX - (lastX - firstX) * weight.value)
            .rotate(lastAngle - (lastAngle - firstAngle) * weight.value)
            .border(
                width = 5.dp,
                color = White,
                shape = MaterialTheme.shapes.medium,
            ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = MaterialTheme.shapes.medium,
        onClick = {},
    ) {
        content()
    }
}