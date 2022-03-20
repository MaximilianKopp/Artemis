package com.ataraxia.artemis.data.statistics

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StatisticDao {

    @Query("SELECT COUNT(*) FROM questions")
    fun getAllQuestionsCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE learnedOnce = 1")
    fun getAllLearnedOnceQuestionsCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE learnedTwice = 1")
    fun getAllLearnedQuestionsCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE failed = 1")
    fun getAllFailedQuestionsCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE topic =:topic")
    fun getAllQuestionsByTopic(topic: String): Int

    @Query("SELECT COUNT(*) FROM questions WHERE topic =:topic AND learnedOnce = 1")
    fun getLearnedOnceQuestionsByTopic(topic: String): Int

    @Query("SELECT COUNT(*) FROM questions WHERE topic =:topic AND learnedTwice = 1")
    fun getLearnedQuestionsByTopic(topic: String): Int

    @Query("SELECT COUNT(*) FROM questions WHERE topic =:topic AND failed = 1")
    fun getFailedQuestionsByTopic(topic: String): Int


    @Query("UPDATE statistics SET VALUE = (SELECT COUNT(*) FROM questions) WHERE name =:name")
    suspend fun updateAllQuestionsCount(name: String)
}