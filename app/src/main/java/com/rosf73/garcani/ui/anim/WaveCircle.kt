package com.rosf73.garcani.ui.anim

import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.ui.core.innerShadow
import com.rosf73.garcani.ui.theme.WhiteCC
import kotlin.random.Random

private const val WAVE_COUNT = 5

@Composable
fun WaveCircle(
    modifier: Modifier = Modifier,
    color: Color,
    backgroundColor: Color,
) {
    val configuration = LocalConfiguration.current
    val drawSize = configuration.screenWidthDp.dp.value

    val waveHeight = 700f
    val path = Path()

    val colors = mutableListOf<Color>()
    val hWeight = mutableListOf<Float>()
    val pointWeight1 = mutableListOf<Int>()
    val pointWeight2 = mutableListOf<Int>()
    val pointWeight3 = mutableListOf<Int>()
    val pointWeight4 = mutableListOf<Int>()
    val durations = mutableListOf<Int>()
    repeat(WAVE_COUNT) {
        val randAlpha = (Random.Default.nextInt(80) + 20).toFloat() / 100
        colors.add(color.copy(alpha = randAlpha))
        val randHeight = Random.Default.nextInt(651)
        hWeight.add(waveHeight - randHeight)
        val randPoint1 = Random.Default.nextInt(50) - 25
        pointWeight1.add(randPoint1)
        val randPoint2 = Random.Default.nextInt(50) - 25
        pointWeight2.add(randPoint2)
        val randPoint3 = Random.Default.nextInt(50) - 25
        pointWeight3.add(randPoint3)
        val randPoint4 = Random.Default.nextInt(50) - 25
        pointWeight4.add(randPoint4)
        val randDuration = Random.Default.nextInt(5000) + 4000
        durations.add(randDuration)
    }

    val deltaAnim = rememberInfiniteTransition(label = "")
    val d1 by deltaAnim.animateFloat(
        initialValue = -hWeight[0],
        targetValue = drawSize + waveHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(durations[0], easing = LinearEasing)
        ),
        label = "",
    )
    val d2 by deltaAnim.animateFloat(
        initialValue = -hWeight[1],
        targetValue = drawSize + waveHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(durations[1], easing = LinearEasing)
        ),
        label = "",
    )
    val d3 by deltaAnim.animateFloat(
        initialValue = -hWeight[2],
        targetValue = drawSize + waveHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(durations[2], easing = LinearEasing)
        ),
        label = "",
    )
    val d4 by deltaAnim.animateFloat(
        initialValue = -hWeight[3],
        targetValue = drawSize + waveHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(durations[3], easing = LinearEasing)
        ),
        label = "",
    )
    val d5 by deltaAnim.animateFloat(
        initialValue = -hWeight[4],
        targetValue = drawSize + waveHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(durations[4], easing = LinearEasing)
        ),
        label = "",
    )

    Canvas(
        modifier = modifier.innerShadow(
            color = MaterialTheme.colorScheme.tertiary,
            blur = 30.dp,
            cornersRadius = 150.dp,
            offsetX = 0.5.dp,
            offsetY = 0.5.dp,
        ),
    ) {
        val circlePath = Path().apply {
            addOval(
                Rect(
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width / 2f,
                )
            )
        }
        drawPath(path = circlePath, brush = Brush.linearGradient(colors = listOf(WhiteCC, backgroundColor)))

        clipPath(path = circlePath, clipOp = ClipOp.Intersect) {
            repeat(WAVE_COUNT) { i ->
                wave(
                    path, colors[i], hWeight[i],
                    pointWeight1[i], pointWeight2[i], pointWeight3[i], pointWeight4[i],
                    targetPosition = when (i) {
                        0 -> d1
                        1 -> d2
                        2 -> d3
                        3 -> d4
                        else -> d5
                    },
                )
            }
        }
    }
}

private fun DrawScope.wave(
    path: Path,
    color: Color,
    height: Float,
    point1Weight: Int,
    point2Weight: Int,
    point3Weight: Int,
    point4Weight: Int,
    targetPosition: Float,
) {
    translate {
        drawPath(path = path, color = color)
        path.reset()
        path.moveTo(0f, targetPosition)

        path.cubicTo(
            size.width / 3, targetPosition + point1Weight,
            size.width / 3 * 2, -height / 2 + targetPosition + point2Weight,
            size.width, -height + targetPosition,
        )
        path.lineTo(size.width, targetPosition)
        path.cubicTo(
            size.width / 3 * 2, height / 2 + targetPosition + point3Weight,
            size.width / 3, targetPosition + point4Weight,
            0f, height + targetPosition,
        )

        path.close()
    }
}
