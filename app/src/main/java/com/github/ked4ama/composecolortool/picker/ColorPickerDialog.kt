package com.github.ked4ama.composecolortool.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme
import java.util.*


@Composable
fun ColorPickerDialog(
    initColor: Int,
    showDialog: Pair<Boolean, String>,
    setShowDialog: (Boolean, String) -> Unit,
    setSelectedColor: (String, Int) -> Unit = { _, _ -> }
) {
    if (!showDialog.first) return
    Dialog(onDismissRequest = {
        setShowDialog(false, showDialog.second)
    }) {
        ColorPickerDialogContent(initColor, showDialog, setShowDialog, setSelectedColor)
    }
}

@Composable
private fun ColorPickerDialogContent(
    initColor: Int,
    showDialog: Pair<Boolean, String>,
    setShowDialog: (Boolean, String) -> Unit,
    setSelectedColor: (String, Int) -> Unit = { _, _ -> }
) {
    var selectedColor by remember { mutableStateOf(initColor) }
    Column(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = "#%6x".format(selectedColor).toUpperCase(Locale.getDefault()),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            fontWeight = FontWeight.Bold
        )
        ColorPicker(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            initColor = initColor,
            onColorSelected = {
                selectedColor = it
            })
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth().height(40.dp)) {
            Button(
                modifier = Modifier.fillMaxSize().weight(1F),
                shape = RectangleShape,
                onClick = {
                    setShowDialog(false, showDialog.second)
                }
            ) {
                Text(text = "Cancel")
            }
            Button(
                modifier = Modifier.fillMaxSize().weight(1F),
                shape = RectangleShape,
                onClick = {
                    setSelectedColor(showDialog.second, selectedColor)
                    setShowDialog(false, showDialog.second)
                }
            ) {
                Text(text = "OK")
            }
        }
    }
}

@Preview
@Composable
fun ColorPickerDialogPreview() {
    ComposeColorToolTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            ColorPickerDialogContent(
                initColor = Color.White.toArgb(),
                showDialog = true to "primary",
                setShowDialog = { _, _ -> }
            )
        }
    }
}