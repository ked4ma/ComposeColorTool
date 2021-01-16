package com.github.ked4ama.composecolortool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.window.Dialog
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.picker.ColorPicker
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme
import java.util.*

class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorSelectorSheet { state ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Rest of the UI")
                    Spacer(Modifier.preferredHeight(20.dp))
                    Button(onClick = { state.show() }) {
                        Text("Click to show sheet")
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ColorSelectorSheet(
    modifier: Modifier = Modifier,
    viewModel: ColorThemeViewModel = viewModel(),
    content: @Composable (ModalBottomSheetState) -> Unit
) {
    val colors = remember(viewModel.isDarkMode) {
        if (viewModel.isDarkMode) {
            viewModel.darkColors
        } else {
            viewModel.lightColors
        }
    }
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    Column {
        ModalBottomSheetLayout(
            modifier = modifier,
            sheetState = state,
            sheetContent = {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Dark Mode")
                            Switch(
                                checked = viewModel.isDarkMode,
                                onCheckedChange = {
                                    viewModel.isDarkMode = it
                                }
                            )
                        }
                    }
                    val setShowDialog: (Boolean, String) -> Unit = { show, key ->
                        viewModel.showDialog = show to key
                    }
                    viewModel.colorKeys.forEach { (key, prop) ->
                        colorSelectorItem(
                            title = key,
                            color = prop.get(colors),
                            setShowDialog = setShowDialog
                        )
                    }
                }
            }
        ) {
            content(state)
        }
        ColorPickerDialog(
            initColor = (viewModel.colorKeys[viewModel.showDialog.second]?.get(colors)
                ?: Color.White).toArgb(),
            showDialog = viewModel.showDialog,
            setShowDialog = { show, key ->
                viewModel.showDialog = show to key
            }
        )
    }
}

private fun LazyListScope.colorSelectorItem(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
    setShowDialog: (Boolean, String) -> Unit,
) {
    item {
        ColorSelectorItemContent(modifier, title, color, setShowDialog)
    }
}

@Composable
private fun ColorSelectorItemContent(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
    setShowDialog: (Boolean, String) -> Unit,
) {
    val context = AmbientContext.current
    ListItem(
        modifier = modifier.clickable(onClick = {
            Toast.makeText(context, title, Toast.LENGTH_LONG).show()
            setShowDialog(true, title)
        }),
        text = {
            Row {
                Text(
                    "#%6X".format(color.toArgb()),
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    text = title
                )
            }
        },
        icon = {
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    )
}

@Composable
fun ColorPickerDialog(
    initColor: Int,
    showDialog: Pair<Boolean, String>,
    setShowDialog: (Boolean, String) -> Unit
) {
    if (!showDialog.first) return
    var selectedColor by remember { mutableStateOf(initColor) }
    Dialog(onDismissRequest = {
        setShowDialog(false, showDialog.second)
    }) {
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
                Button(modifier = Modifier.fillMaxSize().weight(1F), onClick = {
                    setShowDialog(false, showDialog.second)
                }) {
                    Text(text = "Cancel")
                }
                Button(modifier = Modifier.fillMaxSize().weight(1F), onClick = {

                }) {
                    Text(text = "OK")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeColorToolTheme {
        Greeting("Android")
    }
}