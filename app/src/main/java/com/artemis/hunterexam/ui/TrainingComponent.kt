package com.artemis.hunterexam.ui

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.CriteriaFilter
import com.artemis.hunterexam.helper.DictionaryLink
import com.artemis.hunterexam.helper.NavigationButton
import com.artemis.hunterexam.model.Dictionary
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

class TrainingComponent {

    @Composable
    fun TrainingScreen(
        navController: NavController,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit,
        questionViewModel: QuestionViewModel,
        trainingViewModel: TrainingViewModel,
        generalViewModel: GeneralViewModel
    ) {
        val navIndex: Int by generalViewModel.index.observeAsState(0)
        val trainingData = trainingViewModel.trainingData.observeAsState(listOf())
        generalViewModel.resetCurrentSelection()

        if (trainingData.value.isNotEmpty()) {
            TrainingContent(
                navController = navController,
                trainingViewModel = trainingViewModel,
                questionViewModel = questionViewModel,
                generalViewModel = generalViewModel,
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
        trainingData: List<QuestionProjection>,
        index: Int,
        isTrainingDialogOpen: Boolean,
        onOpenTrainingDialog: (Boolean) -> Unit,
    ) {
        val context = LocalContext.current
        val currentQuestion: QuestionProjection by generalViewModel.currentQuestion.observeAsState(
            trainingData[0]
        )
        val isNavButtonEnabled: Boolean by generalViewModel.isButtonEnabled.observeAsState(true)
        val isAnswerButtonEnabled = remember { mutableStateOf(false) }
        val answerBtnText: String by generalViewModel.answerBtnText.observeAsState("Antworten")
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
                                    currentQuestion.favouriteState.value = Color.Black
                                } else {
                                    currentQuestion.favourite = 1
                                    currentQuestion.favouriteState.value = Color.Yellow
                                }
                                questionViewModel.updateQuestion(
                                    QuestionProjection.modelToEntity(
                                        currentQuestion
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Favourite Icon",
                                    tint = currentQuestion.favouriteState.value
                                )
                            }
                            Column {
                                Text(
                                    modifier = Modifier.padding(6.dp),
                                    text = questionViewModel.getTopicOfQuestion(currentQuestion.topic),
                                    style = MaterialTheme.typography.caption,
                                    color = Color.Black
                                )
                                val dictionaryEntryForQuestionHeader: Dictionary =
                                    questionViewModel.checkDictionary(currentQuestion.text)
                                if (dictionaryEntryForQuestionHeader.item.isBlank() || generalViewModel.isHintShow.value == 0) {
                                    Text(
                                        modifier = Modifier.padding(6.dp),
                                        text = currentQuestion.text,
                                        style = MaterialTheme.typography.body1,
                                        color = Color.Black
                                    )
                                } else if (generalViewModel.isHintShow.value == 1) {
                                    DictionaryLink(
                                        fullText = currentQuestion.text,
                                        fontSize = 16.sp,
                                        linkText = listOf(dictionaryEntryForQuestionHeader.item),
                                        dictionaryEntry = dictionaryEntryForQuestionHeader
                                    )
                                }
                            }
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
                            val currentCheckboxText: String =
                                trainingViewModel.setCurrentCheckboxText(
                                    currentQuestion,
                                    checkbox.option
                                )
                            Log.v("CheckedState", checkedState.value.toString())
                            Row(
                                Modifier.padding(top = 10.dp, bottom = 10.dp)
                            ) {
                                val isChecked =
                                    if (answerBtnText == "Antworten") {
                                        generalViewModel.checkStates(
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
                                            generalViewModel.onChangeCheckboxes(
                                                checkbox,
                                                currentQuestion,
                                                checkedState
                                            )
                                            generalViewModel.currentSelection(
                                                currentQuestion,
                                                checkbox.checked,
                                                checkbox.option
                                            )
                                        }
                                    },
                                    modifier = Modifier.padding(start = 5.dp)
                                )

                                val dictionaryEntryForCheckbox: Dictionary =
                                    questionViewModel.checkDictionary(currentCheckboxText)

                                if (dictionaryEntryForCheckbox.item.isBlank() || generalViewModel.isHintShow.value == 0) {
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 3.dp,
                                            top = 12.dp,
                                            end = 2.dp
                                        ),
                                        text = currentCheckboxText,
                                        style = MaterialTheme.typography.caption,
                                        color = Color.Black
                                    )
                                } else if (generalViewModel.isHintShow.value == 1) {
                                    DictionaryLink(
                                        modifier = Modifier.padding(
                                            start = 3.dp,
                                            top = 12.dp,
                                            end = 2.dp
                                        ),
                                        fullText = currentCheckboxText,
                                        fontSize = 12.sp,
                                        linkText = listOf(dictionaryEntryForCheckbox.item),
                                        dictionaryEntry = dictionaryEntryForCheckbox,
                                    )
                                }
                            }
                        }
                    }
                    isAnswerButtonEnabled.value =
                        !currentQuestion.checkboxList.stream().allMatch { !it.checked }
                    if (answerBtnText == "Weiter") {
                        isAnswerButtonEnabled.value = true
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
                Row(modifier = Modifier.padding(bottom = 30.dp)) {
                    //Loads first question
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        enabled = isNavButtonEnabled,
                        onClick = {
                            generalViewModel.setNavigationButton(
                                NavigationButton.FIRST_PAGE,
                                index,
                                trainingData
                            )
                            generalViewModel.resetCurrentSelection()
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
                        modifier = Modifier.weight(0.1f),
                        enabled = isNavButtonEnabled,
                        onClick = {
                            generalViewModel.setNavigationButton(
                                NavigationButton.PREV_PAGE,
                                index,
                                trainingData
                            )
                            generalViewModel.resetCurrentSelection()
                        }) {
                        Icon(
                            Icons.Filled.ChevronLeft,
                            contentDescription = "Prev question button",
                            modifier = Modifier.size(50.dp),
                            tint = Artemis_Yellow
                        )
                    }
                    //Skip to 10 questions backward
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        enabled = isNavButtonEnabled,
                        onClick = {
                            generalViewModel.setNavigationButton(
                                NavigationButton.SKIP_TEN_BACKWARD,
                                index,
                                trainingData
                            )
                            generalViewModel.resetCurrentSelection()
                        }) {
                        Icon(
                            Icons.Filled.RotateLeft,
                            contentDescription = "Prev question button",
                            modifier = Modifier.size(25.dp),
                            tint = Artemis_Yellow
                        )
                    }
                    Button(
                        modifier = Modifier.weight(0.4f),
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
                                generalViewModel.onChangeAnswerButtonText("Weiter")
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
                                generalViewModel.onChangeEnableNavButtons(false)
                            }
                            if (answerBtnText == "Weiter") {
                                currentQuestion.apply {
                                    this.checkboxA.checked = false
                                    this.checkboxB.checked = false
                                    this.checkboxC.checked = false
                                    this.checkboxD.checked = false
                                }
                                generalViewModel.onChangeEnableNavButtons(true)
                                generalViewModel.resetCurrentSelection()
                                Log.v("Current Question", currentQuestion.correctAnswers)
                                generalViewModel.setNavigationButton(
                                    NavigationButton.NEXT_PAGE,
                                    index,
                                    trainingData
                                )
                                if (index == trainingData.size - 1) {
                                    onOpenTrainingDialog(true)
                                }
                                generalViewModel.onChangeAnswerButtonText("Antworten")
                            }
                        }) {
                        Text(
                            text = answerBtnText,
                            color = Color.White
                        )
                    }

                    //Skip to 10 questions forward
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        enabled = isNavButtonEnabled,
                        onClick = {
                            generalViewModel.setNavigationButton(
                                NavigationButton.SKIP_TEN_FORWARD,
                                index,
                                trainingData
                            )
                            generalViewModel.resetCurrentSelection()
                        }) {
                        Icon(
                            Icons.Filled.RotateRight,
                            contentDescription = "Prev question button",
                            modifier = Modifier.size(25.dp),
                            tint = Artemis_Yellow
                        )
                    }
                    //Loads next question
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        enabled = isNavButtonEnabled,
                        onClick = {
                            generalViewModel.setNavigationButton(
                                NavigationButton.NEXT_PAGE,
                                index,
                                trainingData
                            )
                            generalViewModel.resetCurrentSelection()
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
                        modifier = Modifier.weight(0.1f),
                        enabled = isNavButtonEnabled,
                        onClick = {
                            generalViewModel.setNavigationButton(
                                NavigationButton.LAST_PAGE,
                                index,
                                trainingData
                            )
                            generalViewModel.resetCurrentSelection()
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
                        generalViewModel
                    )
                }
            }
        }
        BackHandler(enabled = true) {
            onOpenTrainingDialog(true)
            if (loadScreen != null && isTrainingDialogOpen) {
                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                generalViewModel.onChangeIndex(0)
                generalViewModel.onChangeAnswerButtonText("Antworten")
                generalViewModel.resetCurrentSelection()
                navController.navigate(loadScreen.route)
            }
        }
    }

    @Composable
    fun TrainerAlertDialog(
        onOpenTrainingDialog: (Boolean) -> Unit,
        loadScreen: Screen.DrawerScreen,
        navController: NavController,
        questionViewModel: QuestionViewModel,
        generalViewModel: GeneralViewModel,
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
                            generalViewModel.onChangeIndex(0)
                            onOpenTrainingDialog(false)
                            generalViewModel.onCloseTrainingScreen(
                                Pair(
                                    Constants.ALPHA_INVISIBLE,
                                    Constants.DISABLED
                                )
                            )
                            generalViewModel.onChangeIndex(0)
                            generalViewModel.onChangeAnswerButtonText("Antworten")
                            generalViewModel.resetCurrentSelection()
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



