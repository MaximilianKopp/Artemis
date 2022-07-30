package com.artemis.hunterexam.data.questions

import androidx.room.Dao
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.artemis.hunterexam.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions ORDER BY topic")
    suspend fun getAllQuestions(): List<Question>

    @Update(onConflict = REPLACE)
    suspend fun updateQuestion(question: Question)
}