package com.github.ked4ama.composecolortool.view.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme

@Composable
fun ConstraintLayoutScope.ShowCase3() {
    val (button, textButton, iconButton, outlinedButton, radioButton) = createRefs()
    Button(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(button) {
                top.linkTo(parent.top)
            }
            .fillMaxWidth(),
        onClick = {
            // do nothing
        }) {
        Text(text = "Sample Button")
    }
    TextButton(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(textButton) {
                top.linkTo(button.bottom)
            }
            .fillMaxWidth(),
        onClick = {
            // do nothing
        }) {
        Text(text = "Sample Text Button")
    }
    IconButton(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(iconButton) {
                top.linkTo(textButton.bottom)
            }
            .fillMaxWidth(),
        onClick = {
            // do nothing
        }) {
        Icon(imageVector = Icons.Default.Check)
    }
    OutlinedButton(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(outlinedButton) {
                top.linkTo(iconButton.bottom)
            }
            .fillMaxWidth(),
        onClick = {
            // do nothing
        }) {
        Text(text = "Sample Outlined Button")
    }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .constrainAs(radioButton) {
                top.linkTo(outlinedButton.bottom)
            }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        var selected by remember { mutableStateOf(0) }
        RadioButton(selected = selected == 0, onClick = {
            if (selected != 0) selected = 0
        })
        RadioButton(selected = selected == 1, onClick = {
            if (selected != 1) selected = 1
        })
        RadioButton(selected = selected == 2, onClick = {
            if (selected != 2) selected = 2
        })
    }
}

@Preview(showBackground = true)
@Composable
fun ShowCase3Preview() {
    ComposeColorToolTheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            ShowCase3()
        }
    }
}