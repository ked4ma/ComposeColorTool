package com.github.ked4ama.composecolortool.view.template

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme

@Composable
fun ShowCase1(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier) {
        val fab = createRef()
        FloatingActionButton(
            modifier = Modifier.constrainAs(fab) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }.padding(end = 8.dp, bottom = 8.dp),
            onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowCase1Preview() {
    ComposeColorToolTheme {
        ShowCase1(Modifier.fillMaxSize())
    }
}