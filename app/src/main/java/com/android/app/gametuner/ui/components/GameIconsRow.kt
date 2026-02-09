package com.android.app.gametuner.ui.components

import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.android.app.gametuner.LocalMainActivity
import com.android.app.gametuner.MainActivity

@androidx.compose.runtime.Composable
fun GameIconsRow(
    installedGames: List<PackageInfo>,
    selectedGamePackage: MutableState<String>,
    gameScrollState: MutableState<Int>
) {
    val context = LocalMainActivity.current

    // Create a custom scroll state and pass it to Row
    val scrollState = remember { ScrollState(gameScrollState.value) }

    // Save the scroll state whenever it changes
    LaunchedEffect(scrollState.value) {
        gameScrollState.value = scrollState.value
    }

    if (installedGames.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No games available", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(18.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            installedGames.forEach { game ->
                val appIcon = context.packageManager.getApplicationIcon(game.applicationInfo)
                val isSelected = selectedGamePackage.value == game.packageName
                GameIcon(appIcon, isSelected) {
                    selectedGamePackage.value = game.packageName
                }
            }
        }
    }
}

annotation class Composable

@androidx.compose.runtime.Composable
@Composable
fun GameIcon(icon: Drawable, isSelected: Boolean, onClick: () -> Unit) {
    val scale = if (isSelected) 1.2f else 1f
    val alpha = if (isSelected) 1f else 0.5f

    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(8.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        val bitmap = icon.toBitmap()
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Game Icon",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
        )
    }
}
