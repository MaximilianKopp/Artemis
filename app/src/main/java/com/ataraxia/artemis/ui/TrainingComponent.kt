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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.helper.NavigationButton
import com.ataraxia.artemis.model.Question
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
        trainingData: List<Question>,
        index: Int,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit,
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

        val favouriteState: Int by trainingViewModel.favouriteColor.observeAsState(currentQuestion.favourite)

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
                                questionViewModel.updateQuestion(currentQuestion)
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
                                    modifier = Modifier.padding(
                                        start = 3.dp,
                                        top = 12.dp,
                                        end = 2.dp
                                    ),
                                    text = currentQuestionText,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
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
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.FIRST_PAGE,
                                    index,
                                    trainingData
                                )
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
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.PREV_PAGE,
                                    index,
                                    trainingData
                                )
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
                            enabled = checkedA || checkedB || checkedC || checkedD,
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
                                    questionViewModel.updateQuestion(currentQuestion)

                                    trainingViewModel.onChangeAnswerButtonText("Weiter")
                                    if (trainingViewModel.isSelectionCorrect(
                                            currentQuestion,
                                            checkedAnswers,
                                            selections
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
                                            currentQuestion,
                                            checkedAnswers,
                                            selections
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
                                    questionViewModel.updateQuestion(currentQuestion)
                                    trainingViewModel.onChangeEnableNavButtons(false)
                                }
                                if (answerBtnText == "Weiter") {
                                    trainingViewModel.onChangeEnableNavButtons(true)
                                    trainingViewModel.resetSelections()
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
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.NEXT_PAGE,
                                    index,
                                    trainingData
                                )
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
                            enabled = isButtonEnabled,
                            onClick = {
                                trainingViewModel.setNavigationButton(
                                    NavigationButton.LAST_PAGE,
                                    index,
                                    trainingData
                                )
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
            trainingViewModel.resetSelections()
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
                            trainingViewModel.resetSelections()
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



