package com.ataraxia.artemis.data

import com.ataraxia.artemis.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    val getAllQuestions = questionDao.getAllQuestions()

    fun getQuestionsByTopic(topic: String): List<Question> = questionDao.getQuestionsByTopic(topic)

    fun isEmpty(): Int = questionDao.isEmpty()
}