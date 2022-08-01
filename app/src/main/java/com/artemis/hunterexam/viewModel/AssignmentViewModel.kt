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
import java.time.LocalDateTime

class AssignmentViewModel : ViewModel() {

    private val className = this.javaClass.simpleName

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

    /**
     * Returns the content of a specific checkbox option provided by the current selected question
     *
     * @param question the currently displayed question
     * @param checkbox one of four possible checkboxes that select/unselect an answer option
     *
     * return the option text for the created checkbox from the options properties of the current question
     */
    fun setCurrentOptionText(question: QuestionProjection, checkbox: QuestionCheckbox): String {
        var optionText = Constants.EMPTY_STRING
        when (checkbox.option) {
            Constants.TRAINING_SELECTION_A -> optionText = question.optionA
            Constants.TRAINING_SELECTION_B -> optionText = question.optionB
            Constants.TRAINING_SELECTION_C -> optionText = question.optionC
            Constants.TRAINING_SELECTION_D -> optionText = question.optionD
        }
        return optionText
    }

    /**
     * Adds or removes a checked option to a result list of answers
     *
     * @param isChecked retrieves the information if a checkbox has been checked or not
     * @param currentQuestion the current displayed question of this training unit
     */
    @Suppress("JavaCollectionsStaticMethodOnImmutableList")
    fun currentSelection(isChecked: Boolean, option: String, currentQuestion: QuestionProjection) {
        if (!isChecked) {
            _checkedAnswers.add(option)
        } else {
            _checkedAnswers.remove(option)
        }
        Log.i(setLogTag(className, "currentSelection()"), checkedAnswers.toString())
        currentQuestion.currentSelection = checkedAnswers.toSortedSet().toString()
    }

    private fun setLogTag(className: String, methodName: String): String {
        return "${LocalDateTime.now()} $className $methodName"
    }

    /**
     * Checks the current checkbox state by iterating through the checkboxList of the model
     *
     * @param currentQuestion the currently displayed question
     * @param checkbox the currently focused checkbox
     * @param checkedState the state of this checkbox
     *
     * return the actual state of the currently focused checkbox
     */
    fun checkStates(
        currentQuestion: QuestionProjection,
        checkbox: QuestionCheckbox,
        checkedState: MutableState<Boolean>
    ): Boolean {
        for (cb in currentQuestion.checkboxList) {
            if (cb.option == checkbox.option) {
                checkedState.value = cb.checked
                Log.i(
                    setLogTag(className, "checkStates()"),
                    "Checkbox ${cb.option} is checked" + cb.checked.toString()
                )
            } // no else
        }
        return checkedState.value
    }

    /**
     * Defines the color of checkbox after the evaluation of questions => green for correct red for false
     *
     * @param question the currently displayed question
     * @param checkbox the
     *
     * return color of evaluated checkbox
     */
    fun changeColorByResult(question: QuestionProjection, checkbox: QuestionCheckbox): Color {
        var result = Color.Red
        if (question.correctAnswers.contains(checkbox.option)) {
            result = Color.Green
        }
        return result
    }

    /**
     * Iterates over the resultList of each topic and calculates the total amount of correctly answered questions
     *
     * @param resultList list of all questions in this assignment
     * @param topic the ordinal number of the topic
     *
     * return the amount of correctly answered questions by topic
     */
    fun filterCorrectAnswersOfEachTopic(resultList: List<QuestionProjection>, topic: Int): Int {
        return resultList.count { (it.currentSelection == it.correctAnswers).and(it.topic == topic) }
    }

    /**
     * Calculates the amount of wrong answers by subtract the amount of correctly answered questions from the determined size of questions for the assignment
     *
     * @param correctAnswersByTopic the total amount of correctly answered questions
     *
     * return the amount of correctly answered questions by topic
     */
    fun filterWrongAnswersOfEachTopic(correctAnswersByTopic: Int): Int {
        return Constants.SIZE_OF_EACH_ASSIGNMENT_TOPIC - correctAnswersByTopic
    }

    /**
     * Calculates the mark depending of the amount of correctly answered questions
     *
     * @param resultList list of all questions in this assignment
     *
     * return a resultMap that contains the topic name as key and its calculated mark as value
     */
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

