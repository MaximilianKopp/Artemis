package com.artemis.hunterexam.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.CriteriaFilter
import com.artemis.hunterexam.model.QuestionProjection
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.ui.theme.Artemis_Blue
import com.artemis.hunterexam.ui.theme.Artemis_Green
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import com.artemis.hunterexam.viewModel.GeneralViewModel
import com.artemis.hunterexam.viewModel.QuestionViewModel
import com.artemis.hunterexam.viewModel.TrainingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@ExperimentalComposeUiApi
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
        val questionsByTopicAndFilter =
            questionViewModel.selectTopic(currentTopic, CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
        val questionsLiveData =
            questionViewModel.questions.observeAsState(questionsByTopicAndFilter)
        val currentFilter =
            questionViewModel.filter.observeAsState(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
        val sizeOfTrainingUnit: Int by generalViewModel.sizeOfTrainingUnit.observeAsState(20)

        CheckSearchbarState(
            currentFilter,
            generalViewModel,
            questionViewModel,
            questionsByTopicAndFilter
        )
        LoadQuestionList(
            questionViewModel,
            currentFilter,
            questionsLiveData,
            sizeOfTrainingUnit,
            trainingViewModel,
            generalViewModel,
            navController,
            questionsByTopicAndFilter
        )
        if (isFilterDialogOpen) {
            ShowFilterAlertDialog(
                onOpenDialog,
                generalViewModel,
                sizeOfTrainingUnit,
                questionViewModel,
                questionsByTopicAndFilter
            )
        }
        BackHandler(enabled = true) {
            navController.navigate(Screen.DrawerScreen.QuestionCatalogue.route)
        }
    }

    @Composable
    private fun LoadQuestionList(
        questionViewModel: QuestionViewModel,
        currentFilter: State<CriteriaFilter>,
        questionsLiveData: State<List<QuestionProjection>>,
        sizeOfTrainingUnit: Int,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        navController: NavController,
        questionsByTopicAndFilter: List<QuestionProjection>
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val keyboardFocus = LocalFocusManager.current
        LazyColumn(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        keyboardFocus.clearFocus(true)
                    }
                )
            }
        ) {
            stickyHeader {
                ShowStickyHeaderContent(
                    questionViewModel,
                    currentFilter,
                    questionsLiveData,
                    sizeOfTrainingUnit,
                    trainingViewModel,
                    generalViewModel,
                    navController,
                    questionsByTopicAndFilter
                )
            }
            items(questionsLiveData.value) { question ->
                LoadQuestionItems(
                    question,
                    generalViewModel,
                    questionViewModel,
                    trainingViewModel,
                    navController
                )
            }
        }
    }

    @Composable
    private fun ShowStickyHeaderContent(
        questionViewModel: QuestionViewModel,
        currentFilter: State<CriteriaFilter>,
        questionsLiveData: State<List<QuestionProjection>>,
        sizeOfTrainingUnit: Int,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        navController: NavController,
        questionsByTopicAndFilter: List<QuestionProjection>
    ) {
        Row {
            Card(
                backgroundColor = Artemis_Yellow,
                modifier = Modifier
                    .padding(15.dp)
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp)),
                shape = RoundedCornerShape(15.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = questionViewModel.getCurrentCriteriaFilter(currentFilter.value),
                        style = MaterialTheme.typography.body2
                    )
                    Button(
                        enabled = questionsLiveData.value.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(Artemis_Blue),
                        onClick = {
                            startTraining(
                                currentFilter,
                                questionsLiveData,
                                sizeOfTrainingUnit,
                                trainingViewModel,
                                generalViewModel,
                                navController
                            )
                        }) {
                        Icon(
                            Icons.Filled.ArrowRight,
                            contentDescription = "Start button icon"
                        )
                        Text(
                            color = Color.White,
                            text = "Training starten (${
                                if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) sizeOfTrainingUnit else questionsLiveData.value.count()
                            } Fragen)",
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Text(
                        color = Color.Black,
                        text = "2x richtig: ${questionsByTopicAndFilter.count { it.learnedTwice == 1 }}/${questionsByTopicAndFilter.count()}",
                        style = MaterialTheme.typography.overline
                    )
                    Text(
                        color = Color.Black,
                        text = "1x richtig: ${questionsByTopicAndFilter.count { it.learnedOnce == 1 }}",
                        style = MaterialTheme.typography.overline
                    )
                    Text(
                        color = Color.Black,
                        text = "Fehler: ${questionsByTopicAndFilter.count { it.failed == 1 }}",
                        style = MaterialTheme.typography.overline
                    )
                    Text(
                        color = Color.Black,
                        text = "Noch nicht gelernt: ${questionsByTopicAndFilter.count { it.learnedTwice == 0 }}",
                        style = MaterialTheme.typography.overline
                    )
                }
            }
        }
    }

    private fun startTraining(
        currentFilter: State<CriteriaFilter>,
        questionsLiveData: State<List<QuestionProjection>>,
        sizeOfTrainingUnit: Int,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        navController: NavController
    ) {
        val preparedTrainingData: List<QuestionProjection>
        if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) {
            preparedTrainingData =
                questionsLiveData.value.shuffled()
                    .take(sizeOfTrainingUnit)
            trainingViewModel.onChangeTrainingData(preparedTrainingData)
            generalViewModel.onChangeCurrentQuestion(
                preparedTrainingData[0]
            )
        } else {
            preparedTrainingData = questionsLiveData.value
            trainingViewModel.onChangeTrainingData(questionsLiveData.value)
            generalViewModel.onChangeCurrentQuestion(
                preparedTrainingData[0]
            )
        }
        generalViewModel.onChangeSearchWidgetState(false)
        generalViewModel.onHideSearchWidget(
            Pair(
                Constants.ALPHA_INVISIBLE,
                Constants.DISABLED
            )
        )
        generalViewModel.onChangeIndex(0)
        generalViewModel.onChangeSearchWidgetState(false)
        generalViewModel.onChangeCurrentScreen(Screen.DrawerScreen.Training)
        navController.navigate(Screen.DrawerScreen.Training.route)
    }

    @Composable
    private fun LoadQuestionItems(
        question: QuestionProjection,
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        navController: NavController
    ) {
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
                    generalViewModel.onChangeCurrentQuestion(question)
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
                        tint = question.favouriteState.value
                    )
                    Text(
                        text = question.text,
                        Modifier.padding(start = 5.dp, top = 8.dp)
                    )
                }
                ShowStateSymbolOfQuestion(question, questionViewModel)
            }
        }
    }

    @Composable
    private fun ShowStateSymbolOfQuestion(
        question: QuestionProjection,
        questionViewModel: QuestionViewModel
    ) {
        Row {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(modifier = Modifier.padding(end = 4.dp)) {
                    Row {
                        Text(
                            text = if (question.lastViewed == Constants.LAST_SEEN_DEFAULT) Constants.EMPTY_STRING else "Zuletzt angesehen am ${question.lastViewed}",
                            style = MaterialTheme.typography.caption,
                            fontStyle = FontStyle.Italic
                        )
                        //Icon for learned questions
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Icon for learned questions",
                            modifier = Modifier.size(20.dp),
                            tint = questionViewModel.setQuestionStateColor(
                                question
                            )
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

    @Composable
    private fun ShowFilterAlertDialog(
        onOpenDialog: (Boolean) -> Unit,
        generalViewModel: GeneralViewModel,
        sizeOfTrainingUnit: Int,
        questionViewModel: QuestionViewModel,
        questionsByTopicAndFilter: List<QuestionProjection>
    ) {
        val scrollState = rememberScrollState()
        AlertDialog(
            contentColor = Color.White,
            backgroundColor = Artemis_Green,
            onDismissRequest = { onOpenDialog(false) },
            modifier = Modifier.verticalScroll(scrollState, true),
            shape = RoundedCornerShape(CornerSize(25.dp)),
            buttons = {
                Column(
                    Modifier.padding(8.dp),
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
                    SetTrainingConfiguration(
                        generalViewModel,
                        sizeOfTrainingUnit
                    )
                }
                Column(
                    Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CurrentTrainingFilter(
                        questionViewModel,
                        questionsByTopicAndFilter,
                        onOpenDialog,
                        sizeOfTrainingUnit
                    )
                }
            }
        )
    }

    @Composable
    private fun SetTrainingConfiguration(
        generalViewModel: GeneralViewModel,
        sizeOfTrainingUnit: Int
    ) {
        val isVibrating: Int by generalViewModel.isVibrating.observeAsState(1)
        val isHintShown: Int by generalViewModel.isHintShow.observeAsState(1)

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
            ShowHints(isHintShown, generalViewModel)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SetSizeOfTrainingUnit(sizeOfTrainingUnit, generalViewModel)
        }
    }

    @Composable
    private fun SetFilterButton(
        text: String,
        onChangeFilter: () -> Unit,
        onChangeQuestionList: () -> Unit,
        onOpenDialog: (Boolean) -> Unit,
    ) {
        Button(
            onClick = {
                //Take all questions
                onChangeFilter()
                onChangeQuestionList()
                onOpenDialog(false)
            },
            Modifier
                .width(300.dp)
                .padding(4.dp),
            colors = ButtonDefaults.buttonColors(Artemis_Yellow),
        ) {
            Text(
                color = Color.Black,
                text = text,
                style = MaterialTheme.typography.body1
            )
        }
    }

    @Composable
    private fun CurrentTrainingFilter(
        questionViewModel: QuestionViewModel,
        questionsByTopicAndFilter: List<QuestionProjection>,
        onOpenDialog: (Boolean) -> Unit,
        sizeOfTrainingUnit: Int
    ) {

        //Take questions by random and size of training unit amount
        SetFilterButton(
            text = "Zufallswiedergabe ($sizeOfTrainingUnit Fragen)",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter
                )
            },
            onOpenDialog = { onOpenDialog(false) }
        )

        //Take all questions in chronological order
        SetFilterButton(
            text = "Alle Fragen (1- ${questionsByTopicAndFilter.size})",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_CHRONOLOGICAL) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter
                )
            },
            onOpenDialog = { onOpenDialog(false) }
        )

        //Take all not learned questions
        SetFilterButton(
            text = "Noch nicht gelernt (${questionsByTopicAndFilter.count { it.learnedOnce == 0 && it.learnedTwice == 0 }})",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.NOT_LEARNED) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter.filter { it.learnedOnce == 0 && it.learnedTwice == 0 })
            },
            onOpenDialog = { onOpenDialog(false) }
        )

        //Take all once learned questions
        SetFilterButton(
            text = "Mind. 1x richtig beantwortet (${questionsByTopicAndFilter.count { it.learnedOnce == 1 && it.learnedTwice == 0 }})",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.ONCE_LEARNED) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter.filter { it.learnedOnce == 1 && it.learnedTwice == 0 })
            },
            onOpenDialog = { onOpenDialog(false) }
        )

        //Take all failed questions
        SetFilterButton(
            text = "Falsch beantwortet (${questionsByTopicAndFilter.count { it.failed == 1 }})",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.FAILED) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter.filter { it.failed == 1 })
            },
            onOpenDialog = { onOpenDialog(false) }
        )

        //Take all questions marked as favourite
        SetFilterButton(
            text = "Favouriten (${questionsByTopicAndFilter.count { it.favourite == 1 }})",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.FAVOURITES) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter.filter { it.favourite == 1 })
            },
            onOpenDialog = { onOpenDialog(false) }
        )

        //Take all questions that have been longer than one week not viewed
        SetFilterButton(
            text = "Seit 1 Woche nicht angesehen (${
                questionsByTopicAndFilter.count {
                    LocalDateTime.parse(
                        it.lastViewed,
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(Locale("DE"))
                    )
                        .isBefore(LocalDateTime.now().minusWeeks(1L))
                }
            })",
            onChangeFilter = { questionViewModel.onChangeFilter(CriteriaFilter.LAST_VIEWED) },
            onChangeQuestionList = {
                questionViewModel.onChangeQuestionList(
                    questionsByTopicAndFilter.filter {
                        LocalDateTime.parse(
                            it.lastViewed, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                .withLocale(Locale("DE"))
                        )
                            .isBefore(LocalDateTime.now().minusWeeks(1L))
                    }
                        .sortedWith(Comparator.comparing {
                            LocalDateTime.parse(
                                it.lastViewed,
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                    .withLocale(Locale("DE"))
                            )
                        }
                        )
                )
            },
            onOpenDialog = { onOpenDialog(false) }
        )
    }

    @Composable
    private fun SetSizeOfTrainingUnit(
        sizeOfTrainingUnit: Int,
        generalViewModel: GeneralViewModel
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

    @Composable
    private fun ShowHints(
        isHintShown: Int,
        generalViewModel: GeneralViewModel
    ) {
        Text(
            text = "Hinweise einblenden",
            style = MaterialTheme.typography.body1
        )
        Switch(
            checked = isHintShown == 1,
            onCheckedChange = {
                if (isHintShown == 1) generalViewModel.onChangeShowHints(
                    0
                ) else generalViewModel.onChangeShowHints(1)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Artemis_Yellow,
                checkedTrackColor = Artemis_Yellow,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Artemis_Yellow
            )
        )
    }

    @Composable
    private fun CheckSearchbarState(
        currentFilter: State<CriteriaFilter>,
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        questionsByTopicAndFilter: List<QuestionProjection>,
    ) {
        val searchBarText: String by generalViewModel.searchTextState
        if (currentFilter.value == CriteriaFilter.SEARCH) {
            questionViewModel.onChangeQuestionList(
                questionsByTopicAndFilter.filter {
                    it.text.contains(searchBarText, true)
                        .or(it.optionA.contains(searchBarText, true))
                        .or(it.optionB.contains(searchBarText, true))
                        .or(it.optionC.contains(searchBarText, true))
                        .or(it.optionD.contains(searchBarText, true))
                })
        }
        if (currentFilter.value == CriteriaFilter.ALL_QUESTIONS_SHUFFLED) {
            questionViewModel.onChangeQuestionList(questionsByTopicAndFilter)
        }
        Log.v("Current Filter", currentFilter.toString())
    }
}