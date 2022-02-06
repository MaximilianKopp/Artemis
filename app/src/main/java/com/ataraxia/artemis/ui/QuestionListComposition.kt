package com.ataraxia.artemis.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.model.Question

class QuestionListComposition {

    @Composable
    fun LoadChapterList(
        chapter: String,
    ) {
        val vm: QuestionViewModel = viewModel()
        val questions: List<Question> by vm.questions.observeAsState(listOf())
        vm.loadQuestions(chapter)

        LazyColumn {
            items(questions) { question ->
                val isFavourite = rememberSaveable { mutableStateOf(question.favourite) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        content = { QuestionCard(question, isFavourite) }
                    )
                }
            }
        }
    }

    @Composable
    fun QuestionCard(
        question: Question,
        isFavourite: MutableState<Int>,
    ) {
        val vm: QuestionViewModel = viewModel()
        val iconColor by animateColorAsState(
            if (isFavourite.value == 1) Color.Yellow else Color.Black
        )
        val checkedState = rememberSaveable { mutableStateOf(true) }
        Column {
            Row {
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { onCheckedChange(checkedState) })
                Text(
                    text = question.text,
                    Modifier.padding(start = 3.dp)
                )
            }
            Row {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {

                    Box(modifier = Modifier.padding(end = 30.dp)) {
                        Row {
                            Icon(Icons.Filled.Check, contentDescription = "", Modifier.size(20.dp))
                            Icon(Icons.Filled.Close, contentDescription = "", Modifier.size(20.dp))
                        }
                    }
                    IconButton(onClick = {
                        setFavourite(question, isFavourite) { vm.updateQuestion(question) }
                    }, Modifier.size(20.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Test", tint = iconColor
                        )
                    }
                }
            }
        }
    }

    private fun onCheckedChange(checkedState: MutableState<Boolean>) {
        checkedState.value = !checkedState.value
    }

    private fun setFavourite(
        question: Question,
        isFavourite: MutableState<Int>,
        updateQuestion: (Question) -> Unit
    ) {
        if (question.favourite == 0) {
            question.favourite = 1
            isFavourite.value = question.favourite
        } else if (question.favourite == 1) {
            question.favourite = 0
            isFavourite.value = question.favourite
        }
        updateQuestion(question)
    }

}