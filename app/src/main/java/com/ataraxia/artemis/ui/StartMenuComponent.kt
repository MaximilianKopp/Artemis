package com.ataraxia.artemis.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ataraxia.artemis.R
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavHelper
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.templates.TextButtonTemplate
import com.ataraxia.artemis.templates.TextTemplate
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.StatisticViewModel
import com.ataraxia.artemis.viewModel.TrainingViewModel
import java.math.BigDecimal

class StartMenuComponent {
    private val appBarComposition = AppBarComponent()

    @ExperimentalFoundationApi
    @Composable
    fun StartScreen(
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        statisticViewModel: StatisticViewModel,
    ) {
        StartContent(
            generalViewModel,
            questionViewModel,
            trainingViewModel,
            statisticViewModel
        )
    }

    @ExperimentalFoundationApi
    @Composable
    fun StartContent(
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        statisticViewModel: StatisticViewModel
    ) {
        val navController: NavHostController = rememberNavController()
        val state = rememberScaffoldState(drawerState = DrawerState(DrawerValue.Closed))
        val scope = rememberCoroutineScope()
        val isFilterDialogOpen: Boolean by generalViewModel.filterDialog.observeAsState(false)
        val isTrainingDialogClosed: Boolean by generalViewModel.closeTrainingDialog.observeAsState(
            false
        )
        BackHandler {
            navController.navigate(Screen.DrawerScreen.Home.route) {
                navController.popBackStack()
            }
        }

        Scaffold(
            scaffoldState = state,
            backgroundColor = Artemis_Green,
            topBar = {
                appBarComposition.GeneralTopAppBar(
                    scope = scope,
                    state = state,
                    generalViewModel = generalViewModel,
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
            drawerBackgroundColor = Artemis_Yellow
        ) { it ->
            NavHelper.LoadNavigationRoutes(
                navController = navController,
                paddingValues = it,
                isFilterDialogOpen = isFilterDialogOpen,
                onOpenFilterDialog = { generalViewModel.onOpenFilterDialog(it) },
                isTrainingDialogClosed = isTrainingDialogClosed,
                onOpenTrainingDialog = { generalViewModel.onOpenTrainingDialog(it) },
                generalViewModel = generalViewModel,
                questionViewModel = questionViewModel,
                trainingViewModel = trainingViewModel,
                statisticViewModel = statisticViewModel
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
    fun StartMenu(
        statisticViewModel: StatisticViewModel,
        navController: NavController
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
                .verticalScroll(scrollState, true),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            ShowStartScreenInfo(statisticViewModel)
            Spacer(modifier = Modifier.padding(top = 50.dp))
            Row {
                StartMenuButton(onClick = {
                    navController.navigate(Screen.DrawerScreen.Questions.route)
                }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_menu_book_24,
                        contentDescription = "Questions",
                        text = "Sachgebiete"
                    )
                }
                StartMenuButton(onClick = {
                    navController.navigate(Screen.DrawerScreen.Exam.route)
                }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_assignment_24,
                        contentDescription = "Examination",
                        text = "Prüfung"
                    )
                }
            }
            Row {
                StartMenuButton(onClick = {
                    navController.navigate(Screen.DrawerScreen.Statistics.route)
                }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_insert_chart_24,
                        contentDescription = "Statistics",
                        text = "Statistik"
                    )
                }
                StartMenuButton(onClick = {
                    navController.navigate(Screen.DrawerScreen.Configuration.route)
                }) {
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
    fun ShowStartScreenInfo(
        statisticViewModel: StatisticViewModel
    ) {
        val allQuestions: Int by statisticViewModel.allQuestionsCount.observeAsState(0)
        val onceLearnedQuestions: Int by statisticViewModel.allLearnedOnceQuestionsCount.observeAsState(
            0
        )
        val learnedQuestions: Int by statisticViewModel.allLearnedQuestionsCount.observeAsState(0)
        val failedQuestions: Int by statisticViewModel.allFailedQuestionCount.observeAsState(0)
        val progressInPercent: BigDecimal by statisticViewModel.progressInPercent.observeAsState(
            BigDecimal.ZERO
        )

        Column {
            Row(
                modifier = Modifier.padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Constants.APP_NAME,
                    color = Color.White,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .weight(0.75f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_coat_of_arms_of_rhineland_palatinate),
                    contentDescription = "Coat of arms of RLP",
                    modifier = Modifier
                        .padding(bottom = 5.dp, end = 10.dp)
                        .weight(0.25f)
                )

            }
            Divider(thickness = 2.dp, color = Color.White, startIndent = 25.dp)
            Text(
                text = Constants.DESCRIPTION,
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "Alle Fragen: $allQuestions",
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