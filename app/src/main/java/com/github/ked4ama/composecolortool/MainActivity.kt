package com.github.ked4ama.composecolortool

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme

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
    var colors = remember(viewModel.isDarkMode) {
        if (viewModel.isDarkMode) {
            viewModel.darkColors
        } else {
            viewModel.lightColors
        }
    }
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
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
                colorSelectorItem(title = "primary", color = colors.primary)
                colorSelectorItem(title = "primaryVariant", color = colors.primaryVariant)
                colorSelectorItem(title = "secondary", color = colors.secondary)
                colorSelectorItem(title = "secondaryVariant", color = colors.secondaryVariant)
                colorSelectorItem(title = "background", color = colors.background)
                colorSelectorItem(title = "surface", color = colors.surface)
                colorSelectorItem(title = "error", color = colors.error)
                colorSelectorItem(title = "onPrimary", color = colors.onPrimary)
                colorSelectorItem(title = "onSecondary", color = colors.onSecondary)
                colorSelectorItem(title = "onBackground", color = colors.onBackground)
                colorSelectorItem(title = "onSurface", color = colors.onSurface)
                colorSelectorItem(title = "onError", color = colors.onError)
            }
        }
    ) {
        content(state)
    }
}

private fun LazyListScope.colorSelectorItem(
    modifier: Modifier = Modifier,
    title: String,
    color: Color
) {
    item {
        ColorSelectorItemContent(modifier = modifier, title = title, color = color)
    }
}

@Composable
private fun ColorSelectorItemContent(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
) {
    val context = AmbientContext.current
    ListItem(
        modifier = modifier.clickable(onClick = {
            Toast.makeText(context, title, Toast.LENGTH_LONG).show()
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