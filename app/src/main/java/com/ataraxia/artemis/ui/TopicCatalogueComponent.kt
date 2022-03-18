package com.ataraxia.artemis.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.GREEN_ARTEMIS
import com.ataraxia.artemis.ui.theme.RED_ARTEMIS
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel

class TopicCatalogueComponent {

    @Composable
    fun TopicCatalogueScreen(
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 25.dp)
        ) {

            TopicCatalogueContent(
                generalViewModel = generalViewModel,
                questionViewModel = questionViewModel,
                navController = navController
            )
        }
    }

    @Composable
    fun TopicCatalogueContent(
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        navController: NavController
    ) {
        BackHandler(enabled = true) {

        }
        val topicScreens = Screen.TOPIC_SCREENS
        var countFailedQuestions: Int
        var countLearnedQuestions: Int
        var countAllQuestionsByTopic: Int

        topicScreens.filter { it.title != Constants.TRAINING }.forEach { topicScreen ->
            countFailedQuestions =
                questionViewModel.selectTopic(topicScreen.topic, CriteriaFilter.ALL_QUESTIONS)
                    .filter { it.failed == 1 }.count()
            countLearnedQuestions =
                questionViewModel.selectTopic(topicScreen.topic, CriteriaFilter.ALL_QUESTIONS)
                    .filter { it.learnedTwice == 1 }.count()
            countAllQuestionsByTopic =
                questionViewModel.selectTopic(topicScreen.topic, CriteriaFilter.ALL_QUESTIONS)
                    .count()
            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(15.dp)
                    .size(400.dp, 35.dp)
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
                    .clickable {
                        val questions = questionViewModel.selectTopic(
                            topicScreen.topic,
                            CriteriaFilter.ALL_QUESTIONS
                        )
                        navController.navigate(topicScreen.route)
                        questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS)
                        questionViewModel.onChangeQuestionList(questions)
//                        generalViewModel.onChangeCurrentScreen(topicScreen)
                    }
            ) {
                Row(
                    modifier = Modifier.background(YELLOW_ARTEMIS),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(0.8f),
                        text = "$countFailedQuestions Fehler",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                        color = if (countFailedQuestions == 0) GREEN_ARTEMIS else RED_ARTEMIS
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
}