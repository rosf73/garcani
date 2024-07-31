package com.rosf73.garcani.feature.tarot.spread

import androidx.compose.foundation.border
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.ui.core.GradientButton
import com.rosf73.garcani.ui.theme.CardFrontGradient
import com.rosf73.garcani.ui.theme.CardGradient
import com.rosf73.garcani.ui.theme.White

@Composable
fun SpreadCard(
    modifier: Modifier,
    isOpened: Boolean,
    content: @Composable () -> Unit = {},
) {
    GradientButton(
        modifier = modifier
            .border(
                width = 5.dp,
                color = White,
                shape = MaterialTheme.shapes.medium,
            ),
        containerColor = if (isOpened) CardFrontGradient else CardGradient,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 10.dp
        ),
        enabled = false,
        shape = MaterialTheme.shapes.medium,
        onClick = {},
    ) {
        content()
    }
}