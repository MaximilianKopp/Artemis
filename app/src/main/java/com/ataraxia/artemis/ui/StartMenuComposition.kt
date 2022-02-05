package com.ataraxia.artemis.ui

import AppBarViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ataraxia.artemis.R
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavHelper
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.templates.TextButtonTemplate
import com.ataraxia.artemis.templates.TextTemplate
import com.ataraxia.artemis.ui.theme.GREEN_ARTEMIS
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS

class StartMenuComposition {
    private val appBarComposition = AppBarComposition()
    @Composable
    fun StartScreen(
        topBarViewModel: AppBarViewModel = viewModel(), questionViewModel: QuestionViewModel = viewModel()
    ) {
        val title: String by topBarViewModel.title.observeAsState("")
        val filter: Float by topBarViewModel.filter.observeAsState(Constants.FILTER_ALPHA_INVISIBLE)
        StartContent(
            title = title,
            filter = filter,
            onHideFilter = { topBarViewModel.onHideFilter(visible = it) },
            onTitleChange = { topBarViewModel.onTopBarTitleChange(newTitle = it) },
            questionViewModel = questionViewModel,
            topBarViewModel = topBarViewModel
        )
    }

    @Composable
    fun StartContent(
        title: String,
        filter: Float,
        onHideFilter: (Float) -> Unit,
        onTitleChange: (String) -> Unit,
        questionViewModel: QuestionViewModel,
        topBarViewModel: AppBarViewModel = viewModel()
    ) {
        val navController = rememberNavController()
        val state = rememberScaffoldState(drawerState = DrawerState(DrawerValue.Closed))
        val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = state,
            backgroundColor = GREEN_ARTEMIS,
            topBar = {
                appBarComposition.GeneralTopAppBar(
                    scope = scope,
                    state = state,
                    title = title,
                    filter = filter,
                    topBarViewModel = topBarViewModel
                )
            },
            drawerContent = {
                appBarComposition.DrawerContent(
                    scope = scope,
                    state = state,
                    onTitleChange = onTitleChange,
                    navController = navController
                )
            },
            drawerBackgroundColor = YELLOW_ARTEMIS
        ) {
            NavHelper.LoadNavigationRoutes(
                navController = navController,
                paddingValues = it,
                onTitleChange = onTitleChange,
                onHideFilter = onHideFilter,
                scope = scope,
                questionViewModel = questionViewModel,
                topBarViewModel = topBarViewModel
            )
        }
    }

    @Composable
    fun StartMenuButton(
        onClick: () -> Unit,
        content: @Composable RowScope.() -> Unit
    ) {
        Button(
            shape = TextButtonTemplate.MENU_SHAPE,
            modifier = TextButtonTemplate.MENU_MODIFIER,
            colors = TextButtonTemplate.menuBackgroundColor(),
            onClick = onClick,
            content = content
        )
    }

    @Composable
    fun StartMenuText(text: String) = Text(
        text = text,
        style = TextTemplate.menuStyle(),
        color = TextTemplate.MENU_COLOR
    )

    @Composable
    fun StartMenuContent(drawable: Int, contentDescription: String, text: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = contentDescription
            )
            StartMenuText(text = text)
        }
    }

    @Composable
    fun StartMenu(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                StartMenuButton(onClick = { navController.navigate(Screen.DrawerScreen.Questions.route) }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_menu_book_24,
                        contentDescription = "Questions",
                        text = "Sachgebiete"
                    )
                }
                StartMenuButton(onClick = { navController.navigate("exam") } ) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_assignment_24,
                        contentDescription = "Examination",
                        text = "Prüfung"
                    )
                }
            }
            Row {
                StartMenuButton(onClick = { navController.navigate("statistics") }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_insert_chart_24,
                        contentDescription = "Statistics",
                        text = "Statistik"
                    )
                }
                StartMenuButton(onClick = { navController.navigate("configuration") }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_build_circle_24,
                        contentDescription = "Configuration",
                        text = "Einstellungen"
                    )
                }
            }
        }
    }
}
