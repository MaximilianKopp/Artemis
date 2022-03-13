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
import java.util.*

class TrainingViewModel : ViewModel() {

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion = _currentQuestion

    private val _trainingQuestions = MutableLiveData<List<Question>>()
    val trainingQuestions = _trainingQuestions

    private val _trainingData = MutableLiveData<List<Question>>()
    val trainingData: LiveData<List<Question>> = _trainingData

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

    private val _isNavDialogOpen = MutableLiveData<Boolean>()
    val isNavDialogOpen: LiveData<Boolean> = _isNavDialogOpen

    fun onChangeTrainingData(traningData: List<Question>) {
        viewModelScope.launch {
            onChangeTrainingDataCoroutine(traningData)
        }
    }

    private suspend fun onChangeTrainingDataCoroutine(newTrainingData: List<Question>) =
        withContext(Dispatchers.IO) {
            _trainingData.postValue(newTrainingData)
        }

    fun onChangeCurrentQuestion(newQuestion: Question) {
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
        Log.v("Current index", (newIndex + 1).toString())
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
                Constants.TRAINING_SELECTION_D -> _selectD.postValue(selection)
            }
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

    fun currentSelection(isChecked: Boolean, option: String) {
        if (!isChecked) {
            _checkedAnswers.add(option)
        } else {
            _checkedAnswers.remove(option)
        }
        Log.v("Selected Options", checkedAnswers.toString())
    }

    fun isSelectionCorrect(
        correctAnswers: Question,
        currentCheckedAnswers: List<String>,
        selections: List<Pair<Pair<Boolean, Color>, String>>
    ): Boolean {
        var result = false
        Collections.sort(currentCheckedAnswers)

        if (correctAnswers.correctAnswers == currentCheckedAnswers.toString()) {
            result = true
        }
        changeCheckboxColors(correctAnswers.correctAnswers, selections)
        Log.v("Correct Answers", correctAnswers.correctAnswers)
        Log.v("Current Answers", currentCheckedAnswers.toString())
        Log.v("Question answered", result.toString())
        return result
    }

    //A selection is made of nested Pairs and contains all related checkbox values like isSelect, Value and Checkbox color
    private fun changeCheckboxColors(
        correctAnswers: String,
        selections: List<Pair<Pair<Boolean, Color>, String>>
    ) {
        selections.forEach { selection ->
            if (correctAnswers.contains(selection.second)) {
                when (selection.second) {
                    Constants.TRAINING_SELECTION_A -> _checkBoxColorA.postValue(Color.Green)
                    Constants.TRAINING_SELECTION_B -> _checkBoxColorB.postValue(Color.Green)
                    Constants.TRAINING_SELECTION_C -> _checkBoxColorC.postValue(Color.Green)
                    Constants.TRAINING_SELECTION_D -> _checkBoxColorD.postValue(Color.Green)
                }
            } else {
                when (selection.second) {
                    Constants.TRAINING_SELECTION_A -> _checkBoxColorA.postValue(Color.Red)
                    Constants.TRAINING_SELECTION_B -> _checkBoxColorB.postValue(Color.Red)
                    Constants.TRAINING_SELECTION_C -> _checkBoxColorC.postValue(Color.Red)
                    Constants.TRAINING_SELECTION_D -> _checkBoxColorD.postValue(Color.Red)
                }
            }
            //Check all Checkboxes in order to show correct and wrong selections
            _checkedA.postValue(true)
            _checkedB.postValue(true)
            _checkedC.postValue(true)
            _checkedD.postValue(true)
        }
    }

    fun onChangeEnableButtons(enabled: Boolean) {
        viewModelScope.launch {
            onChangeEnableButtonsCoroutine(enabled)
        }
    }

    private suspend fun onChangeEnableButtonsCoroutine(enabled: Boolean) =
        withContext(Dispatchers.IO) {
            _isButtonEnabled.postValue(enabled)
        }

    fun loadNextQuestion() {
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
    fun setNavTrainingButton(direction: NavTrainingButton, index: Int, questions: List<Question>) {
        when (direction) {
            NavTrainingButton.FIRST_PAGE -> firstPage(questions)
            NavTrainingButton.PREV_PAGE -> prevPage(index, questions)
            NavTrainingButton.NEXT_PAGE -> nextPage(index, questions)
            NavTrainingButton.LAST_PAGE -> lastPage(questions)
        }
    }

    private fun firstPage(questions: List<Question>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(questions[0])
    }

    private fun prevPage(index: Int, questions: List<Question>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(questions[index - 1])
        }
    }

    private fun nextPage(index: Int, questions: List<Question>) {
        if (index < questions.size - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(questions[index + 1])
        }
    }

    private fun lastPage(questions: List<Question>) {
        onChangeIndex(questions.size - 1)
        onChangeCurrentQuestion(questions[questions.size - 1])
    }

    fun onOpenNavDialog(isOpen: Boolean) {
        viewModelScope.launch {
            onOpenNavDialogCoroutine(isOpen)
        }
    }

    private suspend fun onOpenNavDialogCoroutine(isOpen: Boolean) = withContext(Dispatchers.IO) {
        _isNavDialogOpen.postValue(isOpen)
    }
}