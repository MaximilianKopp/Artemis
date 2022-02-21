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
        val questions: List<Question> by questionViewModel.trainingData.observeAsState(listOf())
        val index: Int by trainingViewModel.index.observeAsState(1)
        if (questions.isNotEmpty()) {
            TrainingContent(
                trainingViewModel = trainingViewModel,
                index = index,
                questions = questions
            )
            TrainingContent(
                trainingViewModel = trainingViewModel,
                index = index,
                questions = questions
            )
        }
    }

    @Composable
    fun TrainingContent(
        trainingViewModel: TrainingViewModel,
        index: Int,
        questions: List<Question>
    ) {
        val currentQuestion: Question by trainingViewModel.currentQuestion.observeAsState(questions[0])
        val checkedAnswers: List<String> = trainingViewModel.currentCheckedAnswersList

        val selectA: String by trainingViewModel.selectA.observeAsState("a")
        val selectB: String by trainingViewModel.selectB.observeAsState("b")
        val selectC: String by trainingViewModel.selectC.observeAsState("c")
        val selectD: String by trainingViewModel.selectD.observeAsState("d")

        val checkedA: Boolean by trainingViewModel.checkedA.observeAsState(false)
        val checkedB: Boolean by trainingViewModel.checkedB.observeAsState(false)
        val checkedC: Boolean by trainingViewModel.checkedC.observeAsState(false)
        val checkedD: Boolean by trainingViewModel.checkedD.observeAsState(false)

        val answerBtnText: String by trainingViewModel.answerBtnText.observeAsState("Antworten")
        val optionCBColor: Color by trainingViewModel.optionCheckBoxColor.observeAsState(Color.Black)

        val optionA: Pair<Boolean, String> = Pair(checkedA, selectA)
        val optionB: Pair<Boolean, String> = Pair(checkedB, selectB)
        val optionC: Pair<Boolean, String> = Pair(checkedC, selectC)
        val optionD: Pair<Boolean, String> = Pair(checkedD, selectD)
        val selections: List<Pair<Boolean, String>> = listOf(optionA, optionB, optionC, optionD)

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
                                    colors = CheckboxDefaults.colors(optionCBColor),
                                    checked = selection.first,
                                    onCheckedChange = {
                                        trainingViewModel.onChangeCurrentCheckedAnswersList(
                                            selection.second
                                        )
                                        trainingViewModel.onChangeCheckedOption(
                                            selection.first,
                                            selection.second
                                        )
                                        trainingViewModel.onChangeSelection(selection.second)

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
                        IconButton(
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
                        IconButton(
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
                            onClick = {
                                if (answerBtnText == "Antworten") {
                                    trainingViewModel.onChangeAnswerButtonText("Weiter")
                                    trainingViewModel.submitCheckedAnswers(
                                        currentQuestion.correctAnswers,
                                        checkedAnswers
                                    )
                                    trainingViewModel.onChangeOptionCheckBoxColor(Color.Green)
                                }
                                if (answerBtnText == "Weiter") {
                                    trainingViewModel.resetQuestion()
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
                        IconButton(
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
                        IconButton(
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

