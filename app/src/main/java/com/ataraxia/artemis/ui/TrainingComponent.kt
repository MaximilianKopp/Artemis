package com.ataraxia.artemis.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.helper.NavigationButton
import com.ataraxia.artemis.model.QuestionProjection
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Blue
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.StatisticViewModel
import com.ataraxia.artemis.viewModel.TrainingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class TrainingComponent {

    @Composable
    fun TrainingScreen(
        navController: NavController,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel,
        statisticViewModel: StatisticViewModel,
    ) {
        val navIndex: Int by trainingViewModel.index.observeAsState(0)
        val currentFilter = questionViewModel.filter.observeAsState()
        val trainingData = trainingViewModel.trainingData.observeAsState(listOf())

        Log.v("Current TrainingData", trainingData.value.forEach(::println).toString())
        Log.v("Current Filter", currentFilter.value.toString())

        if (trainingData.value.isNotEmpty()) {
            TrainingContent(
                navController = navController,
                trainingViewModel = trainingViewModel,
                questionViewModel = questionViewModel,
                generalViewModel = generalViewModel,
                statisticViewModel = statisticViewModel,
                trainingData = trainingData.value,
                index = navIndex,
                isTrainingDialogOpen = isTrainingDialogOpen,
                onOpenTrainingDialog = onOpenTrainingDialog,
            )
        }
    }

    @Composable
    fun TrainingContent(
        navController: NavController,
        trainingViewModel: TrainingViewModel,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        statisticViewModel: StatisticViewModel,
        trainingData: List<QuestionProjection>,
        index: Int,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit,
    ) {
        val context = LocalContext.current
        val currentQuestion: QuestionProjection by trainingViewModel.currentQuestion.observeAsState(
            trainingData[0]
        )
        val isNavButtonEnabled: Boolean by trainingViewModel.isButtonEnabled.observeAsState(true)
        val isAnswerButtonEnabled = remember { mutableStateOf(false) }
        val answerBtnText: String by trainingViewModel.answerBtnText.observeAsState("Antworten")

        val favouriteState: Int by trainingViewModel.favouriteColor.observeAsState(currentQuestion.favourite)
        val currentTopic = questionViewModel.currentTopic.value
        val loadScreen =
            currentTopic?.let { generalViewModel.loadScreenByTopic(it) }
        Log.v("Current Topic", currentTopic.toString())
        val renewQuestions =
            currentTopic?.let {
                questionViewModel.selectTopic(
                    it,
                    CriteriaFilter.ALL_QUESTIONS_SHUFFLED
                )
            }

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
                                trainingViewModel.onChangeFavouriteState(currentQuestion.favourite)
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
                                color = Color.Black
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
                        for (checkbox in currentQuestion.checkboxList) {
                            val checkedState =
                                remember { mutableStateOf(checkbox.checked) }
                            val currentQuestionText: String =
                                trainingViewModel.setCurrentQuestionText(
                                    currentQuestion,
                                    checkbox.option
                                )
                            Log.v("CheckedState", checkedState.value.toString())
                            Row(
                                Modifier.padding(top = 10.dp, bottom = 10.dp)
                            ) {
                                val isChecked =
                                    if (answerBtnText == "Antworten") {
                                        trainingViewModel.checkStates(
                                            currentQuestion,
                                            checkbox,
                                            checkedState
                                        )
                                    } else {
                                        true
                                    }
                                val checkBoxColor: CheckboxColors =
                                    if (answerBtnText == "Antworten") {
                                        CheckboxDefaults.colors(checkbox.color)
                                    } else {
                                        CheckboxDefaults.colors(
                                            trainingViewModel.changeColorByResult(
                                                currentQuestion,
                                                checkbox
                                            )
                                        )
                                    }
                                Checkbox(
                                    checked = isChecked,
                                    colors = checkBoxColor,
                                    onCheckedChange = {
                                        if (answerBtnText != "Weiter") {
                                            trainingViewModel.onChangeCheckboxes(
                                                checkbox,
                                                currentQuestion,
                                                checkedState
                                            )
                                            trainingViewModel.currentSelection(
                                                currentQuestion,
                                                checkbox.checked,
                                                checkbox.option
                                            )
                                        }
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
                                    style = MaterialTheme.typography.caption,
                                    color = Color.Black
                                )
                            }
                        }
                        isAnswerButtonEnabled.value =
                            !currentQuestion.checkboxList.stream().allMatch { !it.checked }
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
            //Navigation Buttons
            Column {
                Row {
                    Row(
                        modifier = Modifier.padding(bottom = 30.dp)
                    ) {
                        //Loads first question
                        IconButton(
                            enabled = isNavButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.FIRST_PAGE,
                                    index,
                                    trainingData
                                )
                                trainingViewModel.resetCurrentSelection()
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
                            enabled = isNavButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.PREV_PAGE,
                                    index,
                                    trainingData
                                )
                                trainingViewModel.resetCurrentSelection()
                            }) {
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
                            enabled = isAnswerButtonEnabled.value,
                            colors = ButtonDefaults.buttonColors(Artemis_Blue),
                            //Contains whole logic for further answer processing
                            onClick = {
                                if (answerBtnText == "Antworten") {
                                    //Change last viewed record by current timestamp
                                    currentQuestion.lastViewed =
                                        LocalDateTime.now().format(
                                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                                .withLocale(
                                                    Locale("de")
                                                )
                                        )
                                    questionViewModel.updateQuestion(
                                        QuestionProjection.modelToEntity(
                                            currentQuestion
                                        )
                                    )
                                    trainingViewModel.onChangeAnswerButtonText("Weiter")
                                    if (trainingViewModel.isSelectionCorrect(
                                            currentQuestion
                                        )
                                    ) {
                                        showMessage(context, "Korrekt")
                                        if (currentQuestion.learnedOnce == 0 && currentQuestion.learnedTwice != 1) {
                                            currentQuestion.learnedOnce = 1
                                            currentQuestion.failed = 0
                                            Log.v(
                                                "LearnedOnce",
                                                currentQuestion.learnedOnce.toString()
                                            )
                                        } else if (currentQuestion.learnedOnce == 1 && currentQuestion.learnedTwice != 1) {
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
                                            currentQuestion
                                        )
                                    ) {
                                        showMessage(context, "Falsch")
                                        currentQuestion.learnedOnce = 0
                                        currentQuestion.learnedTwice = 0
                                        currentQuestion.failed = 1

                                        if (generalViewModel.isVibrating.value == 1) {
                                            @Suppress("DEPRECATION") val vibration: Vibrator =
                                                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                            vibration.vibrate(
                                                VibrationEffect.createOneShot(
                                                    200,
                                                    VibrationEffect.DEFAULT_AMPLITUDE
                                                )
                                            )
                                        }
                                        Log.v("Failed", currentQuestion.failed.toString())
                                    }
                                    //Saves all changes into database
                                    questionViewModel.updateQuestion(
                                        QuestionProjection.modelToEntity(
                                            currentQuestion
                                        )
                                    )
                                    trainingViewModel.onChangeEnableNavButtons(false)
                                }
                                if (answerBtnText == "Weiter") {
                                    currentQuestion.apply {
                                        this.checkboxA.checked = false
                                        this.checkboxB.checked = false
                                        this.checkboxC.checked = false
                                        this.checkboxD.checked = false
                                    }
                                    trainingViewModel.onChangeEnableNavButtons(true)
                                    trainingViewModel.resetCurrentSelection()
                                    Log.v("Current Question", currentQuestion.correctAnswers)
                                    trainingViewModel.setNavigationButton(
                                        NavigationButton.NEXT_PAGE,
                                        index,
                                        trainingData
                                    )
                                    if (index == trainingData.size - 1) {
                                        onOpenTrainingDialog(true)
                                    }
                                    trainingViewModel.onChangeAnswerButtonText("Antworten")
                                }
                                statisticViewModel.onChangeTotalStatisticsFromStartScreen()
                            }) {
                            Text(
                                text = answerBtnText,
                                color = Color.White
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 30.dp)
                    ) {
                        //Loads next question
                        IconButton(
                            enabled = isNavButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.NEXT_PAGE,
                                    index,
                                    trainingData
                                )
                                trainingViewModel.resetCurrentSelection()
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
                            enabled = isNavButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.LAST_PAGE,
                                    index,
                                    trainingData
                                )
                                trainingViewModel.resetCurrentSelection()
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
                    text = "${index + 1}/${trainingData.size}",
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
        }
        if (isTrainingDialogOpen) {
            if (loadScreen != null) {
                if (renewQuestions != null) {
                    TrainerAlertDialog(
                        onOpenTrainingDialog,
                        loadScreen,
                        navController,
                        questionViewModel,
                        generalViewModel,
                        trainingViewModel,
                    )
                }
            }
        }
        BackHandler(enabled = true) {
            if (loadScreen != null) {
                navController.navigate(loadScreen.route)
            }
            questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
            trainingViewModel.onChangeIndex(0)
            trainingViewModel.onChangeAnswerButtonText("Antworten")
            trainingViewModel.resetCurrentSelection()
        }
    }

    @Composable
    fun TrainerAlertDialog(
        onOpenTrainingDialog: (Boolean) -> Unit,
        loadScreen: Screen.DrawerScreen,
        navController: NavController,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
        trainingViewModel: TrainingViewModel,

        ) {
        AlertDialog(
            onDismissRequest = { onOpenTrainingDialog(false) },
            shape = RoundedCornerShape(CornerSize(25.dp)),
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
                            navController.navigate(loadScreen.route)
                            questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                            trainingViewModel.onChangeIndex(0)
                            onOpenTrainingDialog(false)
                            generalViewModel.onCloseTrainingScreen(
                                Pair(
                                    Constants.ALPHA_INVISIBLE,
                                    Constants.DISABLED
                                )
                            )
                            trainingViewModel.onChangeIndex(0)
                            trainingViewModel.onChangeAnswerButtonText("Antworten")
                            trainingViewModel.resetCurrentSelection()
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
                            onOpenTrainingDialog(false)
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

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
            this.setGravity(Gravity.CENTER, 0, 0)
            this.show()
        }
    }
}



