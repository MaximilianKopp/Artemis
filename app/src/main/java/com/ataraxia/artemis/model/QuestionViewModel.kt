package com.ataraxia.artemis.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ataraxia.artemis.data.QuestionDatabase
import com.ataraxia.artemis.data.QuestionRepository

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val questionRepository: QuestionRepository

    init {
        val questionDao = QuestionDatabase.getDatabase(application.applicationContext).questionDao()
        questionRepository = QuestionRepository(questionDao)
    }

    fun allQuestionsByTopic(topic: String) = questionRepository.getQuestionsByTopic(topic = topic)
    fun isEmpty(): Int = questionRepository.isEmpty()
}