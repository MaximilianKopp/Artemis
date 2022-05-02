package com.ataraxia.artemis.data.statistics

class StatisticRepository(private val statisticDao: StatisticDao) {

    fun getAllQuestionsCount(): Int = statisticDao.getAllQuestionsCount()
    fun getAllLearnedOnceQuestionsCount(): Int = statisticDao.getAllLearnedOnceQuestionsCount()
    fun getAllLearnedQuestionsCount(): Int = statisticDao.getAllLearnedQuestionsCount()
    fun getAllFailedQuestionsCount(): Int = statisticDao.getAllFailedQuestionsCount()

    fun getLearnedOnceQuestionsCountByTopic(topic: String): Int =
        statisticDao.getLearnedOnceQuestionsByTopic(topic)

    fun getLearnedQuestionsCountByTopic(topic: String): Int =
        statisticDao.getLearnedQuestionsByTopic(topic)

    fun getFailedQuestionsCountByTopic(topic: String): Int =
        statisticDao.getFailedQuestionsByTopic(topic)

    suspend fun updateAllQuestionsCount(name: String) = statisticDao.updateAllQuestionsCount(name)
}