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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.model.Screen

class QuestionListComponent {

    @Composable
    fun ChapterScreen(
        navController: NavController,
        isFilterDialogOpen: Boolean,
        onOpenFilterDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        questionsByChapter: List<Question>,
        questions: List<Question>,
    ) {
        ChapterContent(
            questionsByChapter,
            questions,
            isFilterDialogOpen,
            onOpenFilterDialog,
            questionViewModel,
            navController
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ChapterContent(
        questionsByChapter: List<Question>,
        questions: List<Question>,
        isDialogOpen: Boolean,
        onOpenDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        LazyColumn {
            stickyHeader {
                Button(
                    enabled = questions.isNotEmpty(),
                    modifier = Modifier.padding(8.dp),
                    onClick = {
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
                                questionViewModel.onChangeQuestionList(questionsByChapter)
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
                                val notLearnedQuestions =
                                    questionsByChapter.filter { it.learnedOnce == 1 }
                                questionViewModel.onChangeQuestionList(notLearnedQuestions)
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
                                val failedQuestions = questionsByChapter.filter { it.failed == 1 }
                                questionViewModel.onChangeQuestionList(failedQuestions)
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
                                val favouriteQuestions =
                                    questionsByChapter.filter { it.favourite == 1 }
                                questionViewModel.onChangeQuestionList(favouriteQuestions)
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