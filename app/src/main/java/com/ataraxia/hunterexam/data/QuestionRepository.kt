package com.ataraxia.hunterexam.data

import androidx.lifecycle.LiveData
import com.ataraxia.hunterexam.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    val consoleTest = questionDao.consoleTest()

    val getAllQuestions = questionDao.getAllQuestions()

    val getAllQuestionsFromChapterOne: LiveData<List<Question>> = questionDao.getAllQuestionsFromChapterOne()

    val getAllQuestionsFromChapterTwo: LiveData<List<Question>> = questionDao.getAllQuestionsFromChapterTwo()

    val getAllQuestionsFromChapterThree: LiveData<List<Question>> = questionDao.getAllQuestionsFromChapterThree()

    val getAllQuestionsFromChapterFour: LiveData<List<Question>> = questionDao.getAllQuestionsFromChapterFour()

    val getAllQuestionsFromChapterFive: LiveData<List<Question>> = questionDao.getAllQuestionsFromChapterFive()

    val getAllQuestionsFromChapterSix: LiveData<List<Question>> = questionDao.getAllQuestionsFromChapterSix()

    fun isEmpty(): Int = questionDao.isEmpty()
}