package com.ataraxia.artemis.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.data.GeneralViewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.data.TrainingViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Purple700

class QuestionListComponent {

    @Composable
    fun CurrentTopicScreen(
        navController: NavController,
        isFilterDialogOpen: Boolean,
        onOpenFilterDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        currentTopic: Int
    ) {
        CurrentTopicContent(
            isFilterDialogOpen,
            onOpenFilterDialog,
            navController,
            questionViewModel,
            trainingViewModel,
            generalViewModel,
            currentTopic
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CurrentTopicContent(
        isDialogOpen: Boolean,
        onOpenDialog: (Boolean) -> Unit,
        navController: NavController,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        currentTopic: Int
    ) {
        val filterAbleQuestions =
            questionViewModel.selectTopic(currentTopic, CriteriaFilter.ALL_QUESTIONS)
        val questionsLiveData = questionViewModel.questions.observeAsState(filterAbleQuestions)
        val currentFilter = questionViewModel.filter.observeAsState()

        Log.v("Current Filter", currentFilter.toString())

        LazyColumn {
            stickyHeader {
                Button(
                    enabled = questionsLiveData.value.isNotEmpty(),
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(Purple700),
                    onClick = {
                        if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS) {
                            val preparedTrainingData = questionsLiveData.value.shuffled().take(
                                Constants.TRAINING_SIZE
                            )
                            trainingViewModel.onChangeTrainingData(
                                preparedTrainingData
                            )
                            if (preparedTrainingData.isNotEmpty()) {
                                trainingViewModel.onChangeCurrentQuestion(preparedTrainingData[0])
                            }
                        } else {
                            trainingViewModel.onChangeTrainingData(questionsLiveData.value)
                        }
                        navController.navigate(Screen.DrawerScreen.Training.route)
                    }) {
                    Text(text = "Training starten")
                }
            }
            items(questionsLiveData.value) { question ->
                Card(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            questionViewModel.onChangeFilter(CriteriaFilter.SINGLE_QUESTION)
                            trainingViewModel.onChangeTrainingData(listOf(question))
                            trainingViewModel.onChangeCurrentQuestion(question)
                            navController.navigate(Screen.DrawerScreen.Training.route)
                        }
                ) {
                    Column {
                        Row {
                            Text(
                                text = question.text,
                                Modifier.padding(start = 3.dp)
                            )
                        }
                        Row {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Box(modifier = Modifier.padding(end = 4.dp)) {
                                    Row {
                                        //Icon for learned questions
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "",
                                            modifier = Modifier.size(20.dp),
                                            tint = questionViewModel.setQuestionStateColor(question)
                                        )
                                        //Icon for failed questions
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "",
                                            modifier = Modifier.size(20.dp),
                                            tint = if (question.failed == 1) {
                                                Color.Red
                                            } else {
                                                Color.Black
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = { onOpenDialog(false) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Filterauswahl",
                            style = MaterialTheme.typography.h4
                        )
                        Divider(thickness = 2.dp)
                        Text(
                            text = "Der Filter legt fest, welche Fragen vorausgewählt werden:",
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
                                //Take all questions
                                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions)
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Alle Fragen",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                //Take all not learned questions
                                questionViewModel.onChangeFilter(CriteriaFilter.NOT_LEARNED)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions.filter { it.learnedOnce == 1 })
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Noch nicht gelernt",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                //Take all failed questions
                                questionViewModel.onChangeFilter(CriteriaFilter.FAILED)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions.filter { it.failed == 1 })
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Falsch beantwortet",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                //Take all questions marked as favourite
                                questionViewModel.onChangeFilter(CriteriaFilter.FAVOURITES)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions.filter { it.favourite == 1 })
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Favouriten",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
        }
        BackHandler(enabled = true) {
//            navController.navigate(Screen.DrawerScreen.Questions.route)
        }
    }
}