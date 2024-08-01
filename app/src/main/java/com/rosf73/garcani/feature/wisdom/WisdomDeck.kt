package com.rosf73.garcani.feature.wisdom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.ui.anim.animateFloatAsState
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.CardFrontGradient
import com.rosf73.garcani.ui.theme.CardGradient
import com.rosf73.garcani.ui.theme.PurpleGrey40
import com.rosf73.garcani.ui.theme.White
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Gemini pick random 10 quotes
@Composable
fun WisdomDeck(
    modifier: Modifier = Modifier,
    model: GenerativeModel,
    speech: suspend (String) -> Unit,
    onClose: () -> Unit,
) {
    val quoteList = remember { mutableStateListOf<String>() }
    var isDoneRequest = false

    val coroutineScope = rememberCoroutineScope()

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
                    Please select 30 short quotes of less than 60 characters
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
            onSelect = {
                coroutineScope.launch {
                    speech("\"$it\"")
                }
            },
            onClose = onClose,
        )
    }
}

@Composable
private fun CircularDeck(
    modifier: Modifier = Modifier,
    list: List<String>,
    initY: Float,
    targetY: Float,
    onSelect: (String) -> Unit,
    onClose: () -> Unit,
) {
    val upDuration = 2000
    val cardCount = 10

    val containerOffset by animateFloatAsState(
        initY,
        targetY,
        duration = upDuration,
    )

    val selectedCard = remember { mutableIntStateOf(-1) }

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = size.height * containerOffset
            },
    ) {
        repeat(cardCount) {
            WisdomCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.4f)
                    .aspectRatio(3f / 2f),
                totalCount = cardCount,
                index = it,
                isSelected = selectedCard.intValue == it,
                content = list[it],
                doBefore = {
                    delay(upDuration.toLong())
                },
                onClick = {
                    if (selectedCard.intValue < 0) {
                        selectedCard.intValue = it
                        onSelect(list[it])
                    } else if (selectedCard.intValue == it) {
                        onClose()
                    }
                },
            ) {
            }
        }
    }
}

@Composable
private fun WisdomCard(
    modifier: Modifier = Modifier,
    totalCount: Int,
    index: Int,
    isSelected: Boolean,
    content: String,
    doBefore: suspend () -> Unit = {},
    onClick: () -> Unit,
    doAfter: suspend () -> Unit = {},
) {
    val firstAngle = -135f
    val lastAngle = -45f

    val firstX = (-90).dp
    val lastX = -firstX

    val weight = remember { Animatable(1f) }

    val selectedOffsetX by animateDpAsState(
        targetValue = if (isSelected) 0.dp else lastX - (lastX - firstX) * weight.value,
        animationSpec = tween(if (isSelected) 800 else 0),
        label = "",
    )
    val selectedOffsetY by animateDpAsState(
        targetValue = if (isSelected) (-400).dp else 0.dp,
        animationSpec = tween(800),
        label = "",
    )

    val isDoneToSelect = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        doBefore()
        weight.animateTo(
            targetValue = (totalCount.toFloat() - index - 1) / (totalCount - 1),
            animationSpec = tween(1200),
        )
        doAfter()
    }

    LaunchedEffect(key1 = isSelected) {
        if (isSelected) {
            // do scale anim
            weight.animateTo(0.5f)
            isDoneToSelect.value = true
        }
    }

    GradientButton(
        modifier = modifier
            .offset(selectedOffsetX, selectedOffsetY)
            .rotate(if (isDoneToSelect.value) 0f else lastAngle - (lastAngle - firstAngle) * weight.value)
            .scale(if (isDoneToSelect.value) 2f else 1f)
            .border(
                width = 5.dp,
                color = White,
                shape = MaterialTheme.shapes.medium,
            ),
        containerColor = if (isDoneToSelect.value) CardFrontGradient else CardGradient,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = if (isDoneToSelect.value) 20.dp else 1.dp
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
    ) {
        if (isDoneToSelect.value) {
            Text(text = content, color = PurpleGrey40)
        }
    }
}