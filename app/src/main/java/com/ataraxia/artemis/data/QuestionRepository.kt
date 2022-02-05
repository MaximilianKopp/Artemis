package com.ataraxia.artemis.data

import com.ataraxia.artemis.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    val getAllQuestions = questionDao.getAllQuestions()

    //val getQuestionsByChapter: List<Question> = questionDao.getQuestionsByChapter(topic)

    val getAllQuestionsFromChapterOne: List<Question> = questionDao.getAllQuestionsFromChapterOne()

    val getAllQuestionsFromChapterTwo: List<Question> = questionDao.getAllQuestionsFromChapterTwo()

    val getAllQuestionsFromChapterThree: List<Question> = questionDao.getAllQuestionsFromChapterThree()

    val getAllQuestionsFromChapterFour: List<Question> = questionDao.getAllQuestionsFromChapterFour()

    val getAllQuestionsFromChapterFive: List<Question> = questionDao.getAllQuestionsFromChapterFive()

    val getAllQuestionsFromChapterSix: List<Question> = questionDao.getAllQuestionsFromChapterSix()

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)

    fun isEmpty(): Int = questionDao.isEmpty()
}