package com.ataraxia.artemis.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.model.Question
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val questionRepository: QuestionRepository
    val allQuestionsFromChapterOne: List<Question>
    val allQuestionsFromChapterTwo: List<Question>
    val allQuestionsFromChapterThree: List<Question>
    val allQuestionsFromChapterFour: List<Question>
    val allQuestionsFromChapterFive: List<Question>
    val allQuestionsFromChapterSix: List<Question>

    init {
        val questionDao = QuestionDatabase.getDatabase(application.applicationContext).questionDao()
        questionRepository = QuestionRepository(questionDao)
        allQuestionsFromChapterOne = questionRepository.getAllQuestionsFromChapterOne
        allQuestionsFromChapterTwo = questionRepository.getAllQuestionsFromChapterTwo
        allQuestionsFromChapterThree = questionRepository.getAllQuestionsFromChapterThree
        allQuestionsFromChapterFour = questionRepository.getAllQuestionsFromChapterFour
        allQuestionsFromChapterFive = questionRepository.getAllQuestionsFromChapterFive
        allQuestionsFromChapterSix = questionRepository.getAllQuestionsFromChapterSix
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            questionRepository.updateQuestion(question)
        }
    }

}