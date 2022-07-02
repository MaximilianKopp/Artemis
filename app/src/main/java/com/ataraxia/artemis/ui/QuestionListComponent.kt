package com.ataraxia.artemis.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
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
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.QuestionProjection
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Blue
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.TrainingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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
        val searchBarText: String by generalViewModel.searchTextState
        val sizeOfTrainingUnit: Int by generalViewModel.sizeOfTrainingUnit.observeAsState(20)
        Log.v("Current Searchtext", searchBarText)


        if (currentFilter.value == CriteriaFilter.SEARCH) {
            questionViewModel.onChangeQuestionList(
                filterAbleQuestions.filter {
                    it.text.contains(searchBarText, true)
                        .or(it.optionA.contains(searchBarText, true))
                        .or(it.optionB.contains(searchBarText, true))
                        .or(it.optionC.contains(searchBarText, true))
                        .or(it.optionD.contains(searchBarText, true))
                })
        }
        if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) {
            questionViewModel.onChangeQuestionList(filterAbleQuestions)
        }

        Log.v("Current Filter", currentFilter.toString())

        LazyColumn {
            stickyHeader {
                Row {
                    Button(
                        enabled = questionsLiveData.value.isNotEmpty(),
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(Artemis_Blue),
                        onClick = {
                            val preparedTrainingData: List<QuestionProjection>
                            if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) {
                                preparedTrainingData =
                                    questionsLiveData.value.shuffled().take(sizeOfTrainingUnit)
                                trainingViewModel.onChangeTrainingData(preparedTrainingData)
                                trainingViewModel.onChangeCurrentQuestion(preparedTrainingData[0])
                            } else {
                                preparedTrainingData = questionsLiveData.value
                                trainingViewModel.onChangeTrainingData(questionsLiveData.value)
                                trainingViewModel.onChangeCurrentQuestion(preparedTrainingData[0])
                            }
                            generalViewModel.onChangeSearchWidgetState(false)
                            generalViewModel.onHideSearchWidget(
                                Pair(
                                    Constants.ALPHA_INVISIBLE,
                                    Constants.DISABLED
                                )
                            )
                            trainingViewModel.onChangeIndex(0)
                            generalViewModel.onChangeSearchWidgetState(false)
                            generalViewModel.onChangeCurrentScreen(Screen.DrawerScreen.Training)
                            navController.navigate(Screen.DrawerScreen.Training.route)
                        }) {
                        Column {
                            Text(
                                color = Color.White,
                                text = "Training starten (${
                                    if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) sizeOfTrainingUnit else questionsLiveData.value.count()
                                } Fragen)"
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                color = Color.White,
                                text = "2x richtig: ${filterAbleQuestions.count { it.learnedTwice == 1 }}/${filterAbleQuestions.count()}",
                                style = MaterialTheme.typography.overline
                            )
                            Text(
                                color = Color.White,
                                text = "1x richtig: ${filterAbleQuestions.count { it.learnedOnce == 1 }}",
                                style = MaterialTheme.typography.overline
                            )
                            Text(
                                color = Color.White,
                                text = "Fehler: ${filterAbleQuestions.count { it.failed == 1 }}",
                                style = MaterialTheme.typography.overline
                            )
                            Text(
                                color = Color.White,
                                text = "Noch nicht gelernt: ${filterAbleQuestions.count { it.learnedTwice == 0 }}",
                                style = MaterialTheme.typography.overline
                            )
                        }
                    }
                }
                Column(
                    Modifier
                        .padding(8.dp)
                ) {

                }
            }
            items(questionsLiveData.value) { question ->
                val isFavourite: MutableState<Int> =
                    rememberSaveable { mutableStateOf(question.favourite) }
                Card(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            generalViewModel.onChangeSearchWidgetState(false)
                            generalViewModel.onHideSearchWidget(
                                Pair(
                                    Constants.ALPHA_INVISIBLE,
                                    Constants.DISABLED
                                )
                            )
                            generalViewModel.onChangeCurrentScreen(Screen.DrawerScreen.Training)
                            questionViewModel.onChangeFilter(CriteriaFilter.SINGLE_QUESTION)
                            trainingViewModel.onChangeTrainingData(listOf(question))
                            trainingViewModel.onChangeCurrentQuestion(question)
                            navController.navigate(Screen.DrawerScreen.Training.route)
                        }
                ) {
                    Column {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Favourite Icon",
                                Modifier
                                    .size(26.dp)
                                    .padding(top = 8.dp),
                                tint = if (isFavourite.value == 1) Color.Yellow else Color.Black,
                            )
                            Text(
                                text = question.text,
                                Modifier.padding(start = 5.dp, top = 8.dp)
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
            val scrollState = rememberScrollState()
            AlertDialog(
                contentColor = Color.White,
                backgroundColor = Artemis_Green,
                onDismissRequest = { onOpenDialog(false) },
                modifier = Modifier.verticalScroll(scrollState, true),
                shape = RoundedCornerShape(CornerSize(25.dp)),
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
                                    if (sizeOfTrainingUnit != Constants.SIZE_PER_TRAINING_UNIT_MIN) {
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
                                questionViewModel.onChangeQuestionList(filterAbleQuestions.filter { it.learnedOnce == 0 && it.learnedTwice == 0 })
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
                                text = "Noch nicht gelernt (${filterAbleQuestions.count { it.learnedOnce == 0 && it.learnedTwice == 0 }})",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                //Take all once learned questions
                                questionViewModel.onChangeFilter(CriteriaFilter.ONCE_LEARNED)
                                questionViewModel.onChangeQuestionList(filterAbleQuestions.filter { it.learnedOnce == 1 && it.learnedTwice == 0 })
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
                                text = "Mind. 1x richtig beantwortet (${filterAbleQuestions.count { it.learnedOnce == 1 && it.learnedTwice == 0 }})",
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
                                text = "Falsch beantwortet (${filterAbleQuestions.count { it.failed == 1 }})",
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
                                text = "Favouriten (${filterAbleQuestions.count { it.favourite == 1 }})",
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Button(
                            onClick = {
                                Log.v("Last seen", LocalDateTime.now().minusWeeks(1L).toString())
                                //Take all questions that have been longer than one week not viewed
                                questionViewModel.onChangeFilter(CriteriaFilter.LAST_VIEWED)
                                questionViewModel.onChangeQuestionList(
                                    filterAbleQuestions.filter {
                                        LocalDateTime.parse(
                                            it.lastViewed,
                                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                                .withLocale(Locale("DE"))
                                        )
                                            .isBefore(LocalDateTime.now().minusWeeks(1L))
                                    }
                                        .sortedWith(Comparator.comparing { LocalDateTime.parse(it.lastViewed) })
                                )
                                onOpenDialog(false)
                            },
                            Modifier
                                .width(300.dp)
                                .padding(4.dp),
                            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
                        ) {
                            Text(
                                color = Color.Black,
                                text = "Längere Zeit nicht angesehen (${
                                    filterAbleQuestions.count {
                                        LocalDateTime.parse(
                                            it.lastViewed,
                                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                                .withLocale(Locale("DE"))
                                        )
                                            .isBefore(LocalDateTime.now().minusWeeks(1L))
                                    }
                                })",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
        }
    }
}