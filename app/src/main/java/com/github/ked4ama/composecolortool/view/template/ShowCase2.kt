package com.github.ked4ama.composecolortool.view.template

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme

@Composable
fun ConstraintLayoutScope.ShowCase2() {
    val (text, boldText, extraBoldText, textField) = createRefs()
    Text(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(text) {
                top.linkTo(parent.top)
            }
            .fillMaxWidth(),
        text = "Sample Text"
    )
    Text(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(boldText) {
                top.linkTo(text.bottom)
            }
            .fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        text = "Sample Text (bold)"
    )
    Text(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(extraBoldText) {
                top.linkTo(boldText.bottom)
            }
            .fillMaxWidth(),
        fontWeight = FontWeight.ExtraBold,
        text = "Sample Text (extra bold)"
    )
    var textFieldInput by remember { mutableStateOf("Sample Text Field") }
    TextField(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(textField) {
                top.linkTo(extraBoldText.bottom)
            }
            .fillMaxWidth(),
        value = textFieldInput, onValueChange = {
            textFieldInput = it
        })
}

@Preview(showBackground = true)
@Composable
fun ShowCase2Preview() {
    ComposeColorToolTheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            ShowCase2()
        }
    }
}