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

class StatisticComponent {

    @Composable
    fun StatisticScreen(
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        StatisticsContent(
            questionViewModel,
            navController
        )
    }

    @Composable
    fun StatisticsContent(
        questionViewModel: QuestionViewModel,
        navController: NavController
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
                        text = "1x richtig beantwortet: ${questionViewModel.extractTotalStatistics()["OnceLearnedTotal"]}",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                    Text(
                        text = "Falsch beantwortet: ${questionViewModel.extractTotalStatistics()["FailedTotal"]}",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                }
                Column {
                    Text(
                        text = "2x richtig beantwortet: ${questionViewModel.extractTotalStatistics()["TwiceLearnedTotal"]}",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 25.dp, top = 10.dp)
                    )
                    Text(
                        text = "${questionViewModel.extractTotalStatistics()["TotalPercentage"]}% gelernt",
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