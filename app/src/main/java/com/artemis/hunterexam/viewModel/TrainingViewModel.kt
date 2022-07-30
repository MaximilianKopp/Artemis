package com.artemis.hunterexam.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.model.QuestionCheckbox
import com.artemis.hunterexam.model.QuestionProjection
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
    private val checkedAnswers: Set<String> = _checkedAnswers as HashSet<String>

    fun onChangeTrainingData(trainingData: List<QuestionProjection>) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTrainingDataCoroutine(trainingData)
        }
    }

    private suspend fun onChangeTrainingDataCoroutine(newTrainingData: List<QuestionProjection>) =
        withContext(Dispatchers.IO) {
            _trainingData.postValue(newTrainingData)
        }

    fun setCurrentCheckboxText(question: QuestionProjection, checkedAnswer: String): String {
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
}