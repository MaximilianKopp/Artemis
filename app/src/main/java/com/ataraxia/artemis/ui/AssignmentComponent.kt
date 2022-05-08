package com.ataraxia.artemis.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavigationButton
import com.ataraxia.artemis.model.QuestionProjection
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Blue
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Red
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.AssignmentViewModel
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.StatisticViewModel

class AssignmentComponent {

    @Composable
    fun AssignmentScreen(
        navController: NavController,
        isAssignmentDialogOpen: Boolean,
        onOpenAssignmentDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel,
        statisticViewModel: StatisticViewModel,
    ) {
        val assignmentQuestions: List<QuestionProjection> by questionViewModel.questionsForAssignment.observeAsState(
            listOf()
        )

        if (assignmentQuestions.isNotEmpty()) {
            AssignmentContent(
                assignmentQuestions = assignmentQuestions,
                navController = navController,
                isAssignmentDialogOpen = isAssignmentDialogOpen,
                onOpenAssignmentDialog = onOpenAssignmentDialog,
                questionViewModel = questionViewModel,
                generalViewModel = generalViewModel,
                assignmentViewModel = assignmentViewModel,
                statisticViewModel = statisticViewModel
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ResultContent(
        showResultContent: MutableState<Boolean>,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel,
        assignmentQuestions: List<QuestionProjection>
    ) {
        val verticalScrollState = rememberScrollState()
        val partialMark: MutableState<Int> = remember { mutableStateOf(0) }
        val marksByTopics = assignmentViewModel.calculateMarksByTopic(assignmentQuestions)
        val finalMark = assignmentViewModel.calculateFinalMark(marksByTopics)
        val evaluation: Boolean = assignmentViewModel.evaluate(marksByTopics, finalMark)

        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            LazyColumn {
                stickyHeader {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        backgroundColor = if (evaluation) Artemis_Green.copy(alpha = 0.1f) else Artemis_Red,
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                if (evaluation) {
                                    Text(
                                        text = "Du hast die Prüfung bestanden",
                                        color = Color.White
                                    )
                                } else {
                                    Text(
                                        text = "Du hast die Prüfung leider nicht bestanden",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    Row {
                        Button(
                            modifier = Modifier.padding(5.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Blue),
                            onClick = {
                                showResultContent.value = false
                                generalViewModel.onChangeAppBarAppearance(true)
                            }) {
                            Text(
                                color = Color.White,
                                text = "Zurück"
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.verticalScroll(verticalScrollState, true)
            ) {
                for (topic in Screen.TOPIC_SCREENS.filter { it.title != "Alle Fragen" }) {
                    val amountCorrectAnswersByTopic =
                        assignmentViewModel.filterCorrectAnswersOfEachTopic(
                            assignmentQuestions,
                            topic.topic
                        )
                    val amountWrongAnswersByTopic =
                        assignmentViewModel.filterWrongAnswersOfEachTopic(
                            amountCorrectAnswersByTopic
                        )
                    val currentMark: Int =
                        assignmentViewModel.calculateMark(amountCorrectAnswersByTopic)
                    partialMark.value = currentMark
                    Card(
                        modifier = Modifier
                            .padding(5.dp),
                        backgroundColor = Artemis_Yellow
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Row {
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.h6,
                                    color = Color.Black
                                )
                                if (partialMark.value > 4) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Icon for failed assignment",
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Icon for passed assignment",
                                        tint = Color.Green
                                    )
                                }
                            }
                            Divider(
                                color = Color.Black, thickness = 2.dp
                            )
                            Row {
                                Text(
                                    text = "Total: ${Constants.SIZE_OF_EACH_ASSIGNMENT_TOPIC}",
                                    color = Color.Black
                                )
                            }
                            Row {
                                Text(
                                    text = "Richtig: $amountCorrectAnswersByTopic",
                                    color = Color.Black
                                )
                            }
                            Row {
                                Text(
                                    text = "Falsch: $amountWrongAnswersByTopic", color = Color.Black
                                )
                            }
                            Row {
                                Text(
                                    text = "Note: $currentMark", color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AssignmentContent(
        assignmentQuestions: List<QuestionProjection>,
        navController: NavController,
        isAssignmentDialogOpen: Boolean,
        onOpenAssignmentDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel,
        statisticViewModel: StatisticViewModel
    ) {

        val showResultContent: MutableState<Boolean> = remember { mutableStateOf(false) }
        val navIndex: Int by assignmentViewModel.index.observeAsState(0)
        val currentFilter = questionViewModel.filter.observeAsState()
        val evaluationButtonText: MutableState<String> = remember { mutableStateOf("Auswertung") }
        val enableCheckbox: MutableState<Boolean> = remember { mutableStateOf(true) }
        val currentQuestion: QuestionProjection by assignmentViewModel.currentQuestion.observeAsState(
            assignmentQuestions[0]
        )
        Log.v("Current Filter", currentFilter.value.toString())

        val favouriteState: Int by assignmentViewModel.favouriteColor.observeAsState(currentQuestion.favourite)

        val resultListOfAnsweredQuestions: MutableList<QuestionProjection> =
            assignmentQuestions.toMutableList()

        val isEvaluationDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }

        if (!showResultContent.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val verticalScrollState = rememberScrollState()
                Column(
                    Modifier
                        .wrapContentHeight()
                        .weight(1f, fill = true)
                        .verticalScroll(verticalScrollState, true)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column {
                            Row {
                                IconButton(onClick = {
                                    if (currentQuestion.favourite == 1) {
                                        currentQuestion.favourite = 0
                                    } else {
                                        currentQuestion.favourite = 1
                                    }
                                    assignmentViewModel.onChangeFavouriteState(currentQuestion.favourite)
                                    questionViewModel.updateQuestion(
                                        QuestionProjection.modelToEntity(
                                            currentQuestion
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Favourite Icon",
                                        tint = if (favouriteState == 1) Color.Yellow else Color.Black
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(6.dp),
                                    text = currentQuestion.text,
                                    style = MaterialTheme.typography.body1,
                                )
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 35.dp)
                    ) {
                        Column {
                            //Creating checkboxes
                            if (currentQuestion.checkboxList.isNotEmpty()) {
                                for (checkbox in currentQuestion.checkboxList) {
                                    val checkedState =
                                        remember { mutableStateOf(checkbox.checked) }
                                    val currentQuestionText: String =
                                        assignmentViewModel.setCurrentQuestionText(
                                            currentQuestion, checkbox
                                        )
                                    Row {
                                        val isChecked =
                                            if (evaluationButtonText.value == "Auswertung") {
                                                assignmentViewModel.checkStates(
                                                    currentQuestion,
                                                    checkbox,
                                                    checkedState
                                                )
                                            } else {
                                                true
                                            }
                                        val checkBoxColor: CheckboxColors =
                                            if (evaluationButtonText.value == "Auswertung") {
                                                CheckboxDefaults.colors(checkbox.color)
                                            } else {
                                                CheckboxDefaults.colors(
                                                    assignmentViewModel.changeColorByResult(
                                                        currentQuestion,
                                                        checkbox
                                                    )
                                                )
                                            }
                                        Checkbox(
                                            checked = isChecked,
                                            colors = checkBoxColor,
                                            onCheckedChange = {
                                                assignmentViewModel.onChangeCheckboxes(
                                                    checkbox,
                                                    currentQuestion,
                                                    checkedState
                                                )
                                                assignmentViewModel.currentSelection(
                                                    checkbox.checked,
                                                    checkbox.option,
                                                    currentQuestion
                                                )
                                                resultListOfAnsweredQuestions[navIndex] =
                                                    currentQuestion
                                            },
                                            modifier = Modifier.padding(start = 5.dp)
                                        )
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 3.dp,
                                                top = 12.dp,
                                                end = 2.dp
                                            ),
                                            text = currentQuestionText,
                                            style = MaterialTheme.typography.caption
                                        )
                                    }
                                }
                                Log.v("Current Question", currentQuestion.text)
                                currentQuestion.checkboxList.forEach {
                                    Log.v(
                                        "Current option and check state",
                                        "${it.option} ${it.checked}"
                                    )
                                }
                                Log.v(
                                    "Current Question Selection",
                                    currentQuestion.currentSelection
                                )
                                Log.v(
                                    "Current Question Correct Answers",
                                    currentQuestion.correctAnswers
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (currentQuestion.lastViewed == Constants.LAST_SEEN_DEFAULT) Constants.EMPTY_STRING else "Zuletzt angesehen am ${currentQuestion.lastViewed}",
                        style = MaterialTheme.typography.caption,
                        color = Color.White
                    )
                }
                val horizontalAssignmentScrollState = rememberScrollState()
                val horizontalEvaluationScrollState = rememberScrollState()

                if (evaluationButtonText.value == "Auswertung") {
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp, bottom = 25.dp)
                            .fillMaxWidth()
                            .horizontalScroll(horizontalAssignmentScrollState, true, null, false)
                    ) {
                        val skippedBoxColor: MutableState<Color> = remember {
                            mutableStateOf(Color.White)
                        }
                        for (skippedIndex in 0..110 step 10) {
                            assignmentViewModel.changeSkippedBoxColor(
                                resultListOfAnsweredQuestions,
                                skippedBoxColor,
                                skippedIndex
                            )
                            TextButton(
                                colors = ButtonDefaults.textButtonColors(skippedBoxColor.value),
                                onClick = {
                                    assignmentViewModel.setNavigationButton(
                                        NavigationButton.SKIPPED_INDEX,
                                        skippedIndex,
                                        assignmentQuestions
                                    )
                                },
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(40.dp)
                                    .padding(end = 5.dp)
                            ) {
                                Text(
                                    style = MaterialTheme.typography.caption,
                                    text = if (skippedIndex != 0) skippedIndex.toString() else "1",
                                    color = Color.Black
                                )
                            }
                        }
                    }
                } else if (evaluationButtonText.value == "Ergebnisse") {
                    enableCheckbox.value = false
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp, bottom = 25.dp)
                            .fillMaxWidth()
                            .horizontalScroll(horizontalEvaluationScrollState, true, null, false)
                    ) {
                        for (question in assignmentQuestions) {
                            if (question.correctAnswers != question.currentSelection) {
                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(Artemis_Red),
                                    onClick = {
                                        assignmentViewModel.setNavigationButton(
                                            NavigationButton.SKIPPED_INDEX,
                                            assignmentQuestions.indexOf(question) + 1,
                                            assignmentQuestions
                                        )
                                    },
                                    modifier = Modifier
                                        .width(45.dp)
                                        .height(40.dp)
                                        .padding(end = 5.dp)
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.caption,
                                        text = (assignmentQuestions.indexOf(question) + 1).toString(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                //Navigation Buttons
                Column {
                    Row {
                        Row(
                            modifier = Modifier.padding(bottom = 30.dp)
                        ) {
                            //Loads first question
                            IconButton(
                                onClick = {
                                    assignmentViewModel.setNavigationButton(
                                        NavigationButton.FIRST_PAGE,
                                        navIndex,
                                        assignmentQuestions
                                    )
                                }) {
                                Icon(
                                    Icons.Filled.FirstPage,
                                    contentDescription = "First page button",
                                    modifier = Modifier.size(50.dp),
                                    tint = Artemis_Yellow
                                )
                            }
                            //Loads previous question
                            IconButton(
                                onClick = {
                                    assignmentViewModel.setNavigationButton(
                                        NavigationButton.PREV_PAGE,
                                        navIndex,
                                        assignmentQuestions
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ChevronLeft,
                                    contentDescription = "Prev question button",
                                    modifier = Modifier.size(50.dp),
                                    tint = Artemis_Yellow
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(Artemis_Blue),
                                //Contains whole logic for further answer processing
                                onClick = {
                                    isEvaluationDialogOpen.value = true
                                    evaluationButtonText.value = "Ergebnisse"
                                    generalViewModel.onChangeAppBarAppearance(false)
                                })
                            {
                                Text(
                                    color = Color.White,
                                    text = evaluationButtonText.value
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.padding(bottom = 30.dp)
                        ) {
                            //Loads next question
                            IconButton(
                                onClick = {
                                    assignmentViewModel.setNavigationButton(
                                        NavigationButton.NEXT_PAGE,
                                        navIndex,
                                        assignmentQuestions
                                    )
                                }) {
                                Icon(
                                    Icons.Filled.ChevronRight,
                                    contentDescription = "Next question button",
                                    modifier = Modifier.size(50.dp),
                                    tint = Artemis_Yellow
                                )
                            }

                            //Loads last question
                            IconButton(
                                onClick = {
                                    assignmentViewModel.setNavigationButton(
                                        NavigationButton.LAST_PAGE,
                                        navIndex,
                                        assignmentQuestions
                                    )
                                }) {
                                Icon(
                                    Icons.Filled.LastPage,
                                    contentDescription = "Last page button",
                                    modifier = Modifier.size(50.dp),
                                    tint = Artemis_Yellow
                                )
                            }
                        }
                    }
                }
                Column {
                    Text(
                        text = "${navIndex + 1}/${assignmentQuestions.size}",
                        style = MaterialTheme.typography.body2,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
            if (isEvaluationDialogOpen.value) {
                EvaluationAlertDialog(
                    isEvaluationDialogOpen,
                    showResultContent,
                    assignmentViewModel,
                    assignmentQuestions
                )
            }
            if (isAssignmentDialogOpen) {
                AssignmentAlertDialog(
                    onOpenAssignmentDialog,
                    navController,
                    generalViewModel
                )
            }
        } else {
            ResultContent(
                showResultContent = showResultContent,
                generalViewModel = generalViewModel,
                assignmentViewModel = assignmentViewModel,
                assignmentQuestions = assignmentQuestions
            )
        }
    }

    @Composable
    fun EvaluationAlertDialog(
        isEvaluationDialogOpen: MutableState<Boolean>,
        showResultContent: MutableState<Boolean>,
        assignmentViewModel: AssignmentViewModel,
        assignmentQuestions: List<QuestionProjection>
    ) {
        val marksByTopics = assignmentViewModel.calculateMarksByTopic(assignmentQuestions)
        val finalMark = assignmentViewModel.calculateFinalMark(marksByTopics)
        val evaluation = assignmentViewModel.evaluate(marksByTopics, finalMark)
        val amountCorrectAnswers =
            assignmentViewModel.filterCorrectAnswersInTotal(assignmentQuestions)
        val amountWrongAnswers = assignmentViewModel.filterWrongAnswersInTotal(amountCorrectAnswers)
        val failedTopics = assignmentViewModel.getFailedTopics(marksByTopics)
        val passedTopics = Constants.AMOUNT_OF_TOPICS - failedTopics

        AlertDialog(
            onDismissRequest = { isEvaluationDialogOpen.value = false },
            shape = RoundedCornerShape(15.dp),
            backgroundColor = if (evaluation) {
                Artemis_Green
            } else {
                Artemis_Red
            },
            text = {
                Column {
                    Row {
                        Text(
                            text = if (evaluation) {
                                "Du hast die Prüfung bestanden"
                            } else {
                                "Du hast die Prüfung leider nicht bestanden"
                            }, color = Color.White
                        )
                    }
                    Divider(color = Color.White, thickness = 2.dp)
                    Row {
                        Text(
                            text = "Richtig beantwortet: $amountCorrectAnswers",
                            color = Color.White
                        )
                    }
                    Row {
                        Text(text = "Falsch beantwortet: $amountWrongAnswers", color = Color.White)
                    }
                    Row {
                        Text(text = "Bestandene Sachgebiete: $passedTopics", color = Color.White)
                    }
                    Row {
                        Text(
                            text = "Durchgefallene Sachgebiete: $failedTopics",
                            color = Color.White
                        )
                    }
                }
            },
            buttons = {
                Column(
                    Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            isEvaluationDialogOpen.value = false
                            showResultContent.value = true
                        },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                    ) {
                        Text(
                            text = "Ergebnisse ansehen",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun AssignmentAlertDialog(
        onOpenAssignmentDialog: (Boolean) -> Unit,
        navController: NavController,
        generalViewModel: GeneralViewModel,
    ) {
        AlertDialog(
            onDismissRequest = { onOpenAssignmentDialog(false) },
            backgroundColor = Artemis_Green,
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Zurück zum Hauptmenü?",
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }
            },
            buttons = {
                Column(
                    Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Screen.DrawerScreen.Home.route)
                            onOpenAssignmentDialog(false)
                            generalViewModel.onCloseTrainingScreen(
                                Pair(
                                    Constants.ALPHA_INVISIBLE,
                                    Constants.DISABLED
                                )
                            )
                        },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                    ) {
                        Text(
                            text = "Ja",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Button(
                        onClick = {
                            onOpenAssignmentDialog(false)
                        },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(Artemis_Yellow)
                    ) {
                        Text(
                            text = "Nein",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        )
    }
}


