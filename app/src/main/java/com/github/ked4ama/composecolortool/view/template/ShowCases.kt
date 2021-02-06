package com.github.ked4ama.composecolortool.view.template

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun showCaseSize() = 1

@Composable
fun ShowCase(case: Case) {
    ShowCaseContainer {
        when (case) {
            Case.CARDS -> ShowCase1()
            Case.TEXTS -> ShowCase2()
            Case.BUTTONS -> ShowCase3()
        }
    }
}

@Composable
private fun ShowCaseContainer(content: @Composable ConstraintLayoutScope.() -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        content()
    }
}

enum class Case {
    CARDS, TEXTS, BUTTONS
}