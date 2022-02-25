package com.ataraxia.artemis.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.model.Screen

class QuestionListComponent {

    @Composable
    fun ChapterScreen(
        chapter: String,
        navController: NavController,
        isDialogOpen: Boolean,
        onOpenDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel
    ) {
        val renewQuestions: List<Question> = questionViewModel.selectChapter(chapter)
        val questions: List<Question> by questionViewModel.questions.observeAsState(
            renewQuestions
        )
        ChapterContent(
            renewQuestions,
            questions,
            isDialogOpen,
            onOpenDialog,
            questionViewModel,
            navController,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ChapterContent(
        renewQuestions: List<Question>,

        questions: List<Question>,
        isDialogOpen: Boolean,
        onOpenDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        val filterCriteria: String by questionViewModel.filterCriteria.observeAsState(Constants.FILTER_CRITERIA_ALL)
        LazyColumn {
            stickyHeader {
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        questionViewModel.prepareTrainingData(renewQuestions, filterCriteria)
                        navController.navigate(Screen.DrawerScreen.Training.route)
                    }) {
                    Text(text = "Training starten")
                }
            }
            items(questions) { question ->
                Card(
                    backgroundColor = Color.White,
                    modifier = Modifier.padding(4.dp)
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
                                questionViewModel.filterCriteria.postValue(Constants.FILTER_CRITERIA_ALL)
                                questionViewModel.questions.postValue(renewQuestions.filter { it.isSelected == 1 })
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
                                questionViewModel.onUpdateList(renewQuestions.filter { it.learnedOnce == 1 && it.learnedTwice == 0 })
                                questionViewModel.filterCriteria.postValue(Constants.FILTER_CRITERIA_NOT_LEARNED)
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
                                questionViewModel.onUpdateList(renewQuestions.filter { it.failed == 1 })
                                questionViewModel.filterCriteria.postValue(Constants.FILTER_CRITERIA_FAILED)
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
                                questionViewModel.onUpdateList(renewQuestions.filter { it.favourite == 1 })
                                questionViewModel.filterCriteria.postValue(Constants.FILTER_CRITERIA_FAVOURITES)
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
    }
}