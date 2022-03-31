package com.ataraxia.artemis.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssignmentViewModel : ViewModel() {

    private val _indexOfCurrentQuestion = MutableLiveData<Int>()
    val indexOfCurrentQuestion: LiveData<Int> = _indexOfCurrentQuestion

    fun onChangeIndexOfCurrentQuestion(index: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeIndexOfCurrentQuestionCoroutine(index)
        }
    }

    private suspend fun onChangeIndexOfCurrentQuestionCoroutine(index: Int) =
        withContext(Dispatchers.IO) {
            _indexOfCurrentQuestion.postValue(index)
        }
}