package com.github.ked4ama.composecolortool.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.picker.ColorPickerDialog
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme

@OptIn(ExperimentalMaterialApi::class)
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

data class ColorDescriptionData(val key: String, val alpha: Float = 1F, val color: Color)

@Composable
fun ColorDescription(modifier: Modifier = Modifier, vararg descriptions: ColorDescriptionData) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFDD))
            .border(2.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Text(text = "Used Colors Information:", color = Color.Black)
        Column(modifier = Modifier.align(Alignment.End)) {
            descriptions.forEach { desc ->
                ColorDescriptionItem(description = desc)
            }
        }
    }
}

@Composable
private fun ColorDescriptionItem(
    modifier: Modifier = Modifier,
    description: ColorDescriptionData
) {
    val adjustedAlpha = description.alpha.coerceIn(0F, 1F)
    val adjustedColor = description.color.copy(alpha = adjustedAlpha)
    Row(modifier = modifier.padding(4.dp)) {
        Box(
            Modifier.align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .background(adjustedColor)
            )
        }
        Box(
            Modifier.align(Alignment.CenterVertically).padding(start = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row {
                Text(
                    "#%6X".format(adjustedColor.toArgb()),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = if (adjustedAlpha == 1F) {
                        description.key
                    } else {
                        "${description.key}, alpha: $adjustedAlpha"
                    },
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorDescriptionPreView() {
    ComposeColorToolTheme {
        ColorDescription(
            modifier = Modifier,
            ColorDescriptionData("test1", alpha = 1F, color = Color.Blue),
            ColorDescriptionData("test2", alpha = 0.5F, color = Color.Blue),
            ColorDescriptionData("test3", alpha = 0.12F, color = Color.Green),
        )
    }
}
