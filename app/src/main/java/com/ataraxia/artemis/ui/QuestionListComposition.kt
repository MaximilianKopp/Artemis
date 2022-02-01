package com.ataraxia.artemis.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.model.QuestionViewModel

class QuestionListComposition() {
    lateinit var questionViewModel: QuestionViewModel

    @Composable
    fun LoadTopicList(topic: String) {
        questionViewModel = viewModel()
        LazyColumn() {
            items(questionViewModel.allQuestionsByTopic(topic = topic)) {
                q -> Text(text = q.text )
            }
        }
    }

    @Preview
    @Composable
    fun PreviewLoadTopicList() {
       Text(text = "Hallo")
    }
}