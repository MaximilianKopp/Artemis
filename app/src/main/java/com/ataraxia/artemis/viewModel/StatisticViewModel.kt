package com.ataraxia.artemis.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.math.BigDecimal
import java.math.RoundingMode

class StatisticViewModel(application: Application) : AndroidViewModel(Application()) {

    private val _allQuestionsCount = MutableLiveData<Int>()
    val allQuestionsCount: LiveData<Int> = _allQuestionsCount

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