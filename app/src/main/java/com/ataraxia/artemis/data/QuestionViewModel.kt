package com.ataraxia.artemis.data

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val questionRepository: QuestionRepository
    private val questionsChapter1: List<Question>
    private val questionsChapter2: List<Question>
    private val questionsChapter3: List<Question>
    private val questionsChapter4: List<Question>
    private val questionsChapter5: List<Question>
    private val questionsChapter6: List<Question>

    init {
        val questionDao = QuestionDatabase.getDatabase(application.applicationContext).questionDao()
        questionRepository = QuestionRepository(questionDao)
        questionsChapter1 = questionRepository.getAllQuestionsFromChapterOne
        questionsChapter2 = questionRepository.getAllQuestionsFromChapterTwo
        questionsChapter3 = questionRepository.getAllQuestionsFromChapterThree
        questionsChapter4 = questionRepository.getAllQuestionsFromChapterFour
        questionsChapter5 = questionRepository.getAllQuestionsFromChapterFive
        questionsChapter6 = questionRepository.getAllQuestionsFromChapterSix
    }

    fun onChangeQuestionList(newValue: List<Question>) {
        viewModelScope.launch {
            onChangeQuestionListCoroutine(newValue)
        }
    }

    private suspend fun onChangeQuestionListCoroutine(newValue: List<Question>) =
        withContext(Dispatchers.IO) {
            _questions.postValue(newValue)
        }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            questionRepository.updateQuestion(question)
        }
    }

    fun selectChapter(chapter: String): List<Question> {
        var questions = listOf<Question>()
        when (chapter) {
            Constants.CHAPTER_1 -> questions = questionsChapter1
            Constants.CHAPTER_2 -> questions = questionsChapter2
            Constants.CHAPTER_3 -> questions = questionsChapter3
            Constants.CHAPTER_4 -> questions = questionsChapter4
            Constants.CHAPTER_5 -> questions = questionsChapter5
            Constants.CHAPTER_6 -> questions = questionsChapter6
        }
        return questions
    }

//    private suspend fun prepareTrainingDataCoroutine(
//        questions: List<Question>,
//        filterCriteria: String
//    ) = withContext(Dispatchers.IO) {
//        val learnedOnceQuestions = questions.filter { it.learnedOnce == 1 && it.learnedTwice == 0 }
//        val learnedTwiceQuestions = questions.filter { it.learnedTwice == 1 }
//        val failedQuestions = questions.filter { it.failed == 1 }
//        val favourites = questions.filter { it.favourite == 1 }
//        val openQuestions = questions.toMutableList()
//        openQuestions.removeAll(learnedOnceQuestions)
//        openQuestions.removeAll(learnedTwiceQuestions)
//        openQuestions.removeAll(failedQuestions)
//
//        val trainingDataWithoutFilter = mutableListOf<Question>()
//        trainingDataWithoutFilter.addAll(failedQuestions.take(8))
//        trainingDataWithoutFilter.addAll(learnedOnceQuestions.take(5))
//        trainingDataWithoutFilter.addAll(learnedTwiceQuestions.take(2))
//        val sizeDifference = 30 - trainingDataWithoutFilter.size
//        trainingDataWithoutFilter.addAll(openQuestions.take(Constants.TRAINING_SIZE))
//
//        when (filterCriteria) {
//            Constants.FILTER_CRITERIA_ALL -> _trainingData.postValue(trainingDataWithoutFilter)
//            Constants.FILTER_CRITERIA_NOT_LEARNED -> _trainingData.postValue(learnedOnceQuestions)
//            Constants.FILTER_CRITERIA_FAILED -> _trainingData.postValue(failedQuestions)
//            Constants.FILTER_CRITERIA_FAVOURITES -> _trainingData.postValue(favourites)
//        }
//    }

    fun setQuestionStateColor(question: Question): Color {
        var result = Color.Black
        if (question.learnedOnce == 1 && question.failed == 0) {
            result = Color.Yellow
        }
        if (question.learnedTwice == 1 && question.failed == 0) {
            result = Color.Green
        }
        return result
    }
}