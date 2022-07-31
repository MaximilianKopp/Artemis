package com.artemis.hunterexam.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.NavigationButton
import com.artemis.hunterexam.model.QuestionCheckbox
import com.artemis.hunterexam.model.QuestionProjection
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.model.Topic
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class AssignmentViewModel : ViewModel() {

    private val _checkedAnswers = mutableSetOf<String>()
    private val checkedAnswers: Set<String> = _checkedAnswers as HashSet<String>

    private val _showAppBar = MutableLiveData<Boolean>()
    val showAppBar: LiveData<Boolean> = _showAppBar

    private val _topicWildlifeButtonColor = MutableLiveData<Color>()
    val topicWildlifeButtonColor: LiveData<Color> = _topicWildlifeButtonColor

    private val _topicHuntingOperations = MutableLiveData<Color>()
    val topicHuntingOperations: LiveData<Color> = _topicHuntingOperations

    private val _topicWildLifeTreatment = MutableLiveData<Color>()
    val topicWildLifeTreatment: LiveData<Color> = _topicWildLifeTreatment

    private val _topicWeaponsLawAndTechnology = MutableLiveData<Color>()
    val topicWeaponsLawAndTechnology: LiveData<Color> = _topicWeaponsLawAndTechnology

    private val _topicHuntingLaw = MutableLiveData<Color>()
    val topicHuntingLaw: LiveData<Color> = _topicHuntingLaw

    private val _topicPreservationOfWildLifeAndNature = MutableLiveData<Color>()
    val topicPreservationOfWildLifeAndNature: LiveData<Color> =
        _topicPreservationOfWildLifeAndNature

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion: LiveData<QuestionProjection> = _currentQuestion

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

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

    fun changeColorByResult(question: QuestionProjection, checkbox: QuestionCheckbox): Color {
        var result = Color.Red
        if (question.correctAnswers.contains(checkbox.option)) {
            result = Color.Green
        }
        return result
    }

    fun filterCorrectAnswersOfEachTopic(resultList: List<QuestionProjection>, topic: Int): Int {
        return resultList.count { (it.currentSelection == it.correctAnswers).and(it.topic == topic) }
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

    private fun getFailedTopics(marksByTopics: Map<String, Int>): Int {
        var counter = 0
        for ((_, value) in marksByTopics) {
            if (value > 4) {
                counter++
            }
        }
        return counter
    }

    fun setTopicBoxButton(
        direction: NavigationButton,
        topic: Int,
        questions: List<QuestionProjection>
    ) {
        var index = 0
        when (topic) {
            0 -> index = 0
            1 -> index = 21
            2 -> index = 41
            3 -> index = 61
            4 -> index = 81
            5 -> index = 101
        }
        setDirection(direction, questions, index)
    }

    //Navigation bar with buttons
    fun setNavigationButton(
        direction: NavigationButton,
        index: Int,
        questions: List<QuestionProjection>
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.SKIP_TEN_BACKWARD -> skipTenBackward(index, questions)
            NavigationButton.SKIP_TEN_FORWARD -> skipTenForward(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
            NavigationButton.SKIPPED_INDEX -> skippedIndex(questions, index)
        }
    }

    private fun firstPage(questions: List<QuestionProjection>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(questions[0].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
    }

    private fun prevPage(index: Int, questions: List<QuestionProjection>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(questions[index - 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
        }
    }

    private fun skipTenBackward(index: Int, questions: List<QuestionProjection>) {
        var offset = 10
        if ((index - offset) < 0) {
            offset = index
        }
        onChangeIndex(index - offset)
        onChangeCurrentQuestion(questions[index - offset].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
    }

    fun onChangeIndex(newIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeIndexCoroutine(newIndex)
        }
        Log.v("Current index", (newIndex + 1).toString())
    }

    private suspend fun onChangeIndexCoroutine(newIndex: Int) =
        withContext(Dispatchers.IO) {
            _index.postValue(newIndex)
        }

    private fun skipTenForward(index: Int, questions: List<QuestionProjection>) {
        var offset = 10
        if (index < questions.size - 10) {
            if (index == 0) {
                offset = 9
            }
            onChangeIndex(index + offset)
            onChangeCurrentQuestion(questions[index + offset].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
        }
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

    private fun setDirection(
        direction: NavigationButton,
        questions: List<QuestionProjection>,
        index: Int
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.SKIP_TEN_BACKWARD -> skipTenBackward(index, questions)
            NavigationButton.SKIP_TEN_FORWARD -> skipTenForward(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
            NavigationButton.SKIPPED_INDEX -> skippedIndex(questions, index)
        }
    }

    private fun skippedIndex(questions: List<QuestionProjection>, skippedIndex: Int) {
        val index = if (skippedIndex == 0) 0 else skippedIndex - 1
        onChangeIndex(index)
        onChangeCurrentQuestion(questions[index])
    }

    private fun onChangeTopicButtonColor(newValue: Color, topic: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTopicButtonColorCoroutine(newValue, topic)
        }
    }

    private fun nextPage(index: Int, questions: List<QuestionProjection>) {
        if (index < questions.size - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(questions[index + 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
        }
    }

    private fun lastPage(questions: List<QuestionProjection>) {
        onChangeIndex(questions.size - 1)
        onChangeCurrentQuestion(questions[questions.size - 1].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
    }

    private suspend fun onChangeTopicButtonColorCoroutine(newValue: Color, topic: Int) =
        withContext(Dispatchers.IO) {
            when (topic) {
                Topic.TOPIC_1.ordinal -> _topicWildlifeButtonColor.postValue(newValue)
                Topic.TOPIC_2.ordinal -> _topicHuntingOperations.postValue(newValue)
                Topic.TOPIC_3.ordinal -> _topicWeaponsLawAndTechnology.postValue(newValue)
                Topic.TOPIC_4.ordinal -> _topicWildLifeTreatment.postValue(newValue)
                Topic.TOPIC_5.ordinal -> _topicHuntingLaw.postValue(newValue)
                Topic.TOPIC_6.ordinal -> _topicPreservationOfWildLifeAndNature.postValue(newValue)
            }
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

    fun onChangeAppBarAppearance(showAppBar: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeAppBarAppearanceCoroutine(showAppBar)
        }
    }

    private suspend fun onChangeAppBarAppearanceCoroutine(showAppBar: Boolean) =
        withContext(Dispatchers.IO) {
            _showAppBar.postValue(showAppBar)
        }

    fun checkTopicButtonColors(
        assignmentQuestions: List<QuestionProjection>,
        currentQuestion: QuestionProjection
    ) {
        var count = 0
        when (currentQuestion.topic) {
            Topic.TOPIC_1.ordinal -> {
                count = assignmentQuestions.subList(0, 20).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_2.ordinal -> {
                count = assignmentQuestions.subList(20, 40).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_3.ordinal -> {
                count = assignmentQuestions.subList(40, 60).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_4.ordinal -> {
                count = assignmentQuestions.subList(60, 80).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_5.ordinal -> {
                count = assignmentQuestions.subList(80, 100).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_6.ordinal -> {
                count = assignmentQuestions.subList(100, 120).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
        }
        Log.v("Current count", count.toString() + " " + currentQuestion.text)
        if (count == 20) {
            onChangeTopicButtonColor(Artemis_Yellow, currentQuestion.topic)
        } else {
            onChangeTopicButtonColor(Color.White, currentQuestion.topic)
        }
    }

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
}