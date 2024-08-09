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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import com.rosf73.garcani.ui.anim.animateFloatAsState
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.CardFrontGradient
import com.rosf73.garcani.ui.theme.CardGradient
import com.rosf73.garcani.ui.theme.PurpleGrey40
import com.rosf73.garcani.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Gemini pick random 10 quotes
@Composable
fun WisdomDeck(
    modifier: Modifier = Modifier,
    model: GenerativeModel,
    viewModel: WisdomViewModel,
    speech: suspend (String) -> Unit,
    onClose: () -> Unit,
) {
    val quoteList = viewModel.quoteList

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (quoteList.isEmpty()) {
            viewModel.sendWisdomPrompt(model, speech, onClose)
        }
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

    var enabled by remember { mutableStateOf(false) }

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
                doAfter = { enabled = true },
                enabled = enabled,
            )
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
    enabled: Boolean,
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
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
    ) {
        if (isDoneToSelect.value) {
            val size = if (content.length > 80) 12.sp
                    else if (content.length > 60) 13.sp
                    else TextUnit.Unspecified
            Text(text = content, color = PurpleGrey40, fontSize = size, overflow = TextOverflow.Visible)
        }
    }
}