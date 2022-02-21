package com.ataraxia.artemis.data

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.NavTrainingButton
import com.ataraxia.artemis.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainingViewModel : ViewModel() {

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion

    private val _currentCheckedAnswersList = mutableListOf<String>()
    val currentCheckedAnswersList: List<String> = _currentCheckedAnswersList

    private val _index = MutableLiveData(0)
    val index: LiveData<Int> = _index

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

    private val _optionCheckBoxColor = MutableLiveData(Color.Black)
    val optionCheckBoxColor: LiveData<Color> = _optionCheckBoxColor

    private val _answerBtnText = MutableLiveData("Antworten")
    val answerBtnText: LiveData<String> = _answerBtnText

    private fun onChangeCurrentQuestion(newQuestion: Question) {
        viewModelScope.launch {
            onChangeCurrentQuestionCoroutine(newQuestion)
        }
    }

    private suspend fun onChangeCurrentQuestionCoroutine(newQuestion: Question) =
        withContext(Dispatchers.IO) {
            _currentQuestion.postValue(newQuestion)
        }

    private fun onChangeIndex(newIndex: Int) {
        viewModelScope.launch {
            onChangeIndexCoroutine(newIndex)
        }
    }

    private suspend fun onChangeIndexCoroutine(newIndex: Int) =
        withContext(Dispatchers.IO) {
            _index.postValue(newIndex)
        }

    fun setCurrentQuestionText(question: Question, checkedAnswer: String): String {
        when (checkedAnswer) {
            Constants.TRAINING_SELECTION_A -> return question.optionA
            Constants.TRAINING_SELECTION_B -> return question.optionB
            Constants.TRAINING_SELECTION_C -> return question.optionC
            Constants.TRAINING_SELECTION_D -> return question.optionD
        }
        return ""
    }

    fun onChangeCheckedOption(selection: Boolean, checkedAnswer: String) {
        viewModelScope.launch {
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

    fun onChangeSelection(selection: String) {
        viewModelScope.launch {
            onChangeSelectionCoroutine(selection)
        }
    }

    private suspend fun onChangeSelectionCoroutine(selection: String) =
        withContext(Dispatchers.IO) {
            when (selection) {
                Constants.TRAINING_SELECTION_A -> _selectA.postValue(selection)
                Constants.TRAINING_SELECTION_B -> _selectB.postValue(selection)
                Constants.TRAINING_SELECTION_C -> _selectC.postValue(selection)
                "" -> _selectC.postValue("")
            }
        }

    fun onChangeCurrentCheckedAnswersList(checkedAnswer: String): String {
        if (_currentCheckedAnswersList.contains(checkedAnswer)) {
            _currentCheckedAnswersList.remove(checkedAnswer)
        } else {
            _currentCheckedAnswersList.add(checkedAnswer)
        }
        Log.v("Current answer list", currentCheckedAnswersList.toString())
        return currentCheckedAnswersList.toString().trim()
    }

    fun submitCheckedAnswers(correctAnswers: String, currentCheckedAnswers: List<String>): Boolean {
        var result = false
        if (correctAnswers == currentCheckedAnswers.toString()) {
            result = true
        }
        Log.v("Correct or not?", result.toString())
        return result
    }

    fun setNavTrainingButton(direction: NavTrainingButton, index: Int, questions: List<Question>) {
        when (direction) {
            NavTrainingButton.FIRST_PAGE -> firstPage(index, questions)
            NavTrainingButton.PREV_PAGE -> prevPage(index, questions)
            NavTrainingButton.NEXT_PAGE -> nextPage(index, questions)
            NavTrainingButton.LAST_PAGE -> lastPage(index, questions)
        }
    }

    private fun firstPage(index: Int, questions: List<Question>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(questions[0])
        Log.v("Current index", index.toString())
    }

    private fun prevPage(index: Int, questions: List<Question>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(questions[index - 1])
            Log.v("Current index", index.toString())
        }
    }

    private fun nextPage(index: Int, questions: List<Question>) {
        if (index < Constants.TRAINING_SIZE - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(questions[index + 1])
            Log.v("Current index", index.toString())
        }
    }

    private fun lastPage(index: Int, questions: List<Question>) {
        onChangeIndex(questions.size - 1)
        onChangeCurrentQuestion(questions[questions.size - 1])
        Log.v("Current index", index.toString())
    }

    fun onChangeOptionCheckBoxColor(cbColor: Color) {
        viewModelScope.launch {
            onChangeOptionCheckBoxColorCoroutine(cbColor)
        }
    }

    private suspend fun onChangeOptionCheckBoxColorCoroutine(cbColor: Color) =
        withContext(Dispatchers.IO) {
            _optionCheckBoxColor.postValue(cbColor)
        }

    fun onChangeAnswerButtonText(answerBtnText: String) {
        viewModelScope.launch {
            onChangeAnswerButtonTextCoroutine(answerBtnText)
        }
    }

    private suspend fun onChangeAnswerButtonTextCoroutine(answerBtnText: String) =
        withContext(Dispatchers.IO) {
            _answerBtnText.postValue(answerBtnText)
        }

    fun resetQuestion() {
        _checkedA.postValue(false)
        _checkedB.postValue(false)
        _checkedC.postValue(false)
        _checkedD.postValue(false)
        _optionCheckBoxColor.postValue(Color.Black)
    }
}