    /**
     * Calculates the final mark by the average of all part marks from each topic
     *
     * @param marksByTopics hashmap that has been precalculated by the calculateMarksByTopic method (contains topic as key and calculated mark by topic)
     * return the average final mark
     */
    fun calculateFinalMark(marksByTopics: Map<String, Int>): BigDecimal {
        return (marksByTopics.entries.sumOf { it.value } / marksByTopics.size).toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
    }

    /**
     * Evaluates the whole assignment.
     *
     * @param marksByTopics hashmap that has been precalculated by the calculateMarksByTopic method (contains topic as key and calculated mark by topic)
     *
     * return false if the assignment has been failed or true if it has been passed
     */
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

    /**
     * Calculated the amount of failed topics
     *
     * @param marksByTopics hashmap that has been precalculated by the calculateMarksByTopic method (contains topic as key and calculated mark by topic)
     *
     * return the amount of failed topics
     */
    private fun getFailedTopics(marksByTopics: Map<String, Int>): Int {
        var counter = 0
        for ((_, value) in marksByTopics) {
            if (value > 4) {
                counter++
            }
        }
        return counter
    }

    /**
     * Navigates to the first question of a topic by clicking on the specific topicBoxButton
     *
     * @param direction enum of the navigationButton
     * @param topic the ordinal number of the topic
     * @param assignmentQuestions list of questions within in the current assignment
     */
    fun setTopicBoxButton(
        direction: NavigationButton,
        topic: Int,
        assignmentQuestions: List<QuestionProjection>
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
        setDirection(direction, index, assignmentQuestions)
    }

