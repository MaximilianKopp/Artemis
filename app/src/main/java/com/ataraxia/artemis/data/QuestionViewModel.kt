package com.ataraxia.artemis.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    private val _questions = MutableLiveData<List<Question>>()
    val questions = _questions

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

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            questionRepository.updateQuestion(question)
        }
    }

    fun loadQuestions(chapter: String) {
        viewModelScope.launch {
            loadQuestionsCoroutine(chapter)
        }
    }

    private suspend fun loadQuestionsCoroutine(chapter: String) =
        withContext(Dispatchers.IO) {
            var questions = listOf<Question>()
            when (chapter) {
                Constants.CHAPTER_1 -> questions = questionsChapter1
                Constants.CHAPTER_2 -> questions = questionsChapter2
                Constants.CHAPTER_3 -> questions = questionsChapter3
                Constants.CHAPTER_4 -> questions = questionsChapter4
                Constants.CHAPTER_5 -> questions = questionsChapter5
                Constants.CHAPTER_6 -> questions = questionsChapter6
            }
            _questions.postValue(questions)
        }
}