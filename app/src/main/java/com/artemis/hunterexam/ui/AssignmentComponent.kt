package com.artemis.hunterexam.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.NavigationButton
import com.artemis.hunterexam.model.QuestionProjection
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.ui.theme.Artemis_Blue
import com.artemis.hunterexam.ui.theme.Artemis_Green
import com.artemis.hunterexam.ui.theme.Artemis_Red
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import com.artemis.hunterexam.viewModel.AssignmentViewModel
import com.artemis.hunterexam.viewModel.GeneralViewModel
import com.artemis.hunterexam.viewModel.QuestionViewModel

@ExperimentalFoundationApi
class AssignmentComponent {

    @Composable
    fun TopicButtons(
        assignmentViewModel: AssignmentViewModel,
        assignmentQuestions: List<QuestionProjection>
    ) {
        val topicWildLifeButtonColor: Color by assignmentViewModel.topicWildlifeButtonColor.observeAsState(
            Color.White
        )
        val topicHuntingOperations: Color by assignmentViewModel.topicHuntingOperations.observeAsState(
            Color.White
        )
        val topicWeaponsLawAndTechnology: Color by assignmentViewModel.topicWeaponsLawAndTechnology.observeAsState(
            Color.White
        )
        val topicWildLifeTreatment: Color by assignmentViewModel.topicWildLifeTreatment.observeAsState(
            Color.White
        )
        val topicHuntingLaw: Color by assignmentViewModel.topicHuntingLaw.observeAsState(Color.White)
        val topicPreservationOfWildLifeAndNature: Color by assignmentViewModel.topicPreservationOfWildLifeAndNature.observeAsState(
            Color.White
        )

        //Topic wildlife
        TextButton(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .padding(5.dp),
            colors = ButtonDefaults.textButtonColors(topicWildLifeButtonColor),
            onClick = {
                assignmentViewModel.setTopicBoxButton(
                    NavigationButton.SKIPPED_INDEX,
                    Screen.DrawerScreen.TopicWildLife.topic,
                    assignmentQuestions
                )
            },
        ) {
            Text(
                color = Color.Black,
                style = MaterialTheme.typography.overline,
                text = Screen.DrawerScreen.TopicWildLife.title,
            )
        }

        //Topic hunting operations
        TextButton(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .padding(5.dp),
            colors = ButtonDefaults.textButtonColors(topicHuntingOperations),
            onClick = {
                assignmentViewModel.setTopicBoxButton(
                    NavigationButton.SKIPPED_INDEX,
                    Screen.DrawerScreen.TopicHuntingOperations.topic,
                    assignmentQuestions
                )
            },
        ) {
            Text(
                color = Color.Black,
                style = MaterialTheme.typography.overline,
                text = Screen.DrawerScreen.TopicHuntingOperations.title,
            )
        }

        //Topic weapons and law
        TextButton(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .padding(5.dp),
            colors = ButtonDefaults.textButtonColors(topicWeaponsLawAndTechnology),
            onClick = {
                assignmentViewModel.setTopicBoxButton(
                    NavigationButton.SKIPPED_INDEX,
                    Screen.DrawerScreen.TopicWeaponsLawAndTechnology.topic,
                    assignmentQuestions
                )
            },
        ) {
            Text(
                color = Color.Black,
                style = MaterialTheme.typography.overline,
                text = Screen.DrawerScreen.TopicWeaponsLawAndTechnology.title,
            )
        }

        //Topic wildlife treatment
        TextButton(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .padding(5.dp),
            colors = ButtonDefaults.textButtonColors(topicWildLifeTreatment),
            onClick = {
                assignmentViewModel.setTopicBoxButton(
                    NavigationButton.SKIPPED_INDEX,
                    Screen.DrawerScreen.TopicWildLifeTreatment.topic,
                    assignmentQuestions
                )
            },
        ) {
            Text(
                color = Color.Black,
                style = MaterialTheme.typography.overline,
                text = Screen.DrawerScreen.TopicWildLifeTreatment.title,
            )
        }

        //Topic hunting law
        TextButton(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .padding(5.dp),
            colors = ButtonDefaults.textButtonColors(topicHuntingLaw),
            onClick = {
                assignmentViewModel.setTopicBoxButton(
                    NavigationButton.SKIPPED_INDEX,
                    Screen.DrawerScreen.TopicHuntingLaw.topic,
                    assignmentQuestions
                )
            },
        ) {
            Text(
                color = Color.Black,
                style = MaterialTheme.typography.overline,
                text = Screen.DrawerScreen.TopicHuntingLaw.title,
            )
        }

        //Topic preservation of wildlife and nature
        TextButton(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .padding(5.dp),
            colors = ButtonDefaults.textButtonColors(topicPreservationOfWildLifeAndNature),
            onClick = {
                assignmentViewModel.setTopicBoxButton(
                    NavigationButton.SKIPPED_INDEX,
                    Screen.DrawerScreen.TopicPreservationOfWildLifeAndNature.topic,
                    assignmentQuestions
                )
            },
        ) {
            Text(
                color = Color.Black,
                style = MaterialTheme.typography.overline,
                text = Screen.DrawerScreen.TopicPreservationOfWildLifeAndNature.title,
            )
        }
    }

