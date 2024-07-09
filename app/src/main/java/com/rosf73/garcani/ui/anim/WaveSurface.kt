package com.rosf73.garcani.ui.anim

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp

@Composable
fun WaveSurface(
    modifier: Modifier = Modifier,
    initX: Float = 0f,
    waveWidth: Int = 1000,
    targetWaveHeight: Float = 50f,
    color: Color,
) {
    val animColor by animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(500, easing = LinearEasing),
        label = "",
    )

    val deltaXAnim = rememberInfiniteTransition(label = "")
    val dx by deltaXAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ),
        label = "",
    )

    val waveHeight by animateFloatAsState(
        targetValue = targetWaveHeight,
        animationSpec = TweenSpec(10000, easing = LinearEasing),
        label = "",
    )
    val originalY = 150f
    val path = Path()

    androidx.compose.foundation.Canvas(
        modifier = modifier.size(300.dp),
    ) {
        translate {
            drawPath(path = path, color = animColor)
            path.reset()
            val halfWaveWidth = waveWidth / 2
            path.moveTo(-waveWidth * 2 + (waveWidth * dx) + (waveWidth * initX), originalY.dp.toPx())

            for (i in -waveWidth..(size.width.toInt() + waveWidth) step waveWidth) {
                path.relativeQuadraticBezierTo(
                    halfWaveWidth.toFloat() / 2,
                    -waveHeight,
                    halfWaveWidth.toFloat(),
                    0f
                )
                path.relativeQuadraticBezierTo(
                    halfWaveWidth.toFloat() / 2,
                    waveHeight,
                    halfWaveWidth.toFloat(),
                    0f
                )
            }

            path.lineTo(size.width, size.height)
            path.lineTo(0f, size.height)
            path.close()
        }
    }
}