package com.github.ked4ama.composecolortool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme
import com.github.ked4ama.composecolortool.view.ColorSelectorSheet
import com.github.ked4ama.composecolortool.view.Pager
import com.github.ked4ama.composecolortool.view.PagerState

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ColorThemeViewModel = viewModel()
            ComposeColorToolTheme(darkTheme = viewModel.isDarkMode, viewModel = viewModel) {
                ColorSelectorSheet(viewModel = viewModel) { state ->
                    MainScaffold(state)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainScaffold(state: ModalBottomSheetState? = null) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    Icon(Icons.Default.Menu)
                },
                actions = {
                    IconButton(onClick = { state?.show() }) {
                        Image(
                            modifier = Modifier.size(32.dp),
                            imageVector = vectorResource(id = R.drawable.ic_color_palette),
                        )
                    }
                }
            )
        },
    ) {
        val clock = AmbientAnimationClock.current
        val pagerState = remember(clock) { PagerState(clock) }
        pagerState.maxPage = 3

        Pager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "text$page", modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeColorToolTheme {
        MainScaffold()
    }
}