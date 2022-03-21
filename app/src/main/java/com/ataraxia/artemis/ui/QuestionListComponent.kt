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
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Blue
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.TrainingViewModel

@ExperimentalFoundationApi
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

    @Composable
    fun CurrentTopicContent(
        isFilterDialogOpen: Boolean,
        onOpenDialog: (Boolean) -> Unit,
        navController: NavController,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        currentTopic: Int
    ) {
        val filterAbleQuestions =
            questionViewModel.selectTopic(currentTopic, CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
        val questionsLiveData = questionViewModel.questions.observeAsState(filterAbleQuestions)
        val currentFilter = questionViewModel.filter.observeAsState()
        val isVibrating: Int by generalViewModel.isVibrating.observeAsState(1)
        val sizeOfTrainingUnit: Int by generalViewModel.sizeOfTrainingUnit.observeAsState(20)

        Log.v("Current Filter", currentFilter.toString())

        LazyColumn {
            stickyHeader {
                Button(
                    enabled = questionsLiveData.value.isNotEmpty(),
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(Artemis_Blue),
                    onClick = {
                        if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) {
                            val preparedTrainingData = questionsLiveData.value.shuffled().take(
                                sizeOfTrainingUnit
                            )
                            trainingViewModel.onChangeTrainingData(
                                preparedTrainingData
                            )
                            if (preparedTrainingData.isNotEmpty()) {
                                trainingViewModel.onChangeCurrentQuestion(preparedTrainingData[0])
                            } // no else
                        } else {
                            trainingViewModel.onChangeTrainingData(questionsLiveData.value)
                        }
                        navController.navigate(Screen.DrawerScreen.Training.route)
                    }) {
                    Text(
                        color = Color.White,
                        text = "Training starten",
                    )
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
                                            contentDescription = "Icon for learned questions",
                                            modifier = Modifier.size(20.dp),
                                            tint = questionViewModel.setQuestionStateColor(question)
                                        )
                                        //Icon for failed questions
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "Icon for failed questions",
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
        if (isFilterDialogOpen) {
            AlertDialog(
                contentColor = Color.White,
                backgroundColor = Artemis_Green,
                onDismissRequest = { onOpenDialog(false) },
                buttons = {
                    Column(
                        Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Filterauswahl",
                                style = MaterialTheme.typography.h4
                            )
                            IconButton(onClick = {
                                onOpenDialog(false)
                            }) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Closes the filter dialog"
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Vibrieren bei falscher Antwort",
                                style = MaterialTheme.typography.body1
                            )
                            Switch(
                                checked = isVibrating == 1,
                                onCheckedChange = {
                                    if (isVibrating == 1) generalViewModel.onChangeEnableVibration(
                                        0
                                    ) else generalViewModel.onChangeEnableVibration(1)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Artemis_Yellow,
                                    checkedTrackColor = Artemis_Yellow,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Artemis_Yellow
                                )
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Trainingsumfang",
                                style = MaterialTheme.typography.body1
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    if (sizeOfTrainingUnit != Constants.SIZE_PER_TRANINIG_UNIT_MIN) {
                                        generalViewModel.onChangeSizeOfTrainingUnit(
                                            sizeOfTrainingUnit - 10
                                        )
                                    } // no else
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronLeft,
                                        contentDescription = "Left arrow for determine size of training unit"
                                    )
                                }
                                Text(text = "$sizeOfTrainingUnit")
                                IconButton(onClick = {
                                    if (sizeOfTrainingUnit < Constants.SIZE_PER_TRAINING_UNIT_MAX) {
                                        generalViewModel.onChangeSizeOfTrainingUnit(
                                            sizeOfTrainingUnit + 10
                                        )
                                    } // no else
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronRight,
                                        contentDescription = "Right arrow for determine size of training unit"
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                //Take all questions
                                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions)
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
                                text = "Zufallswiedergabe ($sizeOfTrainingUnit Fragen)",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                //Take all questions in chronological order
                                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_CHRONOLOGICAL)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions)
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),

                            ) {
                            Text(
                                color = Color.Black,
                                text = "Alle Fragen (1- ${filterAbleQuestions.size})",
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
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
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
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
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
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
                                text = "Favouriten",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
        }
        BackHandler(enabled = true) {
            questionViewModel.onChangeQuestionList(filterAbleQuestions)
            navController.navigate(Screen.DrawerScreen.Questions.route)
        }
    }
}