package com.ataraxia.hunterexam.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ataraxia.hunterexam.data.QuestionDatabase
import com.ataraxia.hunterexam.data.QuestionRepository

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val questionRepository: QuestionRepository
    val consoleTest: List<Question>
    val allQuestions: LiveData<List<Question>>
    val allQuestionsFromChapterOne: LiveData<List<Question>>
    val allQuestionsFromChapterTwo: LiveData<List<Question>>
    val allQuestionsFromChapterThree: LiveData<List<Question>>
    val allQuestionsFromChapterFour: LiveData<List<Question>>
    val allQuestionsFromChapterFive: LiveData<List<Question>>
    val allQuestionsFromChapterSix: LiveData<List<Question>>

    init {
        val questionDao = QuestionDatabase.getDatabase(application.applicationContext).questionDao()
        questionRepository = QuestionRepository(questionDao)
        consoleTest = questionRepository.consoleTest
        allQuestions = questionRepository.getAllQuestions
        allQuestionsFromChapterOne = questionRepository.getAllQuestionsFromChapterOne
        allQuestionsFromChapterTwo = questionRepository.getAllQuestionsFromChapterTwo
        allQuestionsFromChapterThree = questionRepository.getAllQuestionsFromChapterThree
        allQuestionsFromChapterFour = questionRepository.getAllQuestionsFromChapterFour
        allQuestionsFromChapterFive = questionRepository.getAllQuestionsFromChapterFive
        allQuestionsFromChapterSix = questionRepository.getAllQuestionsFromChapterSix
    }

    fun isEmpty(): Int = questionRepository.isEmpty()
}