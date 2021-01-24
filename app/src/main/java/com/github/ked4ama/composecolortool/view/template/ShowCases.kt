package com.github.ked4ama.composecolortool.view.template

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun showCaseSize() = 1

@Composable
fun ShowCase(case: Int) {
    val modifier = Modifier.fillMaxSize()
    when (case) {
        0 -> ShowCase1(modifier)
    }
}