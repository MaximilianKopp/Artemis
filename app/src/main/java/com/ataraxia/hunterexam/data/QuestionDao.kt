package com.ataraxia.hunterexam.data
import com.ataraxia.hunterexam.helper.Constants
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.ataraxia.hunterexam.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM hunter_exam ORDER BY topic")
    fun consoleTest(): List<Question>

    @Query("SELECT * FROM hunter_exam ORDER BY topic")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query("SELECT * FROM hunter_exam WHERE topic = ${Constants.CHAPTER_1}")
    fun getAllQuestionsFromChapterOne(): LiveData<List<Question>>

    @Query("SELECT * FROM hunter_exam WHERE topic = ${Constants.CHAPTER_2}")
    fun getAllQuestionsFromChapterTwo(): LiveData<List<Question>>

    @Query("SELECT * FROM hunter_exam WHERE topic = ${Constants.CHAPTER_3}")
    fun getAllQuestionsFromChapterThree(): LiveData<List<Question>>

    @Query("SELECT * FROM hunter_exam WHERE topic = ${Constants.CHAPTER_4}")
    fun getAllQuestionsFromChapterFour(): LiveData<List<Question>>

    @Query("SELECT * FROM hunter_exam WHERE topic = ${Constants.CHAPTER_5}")
    fun getAllQuestionsFromChapterFive(): LiveData<List<Question>>

    @Query("SELECT * FROM hunter_exam WHERE topic = ${Constants.CHAPTER_6}")
    fun getAllQuestionsFromChapterSix(): LiveData<List<Question>>

    @Query("SELECT count(*) FROM hunter_exam")
    fun isEmpty() : Int
}