    /**
     * Defines the direction of navigation
     *
     * @param direction enum in order to set the determine direction
     * @param index the index position of the currentQuestion from the determined assignmentQuestionList
     * @param assignmentQuestions list of questions within in the current assignment
     */
    fun setDirection(
        direction: NavigationButton,
        index: Int,
        assignmentQuestions: List<QuestionProjection>
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(assignmentQuestions)
            NavigationButton.PREV_PAGE -> prevPage(index, assignmentQuestions)
            NavigationButton.SKIP_TEN_BACKWARD -> skipTenBackward(index, assignmentQuestions)
            NavigationButton.SKIP_TEN_FORWARD -> skipTenForward(index, assignmentQuestions)
            NavigationButton.NEXT_PAGE -> nextPage(index, assignmentQuestions)
            NavigationButton.LAST_PAGE -> lastPage(assignmentQuestions)
            NavigationButton.SKIPPED_INDEX -> skippedIndex(assignmentQuestions, index)
        }
    }

    /**
     * Navigates to the first question of the assignmentQuestionList
     *
     * @param assignmentQuestions list of questions within in the current assignment
     */
    private fun firstPage(assignmentQuestions: List<QuestionProjection>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(assignmentQuestions[0].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
    }

    /**
     * Navigates to the previous question of the assignmentQuestionList
     *
     * @param index the index position of the currentQuestion from the determined assignmentQuestionList
     * @param assignmentQuestions list of questions within in the current assignment
     */
    private fun prevPage(index: Int, assignmentQuestions: List<QuestionProjection>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(assignmentQuestions[index - 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
        }
    }

    /**
     * Skips ten question backwards of the assignmentQuestionList
     *
     * @param index the index position of the currentQuestion from the determined assignmentQuestionList
     * @param assignmentQuestions list of questions within in the current assignment
     */
    private fun skipTenBackward(index: Int, assignmentQuestions: List<QuestionProjection>) {
        var offset = 10
        if ((index - offset) < 0) {
            offset = index
        }
        onChangeIndex(index - offset)
        onChangeCurrentQuestion(assignmentQuestions[index - offset].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
    }

    /**
     * Skips ten question forward of the assignmentQuestionList
     *
     * @param index the index position of the currentQuestion from the determined assignmentQuestionList
     * @param assignmentQuestions list of questions within in the current assignment
     */
    private fun skipTenForward(index: Int, assignmentQuestions: List<QuestionProjection>) {
        var offset = 10
        if (index < assignmentQuestions.size - 10) {
            if (index == 0) {
                offset = 9
            }
            onChangeIndex(index + offset)
            onChangeCurrentQuestion(assignmentQuestions[index + offset].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
        }
    }

    /**
     * Navigates to the next question of the assignmentQuestionList
     *
     * @param index the index position of the currentQuestion from the determined assignmentQuestionList
     * @param assignmentQuestions list of questions within in the current assignment
     */
    private fun nextPage(index: Int, assignmentQuestions: List<QuestionProjection>) {
        if (index < assignmentQuestions.size - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(assignmentQuestions[index + 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
        }
    }

    /**
     * Navigates to the last question of the assignmentQuestionList
     *
     * @param assignmentQuestions list of questions within in the current assignment
     */
    private fun lastPage(assignmentQuestions: List<QuestionProjection>) {
        onChangeIndex(assignmentQuestions.size - 1)
        onChangeCurrentQuestion(assignmentQuestions[assignmentQuestions.size - 1].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
    }

    /**
     * Launch the coroutine of the onChangeIndexCoroutine method
     *
     * @param newIndex
     */
    fun onChangeIndex(newIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeIndexCoroutine(newIndex)
        }
        Log.v("Current index", (newIndex + 1).toString())
    }

    /**
     * Change the index position of the assignmentQuestionList
     *
     * @param newIndex new index position
     */
    private suspend fun onChangeIndexCoroutine(newIndex: Int) =
        withContext(Dispatchers.IO) {
            _index.postValue(newIndex)
        }


    /**
     * Launch the coroutine of onChangeCurrentQuestion
     *
     * @param newQuestion new question that will be passed to coroutine
     */
    fun onChangeCurrentQuestion(newQuestion: QuestionProjection) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCurrentQuestionCoroutine(newQuestion)
        }
    }

    /**
     * Change the displayed question
     *
     * @param newQuestion new question that has to be displayed
     */
    private suspend fun onChangeCurrentQuestionCoroutine(newQuestion: QuestionProjection) =
        withContext(Dispatchers.IO) {
            restoreSelection(newQuestion)
            _currentQuestion.postValue(newQuestion)
        }

    /**
     * Secures that the checked options of a previous displayed question still exists
     *
     * @param currentQuestion the currently displayed question
     */
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

    private fun skippedIndex(assignmentQuestions: List<QuestionProjection>, skippedIndex: Int) {
        val index = if (skippedIndex == 0) 0 else skippedIndex - 1
        onChangeIndex(index)
        onChangeCurrentQuestion(assignmentQuestions[index])
    }

    private fun onChangeTopicButtonColor(newValue: Color, topic: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTopicButtonColorCoroutine(newValue, topic)
        }
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

    /**
     * Launches the coroutine of the onChangeCheckboxesCoroutine method
     *
     * @param checkbox the currently focused checkbox that will be passed to coroutine
     * @param currentQuestion the currently displayed question that will be passed to coroutine
     * @param checkedState state of a checked or unchecked checkbox that will be passed to coroutine
     */
    fun onChangeCheckboxes(
        checkbox: QuestionCheckbox,
        currentQuestion: QuestionProjection,
        checkedState: MutableState<Boolean>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCheckboxesCoroutine(checkbox, currentQuestion, checkedState)
        }
    }

    /**
     * Sets the state of each checkbox
     *
     * @param checkbox the currently focused checkbox
     * @param currentQuestion the currently displayed question
     * @param checkedState state of a checked or unchecked checkbox
     */
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

    /**
     * Launches the coroutine of the showAppBar method
     *
     * @param showAppBar boolean that will be passed to coroutine
     */
    fun onChangeAppBarAppearance(showAppBar: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeAppBarAppearanceCoroutine(showAppBar)
        }
    }

    /**
     * Hides the TopAppBar during the whole assignment
     *
     * @param showAppBar decides if the TopAppBar has to been shown or not
     */
    private suspend fun onChangeAppBarAppearanceCoroutine(showAppBar: Boolean) =
        withContext(Dispatchers.IO) {
            _showAppBar.postValue(showAppBar)
        }

    /**
     * If at least one checkbox of all questions within a topic is checked, the topicBoxButton color will be changed to yellow
     *
     * @param assignmentQuestions list of questions within in the current assignment
     * @param currentQuestion the currently displayed question
     */
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

    /**
     * Calculates the mark by determined ranges of a specific topic
     *
     * @param resultOfCorrectAnswers the amount of correct answers within a topic
     *
     * return the calculated mark
     */
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