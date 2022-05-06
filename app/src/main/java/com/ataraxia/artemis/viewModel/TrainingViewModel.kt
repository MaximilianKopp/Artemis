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
import java.util.*

class TrainingViewModel : ViewModel() {

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion: LiveData<QuestionProjection> = _currentQuestion

    private val _trainingData = MutableLiveData<List<QuestionProjection>>()
    val trainingData: LiveData<List<QuestionProjection>> = _trainingData

    private val _checkedAnswers = mutableListOf<String>()
    val checkedAnswers: List<String> = _checkedAnswers

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    private val _checkBoxColorA = MutableLiveData<Color>()
    val checkBoxColorA: LiveData<Color> = _checkBoxColorA

    private val _checkBoxColorB = MutableLiveData<Color>()
    val checkBoxColorB: LiveData<Color> = _checkBoxColorB

    private val _checkBoxColorC = MutableLiveData<Color>()
    val checkBoxColorC: LiveData<Color> = _checkBoxColorC

    private val _checkBoxColorD = MutableLiveData<Color>()
    val checkBoxColorD: LiveData<Color> = _checkBoxColorD

    private val _selectA = MutableLiveData<String>()
    val selectA: LiveData<String> = _selectA

    private val _selectB = MutableLiveData<String>()
    val selectB: LiveData<String> = _selectB

    private val _selectC = MutableLiveData<String>()
    val selectC: LiveData<String> = _selectC

    private val _selectD = MutableLiveData<String>()
    val selectD: LiveData<String> = _selectD

    private val _checkedA = MutableLiveData<Boolean>()
    val checkedA: LiveData<Boolean> = _checkedA

    private val _checkedB = MutableLiveData<Boolean>()
    val checkedB: LiveData<Boolean> = _checkedB

    private val _checkedC = MutableLiveData<Boolean>()
    val checkedC: LiveData<Boolean> = _checkedC

    private val _checkedD = MutableLiveData<Boolean>()
    val checkedD: LiveData<Boolean> = _checkedD

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _answerBtnText = MutableLiveData("Antworten")
    val answerBtnText: LiveData<String> = _answerBtnText

    private val _favouriteColor = MutableLiveData<Int>()
    val favouriteColor: LiveData<Int> = _favouriteColor

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
        return ""
    }

    fun onChangeCheckedOption(selection: Boolean, checkedAnswer: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCheckedOptionCoroutine(selection, checkedAnswer)
        }
    }

    private suspend fun onChangeCheckedOptionCoroutine(selection: Boolean, checkedAnswer: String) =
        withContext(Dispatchers.IO) {
            var newSelectionValue = false
            if (selection) {
                newSelectionValue = false
            } else if (!selection) {
                newSelectionValue = true
            }
            when (checkedAnswer) {
                Constants.TRAINING_SELECTION_A -> _checkedA.postValue(newSelectionValue)
                Constants.TRAINING_SELECTION_B -> _checkedB.postValue(newSelectionValue)
                Constants.TRAINING_SELECTION_C -> _checkedC.postValue(newSelectionValue)
                Constants.TRAINING_SELECTION_D -> _checkedD.postValue(newSelectionValue)
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
            }
        }
        return checkedState.value
    }

    fun onChangeSelection(selection: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeSelectionCoroutine(selection)
        }
    }

    private suspend fun onChangeSelectionCoroutine(selection: String) =
        withContext(Dispatchers.IO) {
            when (selection) {
                Constants.TRAINING_SELECTION_A -> _selectA.postValue(selection)
                Constants.TRAINING_SELECTION_B -> _selectB.postValue(selection)
                Constants.TRAINING_SELECTION_C -> _selectC.postValue(selection)
                Constants.TRAINING_SELECTION_D -> _selectD.postValue(selection)
            }
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

    fun currentSelection(isChecked: Boolean, option: String) {
        if (!isChecked) {
            _checkedAnswers.add(option)
        } else {
            _checkedAnswers.remove(option)
        }
        Log.v("Selected Options", checkedAnswers.toString())
    }

    @Suppress("JavaCollectionsStaticMethodOnImmutableList")
    fun isSelectionCorrect(
        correctAnswers: QuestionProjection,
        currentCheckedAnswers: List<String>,
        checkBoxes: List<QuestionCheckbox>
    ): Boolean {
        var result = false
        Collections.sort(currentCheckedAnswers)

        if (correctAnswers.correctAnswers == currentCheckedAnswers.toString()) {
            result = true
        }
        changeCheckboxColors(correctAnswers.correctAnswers, checkBoxes)
        Log.v("Correct Answers", correctAnswers.correctAnswers)
        Log.v("Current Answers", currentCheckedAnswers.toString())
        Log.v("Question answered", result.toString())
        return result
    }

    //A selection is made of nested Pairs and contains all related checkbox values like isSelect, Value and Checkbox color
    private fun changeCheckboxColors(
        correctAnswers: String,
        checkBoxes: List<QuestionCheckbox>
    ) {
        checkBoxes.forEach { checkbox ->
            if (correctAnswers.contains(checkbox.option)) {
                when (checkbox.option) {
                    Constants.TRAINING_SELECTION_A -> checkbox.color = Color.Green
                    Constants.TRAINING_SELECTION_B -> checkbox.color = Color.Green
                    Constants.TRAINING_SELECTION_C -> checkbox.color = Color.Green
                    Constants.TRAINING_SELECTION_D -> checkbox.color = Color.Green
                }
            } else {
                when (checkbox.option) {
                    Constants.TRAINING_SELECTION_A -> checkbox.color = Color.Red
                    Constants.TRAINING_SELECTION_B -> checkbox.color = Color.Red
                    Constants.TRAINING_SELECTION_C -> checkbox.color = Color.Red
                    Constants.TRAINING_SELECTION_D -> checkbox.color = Color.Red
                }
            }
            //Check all Checkboxes in order to show correct and wrong selections
            _checkedA.postValue(true)
            _checkedB.postValue(true)
            _checkedC.postValue(true)
            _checkedD.postValue(true)
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

    fun resetSelections() {
        _checkedA.postValue(false)
        _checkedB.postValue(false)
        _checkedC.postValue(false)
        _checkedD.postValue(false)
        _checkBoxColorA.postValue(Color.Black)
        _checkBoxColorB.postValue(Color.Black)
        _checkBoxColorC.postValue(Color.Black)
        _checkBoxColorD.postValue(Color.Black)
        _checkedAnswers.clear()
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
        resetSelections()
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