package com.mymusic.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProgressBarComposable(enabled: Boolean) {
    if (enabled) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}