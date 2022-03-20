package com.ataraxia.artemis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.ataraxia.artemis.helper.RowScopeExtension.Companion.TableCell
import com.ataraxia.artemis.model.Screen.DrawerScreen.Statistics
import com.ataraxia.artemis.ui.theme.GREEN_ARTEMIS
import com.ataraxia.artemis.ui.theme.RED_ARTEMIS
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS
import com.ataraxia.artemis.viewModel.QuestionViewModel

class StatisticComponent {

    @Composable
    fun StatisticScreen(questionViewModel: QuestionViewModel) {
        StatisticsContent(questionViewModel)
    }

    @Composable
    fun StatisticsContent(questionViewModel: QuestionViewModel) {
        val allQuestions = questionViewModel.allQuestions
        val onceLearnedQuestions = questionViewModel.onceLearnedQuestions
        val learnedQuestions = questionViewModel.learnedQuestions
        val failedQuestions = questionViewModel.failedQuestions
        val progressInPercent = questionViewModel.progressInPercent

        Column {
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Text(
                    text = Statistics.title,
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp, bottom = 10.dp)
                )
            }
            Divider(thickness = 2.dp, color = Color.White, startIndent = 25.dp)
            Text(
                text = "Alle Fragen: ${allQuestions.count()}",
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
            LazyColumn(
                modifier = Modifier

                    .padding(25.dp)
            ) {
                items(
                    items = questionViewModel.extractStatisticsFromTopics()
                ) { item ->
                    Row(
                        Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        GREEN_ARTEMIS,
                                        YELLOW_ARTEMIS
                                    )
                                ), shape = RectangleShape
                            )
                            .padding(5.dp)
                    ) {
                        TableCell(text = item.topic, weight = 4.75f)
                        TableCell(
                            text = item.totalOnceLearned.toString(),
                            Icons.Filled.Done,
                            YELLOW_ARTEMIS,
                            weight = 1.25f
                        )
                        TableCell(
                            text = item.totalLearned.toString(),
                            Icons.Filled.Done,
                            GREEN_ARTEMIS,
                            weight = 1.25f
                        )
                        TableCell(
                            text = item.totalFailed.toString(),
                            Icons.Filled.Close,
                            RED_ARTEMIS,
                            weight = 1.25f
                        )
                        TableCell(
                            text = item.totalPercentage.toString() + "%",
                            weight = 1.50f
                        )
                    }
                }
            }
        }
    }
}