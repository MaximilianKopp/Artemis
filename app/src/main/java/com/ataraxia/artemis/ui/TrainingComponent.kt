package com.ataraxia.artemis.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.data.TrainingViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.helper.NavTrainingButton
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS

class TrainingComponent {

    @Composable
    fun TrainingScreen(
        navController: NavController,
        questionViewModel: QuestionViewModel,
        filter: CriteriaFilter,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit,
        trainingData: List<Question>
    ) {
        val trainingViewModel: TrainingViewModel = viewModel()
        val navIndex: Int by trainingViewModel.index.observeAsState(0)
        val questions: List<Question> by trainingViewModel.trainingQuestions.observeAsState(
            trainingData
        )
        if (questions.size > Constants.TRAINING_SIZE) {
            trainingViewModel.trainingQuestions.postValue(
                questions.shuffled().take(Constants.TRAINING_SIZE)
            )
        }
        if (filter == CriteriaFilter.SINGLE_QUESTION) {
            trainingViewModel.trainingQuestions.postValue(trainingData)
        }
        if (questions.isNotEmpty() && questions.size <= Constants.TRAINING_SIZE) {
            TrainingContent(
                navController = navController,
                trainingViewModel = trainingViewModel,
                questionViewModel = questionViewModel,
                trainingData = questions,
                index = navIndex,
                isTrainingDialogOpen = isTrainingDialogOpen,
                onOpenTrainingDialog = onOpenTrainingDialog
            )
        }
    }

