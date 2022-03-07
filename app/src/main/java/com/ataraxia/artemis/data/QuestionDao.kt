package com.ataraxia.artemis.data

import androidx.room.Dao
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.ataraxia.artemis.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM artemis_rlp ORDER BY topic")
    fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = :topic")
    fun getQuestionsByChapter(topic: Int): List<Question>

    @Update(onConflict = REPLACE)
    suspend fun updateQuestion(question: Question)

    @Query("SELECT COUNT(topic) FROM artemis_rlp WHERE topic =:topic AND failed = 0")
    fun countFailedQuestions(topic: Int): Int

    @Query("SELECT count(*) FROM artemis_rlp")
    fun isEmpty(): Int
}