    @Composable
    fun AssignmentScreen(
        navController: NavController,
        isAssignmentDialogOpen: Boolean,
        onOpenAssignmentDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel,
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
                assignmentViewModel = assignmentViewModel
            )
        }
    }

    @Composable
    fun ResultContent(
        showResultContent: MutableState<Boolean>,
        assignmentViewModel: AssignmentViewModel,
        generalViewModel: GeneralViewModel,
        assignmentQuestions: List<QuestionProjection>
    ) {
        val verticalScrollState = rememberScrollState()
        val marksByTopics = assignmentViewModel.calculateMarksByTopic(assignmentQuestions)
        val evaluation: Boolean = assignmentViewModel.evaluate(marksByTopics)
        val openScoringDialog = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            LazyColumn {
                stickyHeader {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        backgroundColor = if (evaluation) Artemis_Yellow.copy(alpha = 0.1f) else Artemis_Red,
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                IconButton(
                                    onClick = { openScoringDialog.value = true },
                                ) {
                                    if (openScoringDialog.value) {
                                        ScoringSystem(openScoringDialog = openScoringDialog)
                                    }
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Range of scoring",
                                        tint = Artemis_Yellow
                                    )
                                }
                                if (evaluation) {
                                    Text(
                                        modifier = Modifier.padding(5.dp),
                                        textAlign = TextAlign.Center,
                                        text = "Herzlichen Glückwunsch!\nDu hast die Prüfung bestanden",
                                        color = Color.White,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier.padding(5.dp),
                                        textAlign = TextAlign.Center,
                                        text = "Du hast die Prüfung leider nicht bestanden\nEs wurden mind. 1 Sachgebiet mit der Note 6 oder mehr als 2 Sachgebiete mit der Note 5 abgeschlossen",
                                        color = Color.White,
                                        style = MaterialTheme.typography.subtitle2
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
                                generalViewModel.onChangeVisibilityOfAssignmentCloseButton(
                                    Pair(
                                        Constants.ALPHA_VISIBLE,
                                        Constants.ENABLED
                                    )
                                )
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
                    val currentMark: Int? =
                        marksByTopics[topic.title]
                    Card(
                        modifier = Modifier
                            .padding(5.dp),
                        backgroundColor = Artemis_Yellow,
                        border = BorderStroke(1.dp, Color.White)
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
                                    text = "Note: $currentMark",
                                    color = if (currentMark != null && currentMark > 4) Artemis_Red else Artemis_Green
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ScoringSystem(openScoringDialog: MutableState<Boolean>) {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color.White, RectangleShape),
            onDismissRequest = { openScoringDialog.value = false },
            backgroundColor = Artemis_Yellow,
            text = {
                Column {
                    Text(
                        text = "Notenschlüssel",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.body1
                    )
                    Text(text = "19-20 Punkte -> Note 1")
                    Text(text = "16-18 Punkte -> Note 2")
                    Text(text = "13-15 Punkte -> Note 3")
                    Text(text = "10-12 Punkte -> Note 4")
                    Text(text = "  7-9 Punkte -> Note 5")
                    Text(text = "  0-7 Punkte -> Note 6")
                    Divider(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        color = Color.White,
                        thickness = 1.dp
                    )
                    Text(text = "Die Prüfung gilt als nicht bestanden, wenn in mind. einem Sachgebiet die Note 6 oder in mind. 2 Sachgebieten die Note 5 erreicht wurde.")
                }
            },
            buttons = {})
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
                val horizontalAssignmentScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .padding(start = 5.dp, top = 25.dp)
                        .fillMaxWidth()
                        .horizontalScroll(horizontalAssignmentScrollState, true, null, false)
                ) {
                    TopicButtons(assignmentViewModel, assignmentQuestions)
                }
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
                                        currentQuestion.favouriteState.value = Color.Black
                                    } else {
                                        currentQuestion.favourite = 1
                                        currentQuestion.favouriteState.value = Color.Yellow
                                    }
                                    questionViewModel.updateQuestion(
                                        QuestionProjection.modelToEntity(
                                            currentQuestion
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Favourite Icon",
                                        tint = currentQuestion.favouriteState.value
                                    )
                                }
                                Column {
                                    Text(
                                        modifier = Modifier.padding(6.dp),
                                        text = questionViewModel.getTopicOfQuestion(currentQuestion.topic),
                                        style = MaterialTheme.typography.caption,
                                        color = Color.Black
                                    )
                                    Text(
                                        modifier = Modifier.padding(6.dp),
                                        text = currentQuestion.text,
                                        style = MaterialTheme.typography.body1,
                                    )
                                }
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        Column {
                            //Creating checkboxes
                            if (currentQuestion.checkboxList.isNotEmpty()) {
                                for (checkbox in currentQuestion.checkboxList) {
                                    val checkedState =
                                        remember { mutableStateOf(checkbox.checked) }
                                    val currentQuestionText: String =
                                        assignmentViewModel.setCurrentOptionText(
                                            currentQuestion, checkbox.option
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
                                assignmentViewModel.checkTopicButtonColors(
                                    assignmentQuestions,
                                    currentQuestion
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
                val horizontalEvaluationScrollState = rememberScrollState()

                if (evaluationButtonText.value == "Ergebnisse") {
                    enableCheckbox.value = false
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp, bottom = 25.dp)
                            .fillMaxWidth()
                            .horizontalScroll(horizontalEvaluationScrollState, true, null, false)
                    ) {
                        for (question in assignmentQuestions) {
                            if (question.correctAnswers != question.currentSelection.toSortedSet()
                                    .toString()
                            ) {
                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(Artemis_Red),
                                    onClick = {
                                        assignmentViewModel.setDirection(
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
                    Row(
                        modifier = Modifier.padding(bottom = 30.dp)
                    ) {
                        //Loads first question
                        IconButton(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                assignmentViewModel.setDirection(
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
                        //Skip to 10 questions backward
                        IconButton(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                assignmentViewModel.setDirection(
                                    NavigationButton.SKIP_TEN_BACKWARD,
                                    navIndex,
                                    assignmentQuestions
                                )
                            }
                        ) {
                            Icon(
                                Icons.Filled.RotateLeft,
                                contentDescription = "Prev question button",
                                modifier = Modifier.size(25.dp),
                                tint = Artemis_Yellow
                            )
                        }
                        //Loads previous question
                        IconButton(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                assignmentViewModel.setDirection(
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
                        Button(
                            modifier = Modifier.weight(0.4f),
                            colors = ButtonDefaults.buttonColors(Artemis_Blue),
                            //Contains whole logic for further answer processing
                            onClick = {
                                if (evaluationButtonText.value == "Auswertung") {
                                    isEvaluationDialogOpen.value = true
                                } else {
                                    showResultContent.value = true
                                    generalViewModel.onChangeVisibilityOfAssignmentCloseButton(
                                        Pair(
                                            Constants.ALPHA_INVISIBLE,
                                            Constants.DISABLED
                                        )
                                    )
                                }
                            })
                        {
                            Text(
                                color = Color.White,
                                text = evaluationButtonText.value
                            )
                        }
                        //Loads next question
                        IconButton(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                assignmentViewModel.setDirection(
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
                        //Skip to 10 questions forward
                        IconButton(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                assignmentViewModel.setDirection(
                                    NavigationButton.SKIP_TEN_FORWARD,
                                    navIndex,
                                    assignmentQuestions
                                )
                            }
                        ) {
                            Icon(
                                Icons.Filled.RotateRight,
                                contentDescription = "Prev question button",
                                modifier = Modifier.size(25.dp),
                                tint = Artemis_Yellow
                            )
                        }
                        //Loads last question
                        IconButton(
                            modifier = Modifier.weight(0.1f),
                            onClick = {
                                assignmentViewModel.setDirection(
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
                    questionViewModel,
                    generalViewModel,
                    assignmentQuestions,
                    resultListOfAnsweredQuestions,
                    evaluationButtonText
                )
            } // no else
            if (isAssignmentDialogOpen) {
                AssignmentAlertDialog(
                    onOpenAssignmentDialog,
                    navController,
                    generalViewModel,
                    assignmentViewModel
                )
            } // no else
            BackHandler(enabled = true) {
                onOpenAssignmentDialog(true)
            }
        } else {
            BackHandler(enabled = true) {
                onOpenAssignmentDialog(false)
            }
            ResultContent(
                showResultContent = showResultContent,
                assignmentViewModel = assignmentViewModel,
                generalViewModel = generalViewModel,
                assignmentQuestions = assignmentQuestions
            )
        }
    }

    @Composable
    fun EvaluationAlertDialog(
        isEvaluationDialogOpen: MutableState<Boolean>,
        showResultContent: MutableState<Boolean>,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        assignmentQuestions: List<QuestionProjection>,
        resultListOfAnsweredQuestions: List<QuestionProjection>,
        evaluationButtonText: MutableState<String>,
    ) {
        val isAbleToEvaluate = assignmentQuestions.stream()
            .allMatch { it.currentSelection != "[]" && it.currentSelection != Constants.EMPTY_STRING }

        AlertDialog(
            onDismissRequest = { isEvaluationDialogOpen.value = false },
            shape = RoundedCornerShape(15.dp),
            backgroundColor = Artemis_Green,
            text = {
                Column {
                    Row {
                        Text(
                            text = if (isAbleToEvaluate) {
                                "Möchtest du die Prüfung auswerten?"
                            } else {
                                "Du hast noch nicht alle Fragen angekreuzt. Möchtest du die Prüfung trotzdem auswerten?"
                            }, color = Color.White
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
                            showResultContent.value = true
                            isEvaluationDialogOpen.value = false
                            resultListOfAnsweredQuestions
                                .filter {
                                    it.currentSelection != it.correctAnswers
                                }.map {
                                    it.learnedOnce = 0
                                    it.learnedTwice = 0
                                    it.failed = 1
                                    questionViewModel.updateQuestion(
                                        QuestionProjection.modelToEntity(
                                            it
                                        )
                                    )
                                }
                            evaluationButtonText.value = "Ergebnisse"
                            generalViewModel.onChangeVisibilityOfAssignmentCloseButton(
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
                        border = BorderStroke(1.dp, Color.White)
                    ) {
                        Text(
                            text = "Ja",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Button(
                        onClick = {
                            isEvaluationDialogOpen.value = false
                        },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        border = BorderStroke(1.dp, Color.White)
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

    @Composable
    fun AssignmentAlertDialog(
        onOpenAssignmentDialog: (Boolean) -> Unit,
        navController: NavController,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel
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
                            assignmentViewModel.onChangeIndex(0)
                            generalViewModel.onChangeCurrentScreen(Screen.DrawerScreen.Home)
                            generalViewModel.onChangeVisibilityOfAssignmentCloseButton(
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
                        border = BorderStroke(1.dp, Color.White)
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
                        colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        border = BorderStroke(1.dp, Color.White)
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


