package com.ataraxia.artemis.data

import androidx.room.Dao
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.ataraxia.artemis.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions ORDER BY topic")
    fun getAllQuestions(): List<Question>

    @Update(onConflict = REPLACE)
    suspend fun updateQuestion(question: Question)

    @Query("SELECT COUNT(topic) FROM questions WHERE topic =:topic AND failed = 0")
    fun countFailedQuestions(topic: Int): Int
}