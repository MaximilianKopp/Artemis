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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainingViewModel : ViewModel() {

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion: LiveData<QuestionProjection> = _currentQuestion

    private val _trainingData = MutableLiveData<List<QuestionProjection>>()
    val trainingData: LiveData<List<QuestionProjection>> = _trainingData

    private val _checkedAnswers = mutableSetOf<String>()
    val checkedAnswers: Set<String> = _checkedAnswers as HashSet<String>

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _answerBtnText = MutableLiveData("Antworten")
    val answerBtnText: LiveData<String> = _answerBtnText

    private val _favouriteColor = MutableLiveData<Int>()
    val favouriteColor: LiveData<Int> = _favouriteColor

    private val _checkedState = MutableLiveData<Boolean>()
    val checkedState: LiveData<Boolean> = _checkedState

    fun onChangeCheckedState(checkedState: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCheckedStateCoroutine(checkedState)
        }
    }

    private suspend fun onChangeCheckedStateCoroutine(checkedState: Boolean) =
        withContext(Dispatchers.IO) {
            _checkedState.postValue(checkedState)
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

    fun onChangeTrainingData(trainingData: List<QuestionProjection>) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTrainingDataCoroutine(trainingData)
        }
    }

    private suspend fun onChangeTrainingDataCoroutine(newTrainingData: List<QuestionProjection>) =
        withContext(Dispatchers.IO) {
            _trainingData.postValue(newTrainingData)
        }

    fun onChangeCurrentQuestion(newQuestion: QuestionProjection) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCurrentQuestionCoroutine(newQuestion)
        }
    }

    private suspend fun onChangeCurrentQuestionCoroutine(newQuestion: QuestionProjection) =
        withContext(Dispatchers.IO) {
            _currentQuestion.postValue(newQuestion)
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

    fun setCurrentQuestionText(question: QuestionProjection, checkedAnswer: String): String {
        when (checkedAnswer) {
            Constants.TRAINING_SELECTION_A -> return question.optionA
            Constants.TRAINING_SELECTION_B -> return question.optionB
            Constants.TRAINING_SELECTION_C -> return question.optionC
            Constants.TRAINING_SELECTION_D -> return question.optionD
        }
        return "ERROR"
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

    fun onChangeAnswerButtonText(answerBtnText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeAnswerButtonTextCoroutine(answerBtnText)
        }
    }

    private suspend fun onChangeAnswerButtonTextCoroutine(answerBtnText: String) =
        withContext(Dispatchers.IO) {
            _answerBtnText.postValue(answerBtnText)
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

    fun changeColorByResult(question: QuestionProjection, checkbox: QuestionCheckbox): Color {
        var result = Color.Red
        if (question.correctAnswers.contains(checkbox.option)) {
            result = Color.Green
        }
        return result
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

    //Navigation bar with buttons
    fun setNavigationButton(
        direction: NavigationButton,
        index: Int,
        questions: List<QuestionProjection>
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
        }
    }

    private fun firstPage(questions: List<QuestionProjection>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(questions[0].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
        onChangeFavouriteState(questions[0].favourite)
    }

    private fun prevPage(index: Int, questions: List<QuestionProjection>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(questions[index - 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
            onChangeFavouriteState(questions[index - 1].favourite)
        }
    }

    private fun nextPage(index: Int, questions: List<QuestionProjection>) {
        if (index < questions.size - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(questions[index + 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
            onChangeFavouriteState(questions[index + 1].favourite)
        }
    }

    private fun lastPage(questions: List<QuestionProjection>) {
        onChangeIndex(questions.size - 1)
        onChangeCurrentQuestion(questions[questions.size - 1].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
        onChangeFavouriteState(questions[questions.size - 1].favourite)
    }

//    fun setFavourite(
//        question: Question,
//        isFavourite: MutableState<Int>,
//        currentFilter: CriteriaFilter
//    ) {
//        if (question.favourite == 0) {
//            question.favourite = 1
//            isFavourite.value = question.favourite
//        }
//        else if (question.favourite == 1) {
//            question.favourite = 0
//            isFavourite.value = question.favourite
//        }
//
//        if (question.favourite == 1 && currentFilter == CriteriaFilter.FAVOURITES) {
//            question.favourite = 0
//            isFavourite.value = question.favourite
//        }
//        updateQuestion(question)
//    }
}