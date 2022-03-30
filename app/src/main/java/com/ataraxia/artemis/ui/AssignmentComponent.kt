package com.ataraxia.artemis.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.viewModel.QuestionViewModel

class AssignmentComponent {

    @Composable
    fun AssignmentScreen(
        questionViewModel: QuestionViewModel
    ) {
        val assignmentQuestions = questionViewModel.prepareQuestionsForAssignment()
        AssignmentContent(assignmentQuestions = assignmentQuestions)
    }

    @Composable
    fun AssignmentContent(
        assignmentQuestions: List<Question>
    ) {
        LazyColumn {
            items(assignmentQuestions.shuffled()) { question ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                ) {
                    Text(text = question.text)
                }
            }
        }
    }

}