package com.rosf73.garcani.ui.anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember

@Composable
fun animateFloatAsState(
    init: Float,
    target: Float,
    easing: Easing = FastOutSlowInEasing,
    duration: Int,
    doBefore: suspend () -> Unit = {},
    doAfter: suspend () -> Unit = {},
): State<Float> {
    val animation = remember { Animatable(init) }

    LaunchedEffect(init) {
        doBefore()
        animation.animateTo(
            targetValue = target,
            animationSpec = TweenSpec(duration, easing = easing),
        )
        doAfter()
    }
    return animation.asState()
}