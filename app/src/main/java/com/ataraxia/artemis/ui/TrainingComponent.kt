package com.ataraxia.artemis.ui

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.data.TrainingViewModel
import com.ataraxia.artemis.helper.NavTrainingButton
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS

class TrainingComponent {

    @Composable
    fun TrainingScreen(questionViewModel: QuestionViewModel) {
        val trainingViewModel: TrainingViewModel = viewModel()
        val index: Int by trainingViewModel.index.observeAsState(0)
        val questions: List<Question> by questionViewModel.trainingData.observeAsState(listOf())
        if (questions.isNotEmpty()) {
            TrainingContent(
                trainingViewModel = trainingViewModel,
                questions = questions,
                index = index
            )
        }
    }

    @Composable
    fun TrainingContent(
        trainingViewModel: TrainingViewModel,
        questions: List<Question>,
        index: Int
    ) {
        val currentQuestion: Question by trainingViewModel.currentQuestion.observeAsState(questions[0])
        val checkedAnswers: List<String> = trainingViewModel.checkedAnswers

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
        val answerBtnText: String by trainingViewModel.answerBtnText.observeAsState("Antworten")

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
                        style = MaterialTheme.typography.h6,
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
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = trainingViewModel.setCurrentQuestionText(
                                        currentQuestion,
                                        selection.second
                                    ),
                                    style = if (currentQuestionText.length < 150) MaterialTheme.typography.h6 else MaterialTheme.typography.subtitle2
                                )
                            }
                        }
                    }
                }
            }
            Column {
                Row {
                    Row(
                        modifier = Modifier.padding(bottom = 30.dp, end = 30.dp)
                    ) {
                        //Loads first question
                        IconButton(
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavTrainingButton(
                                    NavTrainingButton.FIRST_PAGE,
                                    index,
                                    questions
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
                                    questions
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
                        modifier = Modifier.padding(top = 5.dp)
                    ) {
                        Button(
                            //Contains whole logic for further anwer processing
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
                                            Log.v(
                                                "LearnedOnce",
                                                currentQuestion.learnedOnce.toString()
                                            )
                                        } else if (currentQuestion.learnedTwice == 0) {
                                            currentQuestion.learnedTwice = 1
                                            Log.v(
                                                "LearnedTwice",
                                                currentQuestion.learnedTwice.toString()
                                            )
                                        }
                                    } else if (!trainingViewModel.isSelectionCorrect(
                                            currentQuestion,
                                            checkedAnswers,
                                            selections
                                        )
                                    ) {
                                        currentQuestion.failed = 1
                                        Log.v("Failed", currentQuestion.failed.toString())
                                    }
                                    trainingViewModel.onChangeEnableButtons(false)
                                }
                                if (answerBtnText == "Weiter") {
                                    trainingViewModel.onChangeEnableButtons(true)
                                    trainingViewModel.loadNextQuestion()
                                    Log.v("Current Question", currentQuestion.correctAnswers)
                                    trainingViewModel.setNavTrainingButton(
                                        NavTrainingButton.NEXT_PAGE,
                                        index,
                                        questions
                                    )
                                    trainingViewModel.onChangeAnswerButtonText("Antworten")

                                }
                            }) {
                            Text(text = answerBtnText)
                        }
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 30.dp, start = 30.dp)
                    ) {
                        //Loads next question
                        IconButton(
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavTrainingButton(
                                    NavTrainingButton.NEXT_PAGE,
                                    index,
                                    questions
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
                                    questions
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
        }
    }
}

