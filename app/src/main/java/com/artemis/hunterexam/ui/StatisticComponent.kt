package com.artemis.hunterexam.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.Constants.Companion.STATISTICS_TOTAL_PERCENTAGE
import com.artemis.hunterexam.helper.TableCell
import com.artemis.hunterexam.ui.theme.Artemis_Green
import com.artemis.hunterexam.ui.theme.Artemis_Red
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import com.artemis.hunterexam.viewModel.QuestionViewModel

class StatisticComponent {

    @Composable
    fun StatisticScreen(
        questionViewModel: QuestionViewModel
    ) {
        StatisticsContent(
            questionViewModel,
        )
    }

    @Composable
    fun StatisticsContent(
        questionViewModel: QuestionViewModel,
    ) {
        val allQuestions: Int = questionViewModel.allQuestions.size
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
                    modifier = Modifier.padding(start = 10.dp, top = 25.dp, bottom = 10.dp)
                )
            }
            Divider(thickness = 2.dp, color = Color.White, startIndent = 10.dp)
            Text(
                text = "Alle Fragen: $allQuestions",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 10.dp, top = 25.dp)
            )
            Row {
                Text(
                    text = "1x richtig beantwortet: ${questionViewModel.extractTotalStatistics()[Constants.STATISTICS_ONCE_LEARNED_TOTAL]}",
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .weight(0.5f)
                )
                Text(
                    text = "2x richtig beantwortet: ${questionViewModel.extractTotalStatistics()[Constants.STATISTICS_TWICE_LEARNED_TOTAL]}",
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .weight(0.5f)
                )
            }
            Row {
                Text(
                    text = "Falsch beantwortet: ${questionViewModel.extractTotalStatistics()[Constants.STATISTICS_FAILED_TOTAL]}",
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .weight(0.5f)
                )
                Text(
                    text = "${questionViewModel.extractTotalStatistics()[STATISTICS_TOTAL_PERCENTAGE]}% gelernt",
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .weight(0.5f)
                )
            }
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Text(
                    text = "Thematisch",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 10.dp, top = 25.dp, bottom = 10.dp)
                )
            }
            val statistics = questionViewModel.extractStatisticsFromTopics()
            Divider(thickness = 2.dp, color = Color.White, startIndent = 10.dp)
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
                    TableCell(
                        text = statistic.topic + " " + statistic.totalPercentage.toString() + "%",
                        weight = 4.0f
                    )
                    TableCell(
                        text = statistic.totalLearned.toString(),
                        Icons.Filled.Done,
                        Artemis_Green,
                        weight = 2.0f
                    )
                    TableCell(
                        text = statistic.totalOnceLearned.toString(),
                        Icons.Filled.Done,
                        Artemis_Yellow,
                        weight = 2.0f
                    )
                    TableCell(
                        text = statistic.totalFailed.toString(),
                        Icons.Filled.Close,
                        Artemis_Red,
                        weight = 2.0f
                    )
                }
            }
        }
    }
}