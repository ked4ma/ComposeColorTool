package com.github.ked4ama.composecolortool.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawerRow(title: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) {
        MaterialTheme.colors.primary.copy(alpha = 0.12F)
    } else {
        Color.Transparent
    }
    val textColor = if (selected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface
    }
    ListItem(
        modifier = Modifier
            .clickable(onClick = {
                if (!selected) {
                    onClick()
                }
            })
            .background(background)
    ) {
        Text(text = title, color = textColor)
    }
}