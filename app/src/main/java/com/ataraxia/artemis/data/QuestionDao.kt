package com.ataraxia.artemis.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.ataraxia.artemis.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM hunter_exam WHERE topic = :topic")
    fun getQuestionsByTopic(topic: String): List<Question>

    @Query("SELECT * FROM hunter_exam ORDER BY topic")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query("SELECT count(*) FROM hunter_exam")
    fun isEmpty(): Int
}