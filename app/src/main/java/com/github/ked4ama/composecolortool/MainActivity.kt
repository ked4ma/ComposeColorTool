package com.github.ked4ama.composecolortool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme
import com.github.ked4ama.composecolortool.view.ColorSelectorSheet

class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ColorThemeViewModel = viewModel()
            ComposeColorToolTheme(darkTheme = viewModel.isDarkMode, viewModel = viewModel) {
                ColorSelectorSheet(viewModel = viewModel) { state ->
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