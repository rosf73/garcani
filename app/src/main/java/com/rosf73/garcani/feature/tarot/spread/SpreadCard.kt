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
    content: @Composable () -> Unit = {},
) {
    GradientButton(
        modifier = modifier
            .border(
                width = 4.dp,
                color = White,
                shape = MaterialTheme.shapes.extraSmall,
            ),
        containerColor = CardGradient,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 10.dp
        ),
        enabled = false,
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {},
    ) {
        content()
    }
}