package com.ataraxia.artemis.ui

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
        val checkedAnswers: List<String> = assignmentViewModel.checkedAnswers

        val currentFilter = questionViewModel.filter.observeAsState()

        val currentQuestion: QuestionProjection by assignmentViewModel.currentQuestion.observeAsState(
            assignmentQuestions[0]
        )

        Log.v("Current Filter", currentFilter.value.toString())

        val checkedA: Boolean by assignmentViewModel.checkedA.observeAsState(false)
        val checkedB: Boolean by assignmentViewModel.checkedB.observeAsState(false)
        val checkedC: Boolean by assignmentViewModel.checkedC.observeAsState(false)
        val checkedD: Boolean by assignmentViewModel.checkedD.observeAsState(false)

        val selectA: String by assignmentViewModel.selectA.observeAsState("a")
        val selectB: String by assignmentViewModel.selectB.observeAsState("b")
        val selectC: String by assignmentViewModel.selectC.observeAsState("c")
        val selectD: String by assignmentViewModel.selectD.observeAsState("d")

        val checkBoxColorA: Color by assignmentViewModel.checkBoxColorA.observeAsState(Color.Black)
        val checkBoxColorB: Color by assignmentViewModel.checkBoxColorB.observeAsState(Color.Black)
        val checkBoxColorC: Color by assignmentViewModel.checkBoxColorC.observeAsState(Color.Black)
        val checkBoxColorD: Color by assignmentViewModel.checkBoxColorD.observeAsState(Color.Black)

        val favouriteState: Int by assignmentViewModel.favouriteColor.observeAsState(currentQuestion.favourite)

        val optionA: Pair<Pair<Boolean, Color>, String> =
            Pair(Pair(checkedA, checkBoxColorA), selectA)
        val optionB: Pair<Pair<Boolean, Color>, String> =
            Pair(Pair(checkedB, checkBoxColorB), selectB)
        val optionC: Pair<Pair<Boolean, Color>, String> =
            Pair(Pair(checkedC, checkBoxColorC), selectC)
        val optionD: Pair<Pair<Boolean, Color>, String> =
            Pair(Pair(checkedD, checkBoxColorD), selectD)

        //Used in order to create checkboxes
        val selections: List<Pair<Pair<Boolean, Color>, String>> =
            listOf(optionA, optionB, optionC, optionD)



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
                        for (selection in selections) {
                            val checkBoxCheckedState: MutableState<Boolean> =
                                rememberSaveable { mutableStateOf(selection.first.first) }
                            val checkBoxSelection: MutableState<String> =
                                rememberSaveable { mutableStateOf(selection.second) }

                            val currentQuestionText: String =
                                assignmentViewModel.setCurrentQuestionText(
                                    currentQuestion,
                                    selection.second
                                )
                            Row {
                                Checkbox(
                                    /*
                                   * selection.first = <Pair<Boolean, Color> => checkbox state & color
                                   * selection.second = String => selection e.g: [a,b,c,d]
                                   */
                                    checked = selection.first.first,
                                    colors = CheckboxDefaults.colors(selection.first.second),
                                    onCheckedChange = {
                                        assignmentViewModel.onChangeCheckedOption(
                                            checkBoxCheckedState.value,
                                            checkBoxSelection.value
                                        )
                                        assignmentViewModel.onChangeSelection(checkBoxSelection.value)
                                        assignmentViewModel.currentSelection(
                                            checkBoxCheckedState.value,
                                            checkBoxSelection.value
                                        )
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
                        modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Artemis_Blue),
                            //Contains whole logic for further answer processing
                            onClick = {
                                assignmentViewModel.setNavigationButton(
                                    NavigationButton.NEXT_PAGE,
                                    navIndex,
                                    assignmentQuestions
                                )
                            })
                        {
                            Text(
                                color = Color.White,
                                text = "Nächste Frage"
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


