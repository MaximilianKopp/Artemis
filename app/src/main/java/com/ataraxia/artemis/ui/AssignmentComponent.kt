package com.ataraxia.artemis.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.viewModel.AssignmentViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel

class AssignmentComponent {

    @Composable
    fun AssignmentScreen(
        questionViewModel: QuestionViewModel,
        assignmentViewModel: AssignmentViewModel
    ) {
//        val assignmentQuestions = questionViewModel.prepareQuestionsForAssignment()
        val assignmentQuestions =
            questionViewModel.questionsForAssignment.observeAsState(questionViewModel.prepareQuestionsForAssignment())
        val indexOfCurrentQuestion: Int by assignmentViewModel.indexOfCurrentQuestion.observeAsState(
            0
        )
        AssignmentContent(
            assignmentQuestions = assignmentQuestions.value,
            indexOfCurrentQuestion = indexOfCurrentQuestion,
            assignmentViewModel = assignmentViewModel
        )
    }

    @Composable
    fun AssignmentContent(
        assignmentQuestions: List<Question>,
        indexOfCurrentQuestion: Int,
        assignmentViewModel: AssignmentViewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(1f, fill = true)
                    .verticalScroll(scrollState, true)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = assignmentQuestions[indexOfCurrentQuestion].text
                    )
                }
                Button(onClick = {
                    assignmentViewModel.onChangeIndexOfCurrentQuestion(
                        indexOfCurrentQuestion + 1
                    )
                }) {

                }

            }
        }
    }
}