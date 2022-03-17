package com.ataraxia.artemis.model

import java.math.BigDecimal

class StatisticProjection(
    val topic: String,
    val totalSize: Int,
    val totalOnceLearned: Int,
    val totalLearned: Int,
    val totalFailed: Int,
    val totalPercentage: BigDecimal
)