package com.ataraxia.artemis.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ataraxia.artemis.R
import com.ataraxia.artemis.data.GeneralViewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavHelper
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.templates.TextButtonTemplate
import com.ataraxia.artemis.templates.TextTemplate
import com.ataraxia.artemis.ui.theme.GREEN_ARTEMIS
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS

class StartMenuComponent {
    private val appBarComposition = AppBarComponent()

    @Composable
    fun StartScreen(

    ) {
        StartContent()
    }

    @Composable
    fun StartContent(
    ) {
        val questionViewModel: QuestionViewModel = viewModel()
        val generalViewModel: GeneralViewModel = viewModel()
        val navController = rememberNavController()
        val state = rememberScaffoldState(drawerState = DrawerState(DrawerValue.Closed))
        val scope = rememberCoroutineScope()
        val isFilterDialogOpen: Boolean by generalViewModel.filterDialog.observeAsState(false)
        val isTrainingDialogClosed: Boolean by generalViewModel.closeTrainingDialog.observeAsState(
            false
        )
        val showStartScreenInfo: Boolean by generalViewModel.showStartScreenInfo.observeAsState(
            true
        )
        Scaffold(
            scaffoldState = state,
            backgroundColor = GREEN_ARTEMIS,
            topBar = {
                appBarComposition.GeneralTopAppBar(
                    scope = scope,
                    state = state,
                    questionViewModel = questionViewModel
                )
            },
            drawerContent = {
                appBarComposition.DrawerContent(
                    questionViewModel = questionViewModel,
                    scope = scope,
                    state = state,
                    navController = navController
                )
            },
            drawerBackgroundColor = YELLOW_ARTEMIS
        ) { it ->
            if (showStartScreenInfo) {
                ShowStartScreenInfo(questionViewModel)
            }
            NavHelper.LoadNavigationRoutes(
                navController = navController,
                paddingValues = it,
                isFilterDialogOpen = isFilterDialogOpen,
                onOpenFilterDialog = { generalViewModel.onOpenFilterDialog(it) },
                isTrainingDialogClosed = isTrainingDialogClosed,
                onCloseTrainingDialog = { generalViewModel.onOpenTrainingDialog(it) }
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
                StartMenuButton(onClick = { navController.navigate(Screen.DrawerScreen.Exam.route) }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_assignment_24,
                        contentDescription = "Examination",
                        text = "Prüfung"
                    )
                }
            }
            Row {
                StartMenuButton(onClick = { navController.navigate(Screen.DrawerScreen.Statistics.route) }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_insert_chart_24,
                        contentDescription = "Statistics",
                        text = "Statistik"
                    )
                }
                StartMenuButton(onClick = { navController.navigate(Screen.DrawerScreen.Configuration.route) }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_build_circle_24,
                        contentDescription = "Configuration",
                        text = "Einstellungen"
                    )
                }
            }
        }
    }

    @Composable
    fun ShowStartScreenInfo(questionViewModel: QuestionViewModel) {
        val allQuestions = questionViewModel.allQuestions
        val onceLearnedQuestions = questionViewModel.onceLearnedQuestions
        val learnedQuestions = questionViewModel.learnedQuestions
        val failedQuestions = questionViewModel.failedQuestions
        val progressInPercent = questionViewModel.progressInPercent

        Column {
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Text(
                    text = Constants.APP_NAME,
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp, bottom = 10.dp)
                )
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_coat_of_arms_of_rhineland_palatinate),
                        contentDescription = "Coat of arms of RLP",
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
            Divider(thickness = 2.dp, color = Color.White, startIndent = 25.dp)
            Text(
                text = Constants.DESCRIPTION,
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "Alle Fragen: ${allQuestions.count()}",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 25.dp)
            )
            Text(
                text = "1x richtig beantwortet: $onceLearnedQuestions",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "2x richtig beantwortet: $learnedQuestions",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "Falsch beantwortet: $failedQuestions",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "$progressInPercent% gelernt",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
        }
    }
}
