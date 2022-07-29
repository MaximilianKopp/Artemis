package com.artemis.hunterexam.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.CriteriaFilter
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.ui.theme.Artemis_Green
import com.artemis.hunterexam.ui.theme.Artemis_Red
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import com.artemis.hunterexam.viewModel.QuestionViewModel

class TopicCatalogueComponent {

    @Composable
    fun TopicCatalogueScreen(
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 25.dp)
        ) {

            TopicCatalogueContent(
                questionViewModel = questionViewModel,
                navController = navController
            )
        }
    }

    @Composable
    fun TopicCatalogueContent(
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        val topicScreens = Screen.TOPIC_SCREENS
        var countFailedQuestions: Int
        var countLearnedQuestions: Int
        var countAllQuestionsByTopic: Int

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState, true)
        ) {
            topicScreens.filter { it.title != Constants.TRAINING }.forEach { topicScreen ->
                countFailedQuestions =
                    questionViewModel.selectTopic(
                        topicScreen.topic,
                        CriteriaFilter.ALL_QUESTIONS_SHUFFLED
                    ).count { it.failed == 1 }
                countLearnedQuestions =
                    questionViewModel.selectTopic(
                        topicScreen.topic,
                        CriteriaFilter.ALL_QUESTIONS_SHUFFLED
                    ).count { it.learnedTwice == 1 }
                countAllQuestionsByTopic =
                    questionViewModel.selectTopic(
                        topicScreen.topic,
                        CriteriaFilter.ALL_QUESTIONS_SHUFFLED
                    )
                        .count()
                Card(
                    shape = RoundedCornerShape(15.dp),
                    modifier =
                    Modifier
                        .padding(15.dp)
                        .size(400.dp, 35.dp)
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                        .clickable {
                            val questions = questionViewModel.selectTopic(
                                topicScreen.topic,
                                CriteriaFilter.ALL_QUESTIONS_SHUFFLED
                            )
                            navController.navigate(topicScreen.route)
                            questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                            questionViewModel.onChangeTopic(topicScreen.topic)
                            questionViewModel.onChangeQuestionList(questions)
                        }
                ) {
                    Row(
                        modifier = Modifier.background(Artemis_Yellow),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(0.8f),
                            text = "$countFailedQuestions Fehler",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.caption,
                            color = if (countFailedQuestions == 0) Artemis_Green else Artemis_Red
                        )
                        Text(
                            modifier = Modifier.weight(1.4f),
                            text = topicScreen.title,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                        )
                        Text(
                            modifier = Modifier.weight(0.8f),
                            text = "$countLearnedQuestions/$countAllQuestionsByTopic",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
        BackHandler(enabled = true) {
            navController.navigate(Screen.DrawerScreen.Home.route)
        }
    }
}