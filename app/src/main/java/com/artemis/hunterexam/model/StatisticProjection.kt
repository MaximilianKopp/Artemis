package com.artemis.hunterexam.model

import java.math.BigDecimal

class StatisticProjection(
    val topic: String,
    val totalOnceLearned: Int,
    val totalLearned: Int,
    val totalFailed: Int,
    val totalPercentage: BigDecimal
)