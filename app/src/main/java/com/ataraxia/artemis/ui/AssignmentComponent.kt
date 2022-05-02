package com.ataraxia.artemis.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavigationButton
import com.ataraxia.artemis.model.QuestionProjection
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Blue
import com.ataraxia.artemis.ui.theme.Artemis_Green
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

    @Composable
    fun AssignmentContent(
        assignmentQuestions: List<QuestionProjection>,
        navController: NavController,
        isAssignmentDialogOpen: Boolean,
        onOpenAssignmentDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel,
        statisticViewModel: StatisticViewModel,
    ) {

        val navIndex: Int by assignmentViewModel.index.observeAsState(0)
        val currentFilter = questionViewModel.filter.observeAsState()
        val currentQuestion: QuestionProjection by assignmentViewModel.currentQuestion.observeAsState(
            assignmentQuestions[0]
        )
        val context = LocalContext.current
        Log.v("Current Filter", currentFilter.value.toString())

        val favouriteState: Int by assignmentViewModel.favouriteColor.observeAsState(currentQuestion.favourite)

        val resultListOfAnsweredQuestions: MutableList<QuestionProjection> =
            assignmentQuestions.toMutableList()

        val isEvaluationDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }

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
                                    rememberSaveable { mutableStateOf(checkbox.checked) }
                                val currentQuestionText: String =
                                    assignmentViewModel.setCurrentQuestionText(
                                        currentQuestion, checkbox
                                    )
                                Row {
                                    Checkbox(
                                        checked = assignmentViewModel.checkedStates(
                                            currentQuestion,
                                            checkbox,
                                            checkedState
                                        ),
                                        colors = CheckboxDefaults.colors(checkbox.color),
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
                            Log.v("Current Question Selection", currentQuestion.currentSelection)
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
            val horizontalScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 25.dp)
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState, true, null, false)
            ) {
                for (skippedIndex in 0..110 step 10) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(Color.White),
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
                                val text: String =
                                    if (currentQuestion.correctAnswers == currentQuestion.currentSelection) {
                                        "Korrekt"
                                    } else {
                                        "Falsch"
                                    }
                                Toast.makeText(context, text, Toast.LENGTH_SHORT)
                                    .show()
                                isEvaluationDialogOpen.value = true
                            })
                        {
                            Text(
                                color = Color.White,
                                text = "Auswerten"
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
            EvaluationAlertDialog(isEvaluationDialogOpen)
        }
        if (isAssignmentDialogOpen) {
            AssignmentAlertDialog(
                onOpenAssignmentDialog,
                navController,
                generalViewModel,
                questionViewModel
            )
        }
    }

    @Composable
    fun EvaluationAlertDialog(isEvaluationDialogOpen: MutableState<Boolean>) {
        AlertDialog(
            onDismissRequest = { isEvaluationDialogOpen.value = false },
            backgroundColor = Artemis_Green,
            text = {
                Text(text = "Test")
            },
            buttons = {
                Column(
                    Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            isEvaluationDialogOpen.value = false
                        },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                    ) {
                        Text(
                            text = "Zurück",
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
        questionViewModel: QuestionViewModel

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


