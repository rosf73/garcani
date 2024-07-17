package com.rosf73.garcani

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun Odyssey(
    modifier: Modifier = Modifier,
) {
    val offsetY1 = remember { Animatable(-5f) }
    val offsetY2 = remember { Animatable(-5f) }
    LaunchedEffect(key1 = offsetY1) {
        offsetY1.animateTo(
            0.5f,
            animationSpec = TweenSpec(2000),
        )
    }
    LaunchedEffect(key1 = offsetY2) {
        delay(200)
        offsetY2.animateTo(
            0.5f,
            animationSpec = TweenSpec(2000),
        )
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
    ) {
        OdysseyCard(
            modifier = Modifier.weight(1f),
            offset = offsetY1.value,
        ) {
            Text(text = "Thought Card")
        }

        Spacer(modifier = Modifier.width(15.dp))

        OdysseyCard(
            modifier = Modifier.weight(1f),
            offset = offsetY2.value,
        ) {
            Text(text = "Tarot")
        }
    }
}

@Composable
private fun OdysseyCard(
    modifier: Modifier = Modifier,
    offset: Float,
    content: @Composable () -> Unit,
) {
    GradientButton(
        modifier = modifier
            .graphicsLayer {
                translationY = size.height * offset
            }
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