    @Composable
    fun TrainingContent(
        navController: NavController,
        trainingViewModel: TrainingViewModel,
        questionViewModel: QuestionViewModel,
        trainingData: List<Question>,
        index: Int,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit
    ) {
        val context = LocalContext.current
        val checkedAnswers: List<String> = trainingViewModel.checkedAnswers
        val currentQuestion: Question by trainingViewModel.currentQuestion.observeAsState(
            trainingData[0]
        )

        val checkedA: Boolean by trainingViewModel.checkedA.observeAsState(false)
        val checkedB: Boolean by trainingViewModel.checkedB.observeAsState(false)
        val checkedC: Boolean by trainingViewModel.checkedC.observeAsState(false)
        val checkedD: Boolean by trainingViewModel.checkedD.observeAsState(false)

        val selectA: String by trainingViewModel.selectA.observeAsState("a")
        val selectB: String by trainingViewModel.selectB.observeAsState("b")
        val selectC: String by trainingViewModel.selectC.observeAsState("c")
        val selectD: String by trainingViewModel.selectD.observeAsState("d")

        val checkBoxColorA: Color by trainingViewModel.checkBoxColorA.observeAsState(Color.Black)
        val checkBoxColorB: Color by trainingViewModel.checkBoxColorB.observeAsState(Color.Black)
        val checkBoxColorC: Color by trainingViewModel.checkBoxColorC.observeAsState(Color.Black)
        val checkBoxColorD: Color by trainingViewModel.checkBoxColorD.observeAsState(Color.Black)

        val isButtonEnabled: Boolean by trainingViewModel.isButtonEnabled.observeAsState(true)
        val answerBtnText: String by trainingViewModel.answerBtnText.observeAsState("ten")

        val isNavDialogOpen: Boolean by trainingViewModel.isNavDialogOpen.observeAsState(false)

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
            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(1f, fill = true)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(6.dp),
                        text = currentQuestion.text,
                        style = MaterialTheme.typography.body1,
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp)
                ) {
                    Column {
                        //Creates Checkboxes with state (default=unchecked) and value (a,b,c or d)
                        for (selection in selections) {
                            val currentQuestionText: String =
                                trainingViewModel.setCurrentQuestionText(
                                    currentQuestion,
                                    selection.second
                                )
                            Row(
                                Modifier.padding(top = 10.dp, bottom = 10.dp)
                            ) {
                                Checkbox(
                                    /*
                                     * selection.first = <Pair<Boolean, Color> => checkbox state & color
                                     * selection.second = String => selection e.g: [a,b,c,d]
                                     */
                                    checked = selection.first.first,
                                    colors = CheckboxDefaults.colors(selection.first.second),
                                    onCheckedChange = {
                                        if (answerBtnText != "Weiter") {
                                            trainingViewModel.onChangeCheckedOption(
                                                selection.first.first,
                                                selection.second
                                            )
                                            trainingViewModel.onChangeSelection(selection.second)
                                            trainingViewModel.currentSelection(
                                                selection.first.first,
                                                selection.second
                                            )
                                        }
                                    },
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp, end = 2.dp),
                                    text = currentQuestionText,
                                    style = MaterialTheme.typography.caption
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
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavTrainingButton(
                                    NavTrainingButton.FIRST_PAGE,
                                    index,
                                    trainingData
                                )
                            }) {
                            Icon(
                                Icons.Filled.FirstPage,
                                contentDescription = "First page button",
                                modifier = Modifier.size(50.dp),
                                tint = YELLOW_ARTEMIS
                            )
                        }
                        //Loads previous question
                        IconButton(
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavTrainingButton(
                                    NavTrainingButton.PREV_PAGE,
                                    index,
                                    trainingData
                                )
                            }) {
                            Icon(
                                Icons.Filled.ChevronLeft,
                                contentDescription = "Prev question button",
                                modifier = Modifier.size(50.dp),
                                tint = YELLOW_ARTEMIS
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                    ) {
                        Button(
                            enabled = checkedA || checkedB || checkedC || checkedD,
                            //Contains whole logic for further answer processing
                            onClick = {
                                if (answerBtnText == "Antworten") {
                                    trainingViewModel.onChangeAnswerButtonText("Weiter")
                                    if (trainingViewModel.isSelectionCorrect(
                                            currentQuestion,
                                            checkedAnswers,
                                            selections
                                        )
                                    ) {
                                        if (currentQuestion.learnedOnce == 0) {
                                            currentQuestion.learnedOnce = 1
                                            currentQuestion.failed = 0
                                            Log.v(
                                                "LearnedOnce",
                                                currentQuestion.learnedOnce.toString()
                                            )
                                        } else if (currentQuestion.learnedTwice == 0) {
                                            currentQuestion.learnedTwice = 1
                                            currentQuestion.learnedOnce = 0
                                            currentQuestion.failed = 0
                                            Log.v(
                                                "LearnedTwice",
                                                currentQuestion.learnedTwice.toString()
                                            )
                                        }
                                    }
                                    if (!trainingViewModel.isSelectionCorrect(
                                            currentQuestion,
                                            checkedAnswers,
                                            selections
                                        )
                                    ) {
                                        currentQuestion.learnedOnce = 0
                                        currentQuestion.learnedTwice = 0
                                        currentQuestion.failed = 1

                                        @Suppress("DEPRECATION") val vibration: Vibrator =
                                            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                        vibration.vibrate(
                                            VibrationEffect.createOneShot(
                                                200,
                                                VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                        )
                                        Log.v("Failed", currentQuestion.failed.toString())
                                    }
                                    //Saves all changes into database
                                    questionViewModel.updateQuestion(currentQuestion)
                                    trainingViewModel.onChangeEnableButtons(false)

                                }
                                if (answerBtnText == "Weiter") {
                                    trainingViewModel.onChangeEnableButtons(true)
                                    trainingViewModel.loadNextQuestion()
                                    Log.v("Current Question", currentQuestion.correctAnswers)
                                    trainingViewModel.setNavTrainingButton(
                                        NavTrainingButton.NEXT_PAGE,
                                        index,
                                        trainingData
                                    )
                                    if (index == trainingData.size - 1) {
                                        trainingViewModel.onOpenNavDialog(true)
                                    }
                                    trainingViewModel.onChangeAnswerButtonText("Antworten")
                                }
                            }) {
                            Text(text = answerBtnText)
                        }
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 30.dp)
                    ) {
                        BackHandler(enabled = true) {
                            questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS)
                            navController.navigate(Screen.DrawerScreen.Questions.route)
                        }
                        //Loads next question
                        IconButton(
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavTrainingButton(
                                    NavTrainingButton.NEXT_PAGE,
                                    index,
                                    trainingData
                                )
                            }) {
                            Icon(
                                Icons.Filled.ChevronRight,
                                contentDescription = "Next question button",
                                modifier = Modifier.size(50.dp),
                                tint = YELLOW_ARTEMIS
                            )
                        }

                        //Loads last question
                        IconButton(
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavTrainingButton(
                                    NavTrainingButton.LAST_PAGE,
                                    index,
                                    trainingData
                                )
                            }) {
                            Icon(
                                Icons.Filled.LastPage,
                                contentDescription = "Last page button",
                                modifier = Modifier.size(50.dp),
                                tint = YELLOW_ARTEMIS
                            )
                        }
                    }
                }
            }
            Column {
                Text(
                    text = "${index + 1}/${trainingData.size}",
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
        }
        if (isTrainingDialogOpen) {
            AlertDialog(
                onDismissRequest = { onOpenTrainingDialog(false) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Möchtest du das Training abbrechen?",
                            style = MaterialTheme.typography.body1
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
                                onOpenTrainingDialog(false)
                                navController.navigate(Screen.DrawerScreen.Questions.route)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Ja",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                onOpenTrainingDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp)
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
        if (isNavDialogOpen) {
            AlertDialog(
                onDismissRequest = { trainingViewModel.onOpenNavDialog(false) },
                buttons = {
                    Button(onClick = {
                        navController.navigate(Screen.DrawerScreen.Questions.route)
                        questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS)
                    }) {
                        Text(text = "Zurück zum Menü")
                    }
                }
            )
        }
    }
}
