package com.ataraxia.artemis.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavigationButton
import com.ataraxia.artemis.model.QuestionProjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssignmentViewModel : ViewModel() {

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion = _currentQuestion

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

    private fun resetSelections() {
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

    fun currentSelection(isChecked: Boolean, option: String) {
        if (!isChecked) {
            _checkedAnswers.add(option)
        } else {
            _checkedAnswers.remove(option)
        }
        Log.v("Selected Options", checkedAnswers.toString())
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
        questions: List<QuestionProjection>
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
            NavigationButton.SKIPPED_INDEX -> skippedIndex(questions, index)
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

    private fun skippedIndex(questions: List<QuestionProjection>, skippedIndex: Int) {
        val index = if (skippedIndex == 0) 0 else skippedIndex - 1
        onChangeIndex(index)
        onChangeCurrentQuestion(questions[index])
        onChangeFavouriteState(questions[index].favourite)
    }
}