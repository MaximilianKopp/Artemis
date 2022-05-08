package com.ataraxia.artemis.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavigationButton
import com.ataraxia.artemis.model.QuestionCheckbox
import com.ataraxia.artemis.model.QuestionProjection
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class AssignmentViewModel : ViewModel() {

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion: LiveData<QuestionProjection> = _currentQuestion

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    private val _favouriteColor = MutableLiveData<Int>()
    val favouriteColor: LiveData<Int> = _favouriteColor

    private val _checkedAnswers = mutableSetOf<String>()
    private val checkedAnswers: Set<String> = _checkedAnswers as HashSet<String>

    fun setCurrentQuestionText(question: QuestionProjection, checkbox: QuestionCheckbox): String {
        when (checkbox.option) {
            Constants.TRAINING_SELECTION_A -> return question.optionA
            Constants.TRAINING_SELECTION_B -> return question.optionB
            Constants.TRAINING_SELECTION_C -> return question.optionC
            Constants.TRAINING_SELECTION_D -> return question.optionD
        }
        return "Error"
    }

    @Suppress("JavaCollectionsStaticMethodOnImmutableList")
    fun currentSelection(isChecked: Boolean, option: String, currentQuestion: QuestionProjection) {
        if (!isChecked) {
            _checkedAnswers.add(option)
        } else {
            _checkedAnswers.remove(option)
        }
        Log.v("Selected Options", checkedAnswers.toString())
        currentQuestion.currentSelection = checkedAnswers.toSortedSet().toString()
    }

    fun checkStates(
        currentQuestion: QuestionProjection,
        checkbox: QuestionCheckbox,
        checkedState: MutableState<Boolean>
    ): Boolean {
        for (cb in currentQuestion.checkboxList) {
            if (cb.option == checkbox.option) {
                checkedState.value = cb.checked
            }
        }
        return checkedState.value
    }

    fun onChangeCheckboxes(
        checkbox: QuestionCheckbox,
        currentQuestion: QuestionProjection,
        checkedState: MutableState<Boolean>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCheckboxesCoroutine(checkbox, currentQuestion, checkedState)
        }
    }

    private suspend fun onChangeCheckboxesCoroutine(
        checkbox: QuestionCheckbox,
        currentQuestion: QuestionProjection,
        checkedState: MutableState<Boolean>
    ) = withContext(Dispatchers.IO) {
        checkbox.checked = !checkbox.checked
        when (checkbox.option) {
            Constants.TRAINING_SELECTION_A -> currentQuestion.checkboxA = checkbox.apply {
                checkedState.value = checkbox.checked
            }
            Constants.TRAINING_SELECTION_B -> currentQuestion.checkboxB = checkbox.apply {
                checkedState.value = checkbox.checked
            }
            Constants.TRAINING_SELECTION_C -> currentQuestion.checkboxC = checkbox.apply {
                checkedState.value = checkbox.checked
            }
            Constants.TRAINING_SELECTION_D -> currentQuestion.checkboxD = checkbox.apply {
                checkedState.value = checkbox.checked
            }
        }
        _currentQuestion.postValue(currentQuestion)
        onChangeCurrentQuestion(currentQuestion)
    }

    private fun onChangeIndex(newIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeIndexCoroutine(newIndex)
        }
        Log.v("Current index", (newIndex + 1).toString())
    }

    private suspend fun onChangeIndexCoroutine(newIndex: Int) =
        withContext(Dispatchers.IO) {
            _index.postValue(newIndex)
        }

    fun onChangeCurrentQuestion(newQuestion: QuestionProjection) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCurrentQuestionCoroutine(newQuestion)
        }
    }

    private suspend fun onChangeCurrentQuestionCoroutine(newQuestion: QuestionProjection) =
        withContext(Dispatchers.IO) {
            restoreSelection(newQuestion)
            _currentQuestion.postValue(newQuestion)
        }

    fun onChangeFavouriteState(isFavourite: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeFavouriteStateCoroutine(isFavourite)
        }
    }

    private suspend fun onChangeFavouriteStateCoroutine(isFavourite: Int) =
        withContext(Dispatchers.IO) {
            _favouriteColor.postValue(isFavourite)
        }

    //Navigation bar with buttons
    fun setNavigationButton(
        direction: NavigationButton,
        index: Int,
        questions: List<QuestionProjection>,
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
            NavigationButton.SKIPPED_INDEX -> skippedIndex(questions, index)
        }
    }

    fun changeColorByResult(question: QuestionProjection, checkbox: QuestionCheckbox): Color {
        var result = Color.Red
        if (question.correctAnswers.contains(checkbox.option)) {
            result = Color.Green
        }
        return result
    }

    fun filterCorrectAnswersInTotal(resultList: List<QuestionProjection>): Int {
        return resultList.filter { (it.currentSelection == it.correctAnswers) }
            .count()
    }

    fun filterWrongAnswersInTotal(correctAnswers: Int): Int {
        return Constants.SIZE_OF_ASSIGNMENT_TOTAL - correctAnswers
    }

    fun filterCorrectAnswersOfEachTopic(resultList: List<QuestionProjection>, topic: Int): Int {
        return resultList.filter { (it.currentSelection == it.correctAnswers).and(it.topic == topic) }
            .count()
    }

    fun filterWrongAnswersOfEachTopic(correctAnswersByTopic: Int): Int {
        return Constants.SIZE_OF_EACH_ASSIGNMENT_TOPIC - correctAnswersByTopic
    }

    fun calculateMarksByTopic(resultList: List<QuestionProjection>): Map<String, Int> {
        var resultOfCorrectAnswers: Int
        val mapResult = HashMap<String, Int>()
        for (topic in Screen.TOPIC_SCREENS.filter { it.title != "Alle Fragen" }) {
            resultOfCorrectAnswers =
                resultList.count { (it.correctAnswers == it.currentSelection).and(it.topic == Screen.DrawerScreen.TopicWildLife.topic) }
            mapResult[topic.title] = calculateMark(resultOfCorrectAnswers)
        }
        return mapResult
    }

    fun calculateFinalMark(marksByTopics: Map<String, Int>): BigDecimal {
        return (marksByTopics.entries.sumOf { it.value } / marksByTopics.size).toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
    }

    fun evaluate(marksByTopics: Map<String, Int>, finalMark: BigDecimal): Boolean {
        var result = true
        val failedTopics = getFailedTopics(marksByTopics)

        if (failedTopics > 1) {
            result = false
        }
        if (finalMark > BigDecimal.valueOf(4.55)) {
            result = false
        }
        return result
    }

    fun getFailedTopics(marksByTopics: Map<String, Int>): Int {
        var counter = 0
        for ((_, value) in marksByTopics) {
            if (value > 4) {
                counter++
            }
        }
        return counter
    }

    fun changeSkippedBoxColor(
        assignmentQuestions: List<QuestionProjection>,
        skippedBoxColor: MutableState<Color>, skippedIndex: Int
    ) {
        val startIndex = skippedIndex
        val endIndex = skippedIndex + 10
        val isColorChanged: Boolean

        val currentSubList = assignmentQuestions.subList(startIndex, endIndex)

        isColorChanged = currentSubList.stream()
            .allMatch { it.currentSelection != "[]" && it.currentSelection != "" }
        if (isColorChanged) {
            skippedBoxColor.value = Artemis_Yellow
        } else {
            skippedBoxColor.value = Color.White
        }
    }

    //If a decade of questions has been
