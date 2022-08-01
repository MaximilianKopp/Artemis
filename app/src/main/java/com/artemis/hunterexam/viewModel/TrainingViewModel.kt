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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainingViewModel : ViewModel() {

    private val _trainingData = MutableLiveData<List<QuestionProjection>>()
    val trainingData: LiveData<List<QuestionProjection>> = _trainingData

    private val _checkedAnswers = mutableSetOf<String>()
    private val checkedAnswers: Set<String> = _checkedAnswers as HashSet<String>

    private val _answerBtnText = MutableLiveData(Constants.BTN_ANSWER)
    val answerBtnText: LiveData<String> = _answerBtnText

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion: LiveData<QuestionProjection> = _currentQuestion

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    fun onChangeTrainingData(trainingData: List<QuestionProjection>) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTrainingDataCoroutine(trainingData)
        }
    }

    fun onChangeEnableNavButtons(enabled: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeEnableNavButtonsCoroutine(enabled)
        }
    }

    private suspend fun onChangeEnableNavButtonsCoroutine(enabled: Boolean) =
        withContext(Dispatchers.IO) {
            _isButtonEnabled.postValue(enabled)
        }

    private suspend fun onChangeTrainingDataCoroutine(newTrainingData: List<QuestionProjection>) =
        withContext(Dispatchers.IO) {
            _trainingData.postValue(newTrainingData)
        }

    fun setCurrentCheckboxText(question: QuestionProjection, checkedAnswer: String): String {
        when (checkedAnswer) {
            Constants.TRAINING_SELECTION_A -> return this.removeAlphabeticPrefixFromQuestionText(
                question.optionA
            )
            Constants.TRAINING_SELECTION_B -> return this.removeAlphabeticPrefixFromQuestionText(
                question.optionB
            )
            Constants.TRAINING_SELECTION_C -> return this.removeAlphabeticPrefixFromQuestionText(
                question.optionC
            )
            Constants.TRAINING_SELECTION_D -> return this.removeAlphabeticPrefixFromQuestionText(
                question.optionD
            )
        }
        return Constants.ERROR
    }

    private fun removeAlphabeticPrefixFromQuestionText(option: String): String {
        var questionTextWithoutPrefix = Constants.EMPTY_STRING
        val alphabeticIndices = listOf("a.", "b.", "c.", "d.")
        alphabeticIndices.forEach {
            if (option.contains(it)) {
                questionTextWithoutPrefix = option.replace(it, Constants.EMPTY_STRING)
            }
        }
        return questionTextWithoutPrefix
    }

    @Suppress("JavaCollectionsStaticMethodOnImmutableList")
    fun isSelectionCorrect(
        correctAnswers: QuestionProjection,
    ): Boolean {
        var result = false

        if (correctAnswers.correctAnswers == checkedAnswers.toSortedSet().toString()) {
            result = true
        }
        Log.v("Correct Answers", correctAnswers.correctAnswers)
        Log.v("Question answered", result.toString())
        return result
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

    fun onChangeAnswerButtonText(answerBtnText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeAnswerButtonTextCoroutine(answerBtnText)
        }
    }

    private suspend fun onChangeAnswerButtonTextCoroutine(answerBtnText: String) =
        withContext(Dispatchers.IO) {
            _answerBtnText.postValue(answerBtnText)
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

    fun currentSelection(
        currentQuestion: QuestionProjection,
        checkedState: Boolean,
        option: String
    ) {
        if (!checkedState) {
            _checkedAnswers.add(option)
        } else {
            _checkedAnswers.remove(option)
        }
        Log.v("Selected Options", checkedAnswers.toString())
        currentQuestion.currentSelection = checkedAnswers.toSortedSet().toString()
    }

    fun resetCurrentSelection() {
        _checkedAnswers.clear()
        _currentQuestion.value?.checkboxA?.checked = false
        _currentQuestion.value?.checkboxB?.checked = false
        _currentQuestion.value?.checkboxC?.checked = false
        _currentQuestion.value?.checkboxD?.checked = false
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
            else -> {}
        }
    }

    fun checkStates(
        currentQuestion: QuestionProjection,
        checkbox: QuestionCheckbox,
        checkedState: MutableState<Boolean>
    ): Boolean {
        for (cb in currentQuestion.checkboxList) {
            if (cb.option == checkbox.option) {
                checkedState.value = cb.checked
                when (cb.option) {
                    Constants.TRAINING_SELECTION_A -> currentQuestion.checkboxA.checked =
                        checkedState.value
                }
            }
        }
        _currentQuestion.postValue(currentQuestion)
        return checkedState.value
    }

    fun changeColorByResult(question: QuestionProjection, checkbox: QuestionCheckbox): Color {
        var result = Color.Red
        if (question.correctAnswers.contains(checkbox.option)) {
            result = Color.Green
        }
        return result
    }
}