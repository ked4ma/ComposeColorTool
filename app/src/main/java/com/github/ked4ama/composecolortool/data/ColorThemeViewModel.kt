package com.github.ked4ama.composecolortool.data

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.ked4ama.composecolortool.ui.purple200
import com.github.ked4ama.composecolortool.ui.purple500
import com.github.ked4ama.composecolortool.ui.purple700
import com.github.ked4ama.composecolortool.ui.teal200

class ColorThemeViewModel : ViewModel() {
    val colorKeys = linkedMapOf(
        "primary" to Colors::primary,
        "primaryVariant" to Colors::primaryVariant,
        "secondary" to Colors::secondary,
        "secondaryVariant" to Colors::secondaryVariant,
        "background" to Colors::background,
        "surface" to Colors::surface,
        "error" to Colors::error,
        "onPrimary" to Colors::onPrimary,
        "onSecondary" to Colors::onSecondary,
        "onBackground" to Colors::onBackground,
        "onSurface" to Colors::onSurface,
        "onError" to Colors::onError,
    )
    var lightColors by mutableStateOf(
        lightColors(
            primary = purple500,
            primaryVariant = purple700,
            secondary = teal200
        )
    )
    var darkColors by mutableStateOf(
        darkColors(
            primary = purple200,
            primaryVariant = purple700,
            secondary = teal200
        )
    )
    var isDarkMode by mutableStateOf(false)
    var showDialog by mutableStateOf(false to "")
}