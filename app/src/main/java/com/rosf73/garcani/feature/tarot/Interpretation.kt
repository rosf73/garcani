package com.rosf73.garcani.feature.tarot

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rosf73.garcani.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun Interpretation(
    modifier: Modifier = Modifier,
    result: String,
    onClickClose: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val localContext = LocalContext.current
    val clipboardManager = localContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    Column(
        modifier = modifier
            .shadow(10.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
            .verticalScroll(scrollState),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_gemini),
                contentDescription = stringResource(id = R.string.desc_gemini)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = "Result",
                style = MaterialTheme.typography.titleLarge,
            )

            IconButton(onClick = {
                val clip = ClipData.newPlainText("GArcani", result)
                clipboardManager.setPrimaryClip(clip)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(id = R.string.desc_copy)
                )
            }
            IconButton(onClick = onClickClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(id = R.string.desc_close)
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        MarkdownText(
            markdown = result,
        )
    }
}
