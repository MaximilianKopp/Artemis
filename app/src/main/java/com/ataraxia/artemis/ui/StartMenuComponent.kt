package com.ataraxia.artemis.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.ataraxia.artemis.viewModel.AssignmentViewModel
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.TrainingViewModel

class StartMenuComponent {
    private val appBarComposition = AppBarComponent()

    @ExperimentalFoundationApi
    @Composable
    fun StartScreen(
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        assignmentViewModel: AssignmentViewModel
    ) {
        StartContent(
            generalViewModel,
            questionViewModel,
            trainingViewModel,
            assignmentViewModel
        )
    }

    @ExperimentalFoundationApi
    @Composable
    fun StartContent(
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        assignmentViewModel: AssignmentViewModel
    ) {
        val navController: NavHostController = rememberNavController()
        val state = rememberScaffoldState(drawerState = DrawerState(DrawerValue.Closed))
        val scope = rememberCoroutineScope()
        val currentScreen: Screen.DrawerScreen by generalViewModel.currentScreen.observeAsState(
            Screen.DrawerScreen.Home
        )
        val isFilterDialogOpen: Boolean by generalViewModel.filterDialog.observeAsState(false)
        val isTrainingDialogClosed: Boolean by generalViewModel.openTrainingDialog.observeAsState(
            false
        )
        val isAssignmentDialogClosed: Boolean by generalViewModel.openAssignmentDialog.observeAsState(
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
                    currentScreen = currentScreen,
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
            drawerBackgroundColor = Artemis_Green
        ) { it ->
            NavHelper.LoadNavigationRoutes(
                navController = navController,
                paddingValues = it,
                isFilterDialogOpen = isFilterDialogOpen,
                onOpenFilterDialog = { generalViewModel.onOpenFilterDialog(it) },
                isTrainingDialogClosed = isTrainingDialogClosed,
                onOpenTrainingDialog = { generalViewModel.onOpenTrainingDialog(it) },
                isAssignmentDialogClosed = isAssignmentDialogClosed,
                onOpenAssignmentDialog = { generalViewModel.onOpenAssignmentDialog(it) },
                generalViewModel = generalViewModel,
                questionViewModel = questionViewModel,
                trainingViewModel = trainingViewModel,
                assignmentViewModel = assignmentViewModel
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
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        assignmentViewModel: AssignmentViewModel,
        navController: NavController
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 5.dp)
                .verticalScroll(scrollState, true),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            ShowStartScreenInfo(questionViewModel)
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Row {
                StartMenuButton(onClick = {
                    navController.navigate(Screen.DrawerScreen.Questions.route)
                }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_menu_book_24,
                        contentDescription = "Questions",
                        text = Screen.DrawerScreen.Questions.title
                    )
                }
                StartMenuButton(onClick = {
                    val assignmentQuestions =
                        questionViewModel.prepareQuestionsForAssignment().toList()
                    questionViewModel.onChangeQuestionsForAssignment(assignmentQuestions)
                    assignmentViewModel.onChangeCurrentQuestion(assignmentQuestions[0])
                    generalViewModel.onChangeCurrentScreen(Screen.DrawerScreen.Assignment)
                    navController.navigate(Screen.DrawerScreen.Assignment.route)
                }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_assignment_24,
                        contentDescription = "Examination",
                        text = Screen.DrawerScreen.Assignment.title
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
                        text = Screen.DrawerScreen.Statistics.title
                    )
                }
                StartMenuButton(onClick = {
                    navController.navigate(Screen.DrawerScreen.Imprint.route)
                }) {
                    StartMenuContent(
                        drawable = R.drawable.ic_baseline_info_24,
                        contentDescription = "Imprint",
                        text = Screen.DrawerScreen.Imprint.title
                    )
                }
            }
            Row {
                Card(
                    shape = RoundedCornerShape(15.dp),
                    modifier =
                    Modifier
                        .padding(15.dp)
                        .size(200.dp, 35.dp)
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                        .clickable {
                            navController.navigate(Screen.DrawerScreen.Privacy.route)
                        }
                ) {
                    Row(
                        modifier = Modifier.background(Artemis_Yellow),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1.4f),
                            text = Screen.DrawerScreen.Privacy.title,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ShowStartScreenInfo(
        questionViewModel: QuestionViewModel,
    ) {
        val allQuestions: Int = questionViewModel.allQuestions.size

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
                text = "1x richtig beantwortet: ${questionViewModel.extractTotalStatistics()["OnceLearnedTotal"]}",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "2x richtig beantwortet: ${questionViewModel.extractTotalStatistics()["TwiceLearnedTotal"]}",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "Falsch beantwortet: ${questionViewModel.extractTotalStatistics()["FailedTotal"]}",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
            Text(
                text = "${questionViewModel.extractTotalStatistics()["TotalPercentage"]}% gelernt",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 10.dp)
            )
        }
    }
}