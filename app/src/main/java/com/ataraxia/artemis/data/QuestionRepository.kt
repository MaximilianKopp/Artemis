package com.ataraxia.artemis.data

import com.ataraxia.artemis.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    fun getAllQuestions() = questionDao.getAllQuestions()

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)

    fun getQuestionsByChapter(topic: Int): List<Question> = questionDao.getQuestionsByChapter(topic)

    fun isEmpty(): Int = questionDao.isEmpty()
}