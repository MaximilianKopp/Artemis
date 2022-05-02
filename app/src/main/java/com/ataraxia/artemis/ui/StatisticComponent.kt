package com.ataraxia.artemis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ataraxia.artemis.helper.RowScopeExtension.Companion.TableCell
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Red
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.QuestionViewModel
import com.ataraxia.artemis.viewModel.StatisticViewModel
import java.math.BigDecimal

class StatisticComponent {

    @Composable
    fun StatisticScreen(
        questionViewModel: QuestionViewModel,
        statisticViewModel: StatisticViewModel,
        navController: NavController
    ) {
        StatisticsContent(
            questionViewModel,
            statisticViewModel,
            navController
        )
    }

    @Composable
    fun StatisticsContent(
        questionViewModel: QuestionViewModel,
        statisticViewModel: StatisticViewModel,
        navController: NavController
    ) {
        val allQuestions: Int by statisticViewModel.allQuestionsCount.observeAsState(0)
        val onceLearnedQuestions: Int by statisticViewModel.allLearnedOnceQuestionsCount.observeAsState(
            0
        )
        val learnedQuestions: Int by statisticViewModel.allLearnedQuestionsCount.observeAsState(0)
        val failedQuestions: Int by statisticViewModel.allFailedQuestionCount.observeAsState(0)
        val progressInPercent: BigDecimal by statisticViewModel.progressInPercent.observeAsState(
            BigDecimal.ZERO
        )
        val scrollableState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(
                scrollableState, enabled = true
            )
        ) {
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Text(
                    text = "Gesamt",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp, bottom = 10.dp)
                )
            }
            Divider(thickness = 2.dp, color = Color.White, startIndent = 25.dp)
            Text(
                text = "Alle Fragen: $allQuestions",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 25.dp, top = 25.dp)
            )
            Row {
                Column {
                    Text(
                        text = "1x richtig beantwortet: $onceLearnedQuestions",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                    Text(
                        text = "Falsch beantwortet: $failedQuestions",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                }
                Column {
                    Text(
                        text = "2x richtig beantwortet: $learnedQuestions",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                    Text(
                        text = "$progressInPercent% gelernt",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Text(
                    text = "Thematisch",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp, bottom = 10.dp)
                )
            }
            val statistics = questionViewModel.extractStatisticsFromTopics()
            Divider(thickness = 2.dp, color = Color.White, startIndent = 25.dp)
            Spacer(modifier = Modifier.padding(15.dp))
            for (statistic in statistics) {
                Row(
                    Modifier
                        .border(1.dp, Color.White)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Artemis_Green,
                                    Artemis_Yellow
                                )
                            ), shape = RectangleShape
                        )
                        .padding(25.dp)
                ) {
                    TableCell(text = statistic.topic, weight = 4.75f)
                    TableCell(
                        text = statistic.totalOnceLearned.toString(),
                        Icons.Filled.Done,
                        Artemis_Yellow,
                        weight = 1.25f
                    )
                    TableCell(
                        text = statistic.totalLearned.toString(),
                        Icons.Filled.Done,
                        Artemis_Green,
                        weight = 1.25f
                    )
                    TableCell(
                        text = statistic.totalFailed.toString(),
                        Icons.Filled.Close,
                        Artemis_Red,
                        weight = 1.25f
                    )
                    TableCell(
                        text = statistic.totalPercentage.toString() + "%",
                        weight = 1.50f
                    )
                }
            }
        }
    }
}