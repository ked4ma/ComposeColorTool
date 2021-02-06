package com.github.ked4ama.composecolortool

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.github.ked4ama.composecolortool.data.ColorThemeViewModel
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme
import com.github.ked4ama.composecolortool.view.ColorSelectorSheet
import com.github.ked4ama.composecolortool.view.DrawerRow
import com.github.ked4ama.composecolortool.view.template.Case
import com.github.ked4ama.composecolortool.view.template.ShowCase
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ColorThemeViewModel = viewModel()
            ComposeColorToolTheme(darkTheme = viewModel.isDarkMode, viewModel = viewModel) {
                ColorSelectorSheet(viewModel = viewModel) { state ->
                    MainScaffold(viewModel, state)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainScaffold(
    viewModel: ColorThemeViewModel,
    state: ModalBottomSheetState? = null
) {
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val case = remember(viewModel.caseState) { viewModel.caseState }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = { scaffoldState.drawerState.open() }) {
                        Icon(imageVector = Icons.Default.Menu)
                    }
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
        drawerContent = {
            val context = AmbientContext.current

            // TODO use nav controller
            DrawerRow(title = "Cards", selected = case == Case.CARDS, onClick = {
                viewModel.caseState = Case.CARDS
            })
            DrawerRow(title = "Texts", selected = case == Case.TEXTS, onClick = {
                viewModel.caseState = Case.TEXTS
            })
            DrawerRow(title = "Buttons", selected = case == Case.BUTTONS, onClick = {
                viewModel.caseState = Case.BUTTONS
            })
            Row(modifier = Modifier.weight(1F)) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Bottom),
                    onClick = {
                        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                    }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Licenses")
                    }
                }
//                ColorDescription(
//                    modifier = Modifier.align(Alignment.Bottom),
//                    ColorDescriptionData("primary", 1F, MaterialTheme.colors.primary),
//                    ColorDescriptionData("primary", 0.12F, MaterialTheme.colors.primary),
//                    ColorDescriptionData("onSurface", 1F, MaterialTheme.colors.onSurface),
//                )
            }
        }
    ) {
        ShowCase(case)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeColorToolTheme {
        MainScaffold(ColorThemeViewModel())
    }
}
