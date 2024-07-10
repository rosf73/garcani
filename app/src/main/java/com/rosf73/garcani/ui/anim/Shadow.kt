package com.rosf73.garcani.ui.anim

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.rosf73.garcani.ui.theme.BlackCC
import com.rosf73.garcani.ui.theme.Transparent

@Composable
fun Shadow(
    modifier: Modifier,
    color: Color? = null,
) {
    Canvas(modifier = modifier) {
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(color ?: BlackCC, Transparent),
            ),
        )
    }
}
