package com.ataraxia.artemis.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val checkedA = _checkedA

    private val _checkedB = MutableLiveData<Boolean>()
    val checkedB = _checkedB

    private val _checkedC = MutableLiveData<Boolean>()
    val checkedC = _checkedC

    private val _checkedD = MutableLiveData<Boolean>()
    val checkedD = _checkedD

    fun onChangeCurrentQuestion(newQuestion: Question) {
        viewModelScope.launch {
            onChangeCurrentQuestionCoroutine(newQuestion)
        }
    }

    private suspend fun onChangeCurrentQuestionCoroutine(newQuestion: Question) =
        withContext(Dispatchers.IO) {
            _currentQuestion.postValue(newQuestion)
        }

    fun onChangeIndex(newIndex: Int) {
        viewModelScope.launch {
            onChangeIndexCoroutine(newIndex)
        }
    }

    private suspend fun onChangeIndexCoroutine(newIndex: Int) =
        withContext(Dispatchers.IO) {
            _index.postValue(newIndex)
        }

    fun onChangeSelectA(selectA: String) {
        viewModelScope.launch {
            onChangeSelectACoroutine(selectA)
        }
    }

    private suspend fun onChangeSelectACoroutine(selectA: String) =
        withContext(Dispatchers.IO) {
            _selectA.postValue(selectA)
        }

    fun onChangeSelectB(selectB: String) {
        viewModelScope.launch {
            onChangeSelectBCoroutine(selectB)
        }
    }

    private suspend fun onChangeSelectBCoroutine(selectB: String) =
        withContext(Dispatchers.IO) {
            _selectB.postValue(selectB)
        }

    fun onChangeSelectC(selectC: String) {
        viewModelScope.launch {
            onChangeSelectCCoroutine(selectC)
        }
    }

    fun onChangeSelectD(selectD: String) {
        viewModelScope.launch {
            onChangeSelectDCoroutine(selectD)
        }
    }

    private suspend fun onChangeSelectDCoroutine(selectD: String) =
        withContext(Dispatchers.IO) {
            _selectD.postValue(selectD)
        }

    private suspend fun onChangeSelectCCoroutine(selectC: String) =
        withContext(Dispatchers.IO) {
            _selectC.postValue(selectC)
        }

    fun onChangecurrentCheckedAnswersList(checkedAnswer: String): String {
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
}