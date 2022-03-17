package com.ataraxia.artemis.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.data.db.QuestionDatabase
import com.ataraxia.artemis.data.statistics.StatisticRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticViewModel(application: Application) : AndroidViewModel(Application()) {

    private val _allQuestionsCount = MutableLiveData<Int>()
    val allQuestionsCount: LiveData<Int> = _allQuestionsCount

    private val _allLearnedOnceQuestionCount = MutableLiveData<Int>()
    val allLearnedOnceQuestionsCount: LiveData<Int> = _allLearnedOnceQuestionCount

    private val _allLearnedQuestionCount = MutableLiveData<Int>()
    val allLearnedQuestionsCount: LiveData<Int> = _allLearnedQuestionCount

    private val _allFailedQuestionCount = MutableLiveData<Int>()
    val allFailedQuestionCount: LiveData<Int> = _allFailedQuestionCount

    private val statisticRepository: StatisticRepository =
        StatisticRepository(QuestionDatabase.getDatabase(application).statisticDao())

    init {
        _allQuestionsCount.postValue(statisticRepository.getAllQuestionsCount())
        _allLearnedOnceQuestionCount.postValue(statisticRepository.getAllLearnedOnceQuestionsCount())
        _allLearnedQuestionCount.postValue(statisticRepository.getAllLearnedQuestionsCount())
        _allFailedQuestionCount.postValue(statisticRepository.getAllFailedQuestionsCount())
    }

    fun onChangeTotalStatisticsFromStartScreen() {
        viewModelScope.launch {
            onChangeTotalStatisticsFromStartScreenCoroutine()
        }
    }

    private suspend fun onChangeTotalStatisticsFromStartScreenCoroutine() =
        withContext(Dispatchers.IO) {
            _allQuestionsCount.postValue(statisticRepository.getAllQuestionsCount())
            _allLearnedOnceQuestionCount.postValue(statisticRepository.getAllLearnedOnceQuestionsCount())
            _allLearnedQuestionCount.postValue(statisticRepository.getAllLearnedQuestionsCount())
            _allFailedQuestionCount.postValue(statisticRepository.getAllFailedQuestionsCount())
        }


}