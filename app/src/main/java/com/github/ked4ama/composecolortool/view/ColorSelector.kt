package com.github.ked4ama.composecolortool.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.picker.ColorPickerDialog

@ExperimentalMaterialApi
@Composable
fun ColorSelectorSheet(
    modifier: Modifier = Modifier,
    viewModel: ColorThemeViewModel = viewModel(),
    content: @Composable (ModalBottomSheetState) -> Unit
) {
    val colors = remember(viewModel.getColors()) { viewModel.getColors() }
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
            },
            setSelectedColor = { key, color ->
                viewModel.setColor(key, color)
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