//    fun changeSkippedBoxColor(
//        assignmentQuestions: List<QuestionProjection>,
//        skippedIndex: Int
//    ): Color {
//        val indexStart = if (skippedIndex != 0) skippedIndex - 10 else 0
//        val indexEnd = if (skippedIndex == 0) 10 else skippedIndex
//        val sublist = assignmentQuestions.subList(indexStart, indexEnd)
//        var amountOfSelections = 0
//
//        for (question in sublist) {
//            if (question.currentSelection.isNotBlank() && question.currentSelection != "[]") {
//                amountOfSelections++
//            }
//        }
//        Log.v("Current indexStart", indexStart.toString())
//        Log.v("Current indexEnd", indexEnd.toString())
//        return if (amountOfSelections == 10) {
//            Artemis_Yellow
//        } else {
//            Color.White
//        }
//    }

    fun calculateMark(resultOfCorrectAnswers: Int): Int {
        var mark = 6
        when (resultOfCorrectAnswers) {
            in 19..20 -> {
                mark = 1
            }
            in 16..18 -> {
                mark = 2
            }
            in 13..15 -> {
                mark = 3
            }
            in 10..12 -> {
                mark = 4
            }
            in 7..9 -> {
                mark = 5
            }
            in 4..6 -> {
                mark = 6
            }
        }
        return mark
    }

    @Suppress("JavaCollectionsStaticMethodOnImmutableList")
    private fun restoreSelection(currentQuestion: QuestionProjection) {
        _checkedAnswers.clear()
        for (cb in currentQuestion.checkboxList) {
            if (cb.checked) {
                _checkedAnswers.add(cb.option)
            }
        }
        checkedAnswers.toSortedSet().toString()
        currentQuestion.currentSelection = _checkedAnswers.toString()
    }

    private fun firstPage(questions: List<QuestionProjection>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(questions[0])
        onChangeFavouriteState(questions[0].favourite)
    }

    private fun prevPage(index: Int, questions: List<QuestionProjection>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(questions[index - 1])
            onChangeFavouriteState(questions[index - 1].favourite)
        }
    }

    private fun nextPage(index: Int, questions: List<QuestionProjection>) {
        if (index < questions.size - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(questions[index + 1])
            onChangeFavouriteState(questions[index + 1].favourite)
        }
    }

    private fun lastPage(questions: List<QuestionProjection>) {
        onChangeIndex(questions.size - 1)
        onChangeCurrentQuestion(questions[questions.size - 1])
        onChangeFavouriteState(questions[questions.size - 1].favourite)
    }

    private fun skippedIndex(questions: List<QuestionProjection>, skippedIndex: Int) {
        val index = if (skippedIndex == 0) 0 else skippedIndex - 1
        onChangeIndex(index)
        onChangeCurrentQuestion(questions[index])
        onChangeFavouriteState(questions[index].favourite)
    }
}