package com.ataraxia.artemis.data.questions

import com.ataraxia.artemis.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    suspend fun getAllQuestions() = questionDao.getAllQuestions()

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)
}