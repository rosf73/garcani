package com.rosf73.garcani

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.ui.anim.animateFloatAsState
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun Odyssey(
    modifier: Modifier = Modifier,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
) {
    val init1 = remember { mutableFloatStateOf(-5f) }
    val init2 = remember { mutableFloatStateOf(-5f) }
    val target1 = remember { mutableFloatStateOf(0.5f) }
    val target2 = remember { mutableFloatStateOf(0.5f) }
    var isLeftFast = true
    val isClicked = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
    ) {
        OdysseyCard(
            modifier = Modifier.weight(1f),
            initOffset = init1.floatValue,
            targetOffset = target1.floatValue,
            duration = if (isClicked.value) 600 else 2000,
            doBefore = { if (!isLeftFast) delay(200) },
            onClick = {
                // 위로 살짝
                init1.floatValue = 0.5f
                target1.floatValue = 0.35f
                init2.floatValue = 0.5f
                target2.floatValue = 0.35f

                isLeftFast = true
                isClicked.value = true
            },
            doAfter = if (isClicked.value) {{
                // 아래로 사라지기
                init1.floatValue = 0.35f
                target1.floatValue = 1.5f
                init2.floatValue = 0.35f
                target2.floatValue = 1.5f

                delay(600)
                if (isLeftFast) onClickLeft()
            }} else {{}}
        ) {
            Text(text = "Thought Card")
        }

        Spacer(modifier = Modifier.width(15.dp))

        OdysseyCard(
            modifier = Modifier.weight(1f),
            initOffset = init2.floatValue,
            targetOffset = target2.floatValue,
            duration = if (isClicked.value) 600 else 2000,
            doBefore = { if (isLeftFast) delay(200) },
            onClick = {
                // 위로 살짝
                init1.floatValue = 0.5f
                target1.floatValue = 0.35f
                init2.floatValue = 0.5f
                target2.floatValue = 0.35f

                isLeftFast = false
                isClicked.value = true
            },
            doAfter = if (isClicked.value) {{
                // 아래로 사라지기
                init1.floatValue = 0.35f
                target1.floatValue = 1.5f
                init2.floatValue = 0.35f
                target2.floatValue = 1.5f

                delay(600)
                if (!isLeftFast) onClickRight()
            }} else {{}}
        ) {
            Text(text = "Tarot")
        }
    }
}

@Composable
private fun OdysseyCard(
    modifier: Modifier = Modifier,
    initOffset: Float,
    targetOffset: Float,
    easing: Easing = FastOutSlowInEasing,
    duration: Int = 2000,
    doBefore: suspend () -> Unit = {},
    doAfter: suspend () -> Unit = {},
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val offset by animateFloatAsState(
        initOffset,
        targetOffset,
        easing,
        duration,
        doBefore = doBefore,
        doAfter = doAfter,
    )

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
        onClick = onClick,
    ) {
        content()
    }
}