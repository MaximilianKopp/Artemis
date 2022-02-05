package com.ataraxia.artemis.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM artemis_rlp ORDER BY topic")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query("SELECT * FROM artemis_rlp WHERE topic = :topic")
    fun getQuestionsByChapter(topic: String): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = ${Constants.CHAPTER_1}")
    fun getAllQuestionsFromChapterOne(): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = ${Constants.CHAPTER_2}")
    fun getAllQuestionsFromChapterTwo(): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = ${Constants.CHAPTER_3}")
    fun getAllQuestionsFromChapterThree(): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = ${Constants.CHAPTER_4}")
    fun getAllQuestionsFromChapterFour(): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = ${Constants.CHAPTER_5}")
    fun getAllQuestionsFromChapterFive(): List<Question>

    @Query("SELECT * FROM artemis_rlp WHERE topic = ${Constants.CHAPTER_6}")
    fun getAllQuestionsFromChapterSix(): List<Question>

    @Update(onConflict = REPLACE)
    suspend fun updateQuestion(question: Question)

    @Query("SELECT count(*) FROM artemis_rlp")
    fun isEmpty(): Int
}