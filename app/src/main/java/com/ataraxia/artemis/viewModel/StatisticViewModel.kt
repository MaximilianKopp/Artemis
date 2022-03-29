package com.ataraxia.artemis.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ataraxia.artemis.data.db.ArtemisDatabase
import com.ataraxia.artemis.data.statistics.StatisticRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class StatisticViewModel(application: Application) : AndroidViewModel(Application()) {

    private val _allQuestionsCount = MutableLiveData<Int>()
    val allQuestionsCount: LiveData<Int> = _allQuestionsCount

    private val _allLearnedOnceQuestionCount = MutableLiveData<Int>()
    val allLearnedOnceQuestionsCount: LiveData<Int> = _allLearnedOnceQuestionCount

    private val _allLearnedQuestionCount = MutableLiveData<Int>()
    val allLearnedQuestionsCount: LiveData<Int> = _allLearnedQuestionCount

    private val _allFailedQuestionCount = MutableLiveData<Int>()
    val allFailedQuestionCount: LiveData<Int> = _allFailedQuestionCount

    private val _progressInPercent = MutableLiveData<BigDecimal>()
    val progressInPercent: LiveData<BigDecimal> = _progressInPercent

    private val statisticRepository: StatisticRepository =
        StatisticRepository(ArtemisDatabase.getDatabase(application).statisticDao())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _allQuestionsCount.postValue(statisticRepository.getAllQuestionsCount())
            _allLearnedOnceQuestionCount.postValue(statisticRepository.getAllLearnedOnceQuestionsCount())
            _allLearnedQuestionCount.postValue(statisticRepository.getAllLearnedQuestionsCount())
            _allFailedQuestionCount.postValue(statisticRepository.getAllFailedQuestionsCount())
            _progressInPercent.postValue(
                calculatePercentage(
                    statisticRepository.getAllLearnedQuestionsCount(),
                    statisticRepository.getAllQuestionsCount()
                )
            )
        }
    }

    fun onChangeTotalStatisticsFromStartScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTotalStatisticsFromStartScreenCoroutine()
        }
    }

    private suspend fun onChangeTotalStatisticsFromStartScreenCoroutine() =
        withContext(Dispatchers.IO) {
            _allQuestionsCount.postValue(statisticRepository.getAllQuestionsCount())
            _allLearnedOnceQuestionCount.postValue(statisticRepository.getAllLearnedOnceQuestionsCount())
            _allLearnedQuestionCount.postValue(statisticRepository.getAllLearnedQuestionsCount())
            _allFailedQuestionCount.postValue(statisticRepository.getAllFailedQuestionsCount())
            _progressInPercent.postValue(
                calculatePercentage(
                    statisticRepository.getAllLearnedQuestionsCount(),
                    statisticRepository.getAllQuestionsCount()
                )
            )
        }


    private fun calculatePercentage(learnedQuestions: Int, allQuestions: Int): BigDecimal {
        val learnedQuestionsInPercent =
            BigDecimal(
                (learnedQuestions.toDouble() / allQuestions.toDouble()) * 100.0
            ).setScale(
                2,
                RoundingMode.HALF_UP
            )
        return learnedQuestionsInPercent.setScale(
            if (
                learnedQuestionsInPercent.compareTo(BigDecimal(100.0)) == 0 ||
                learnedQuestionsInPercent.compareTo(BigDecimal(0.0)) == 0
            )
                0 else 2,
            RoundingMode.HALF_UP
        )
    }
}