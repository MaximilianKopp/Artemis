package com.artemis.hunterexam.data.questions

import com.artemis.hunterexam.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    suspend fun getAllQuestions() = questionDao.getAllQuestions()

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)
}