package com.github.ked4ama.composecolortool.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
        Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
            Spacer(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .background(Color(selectedColor))
                    .align(Alignment.CenterVertically)
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "#%6x".format(selectedColor).toUpperCase(Locale.getDefault()),
                fontWeight = FontWeight.Bold
            )
        }
        ColorPicker(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            initColor = initColor,
            onColorSelected = {
                selectedColor = it
            })
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(175, 183, 255)
                ),
                onClick = {
                    setShowDialog(false, showDialog.second)
                }
            ) {
                Text(text = "Cancel", color = Color.Gray)
            }
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(98, 114, 255),
                ),
                onClick = {
                    setSelectedColor(showDialog.second, selectedColor)
                    setShowDialog(false, showDialog.second)
                }
            ) {
                Text(text = "OK", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun ColorPickerDialogPreview() {
    ComposeColorToolTheme(darkTheme = true) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ColorPickerDialogContent(
                initColor = Color.Cyan.toArgb(),
                showDialog = true to "primary",
                setShowDialog = { _, _ -> }
            )
        }
    }
}