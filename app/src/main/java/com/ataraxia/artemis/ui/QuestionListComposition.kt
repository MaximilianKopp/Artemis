package com.ataraxia.artemis.ui

import AppBarViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Question

class QuestionListComposition {

    @Composable
    fun LoadChapterList(
        chapter: String,
        questionViewModel: QuestionViewModel,
        topBarViewModel: AppBarViewModel = viewModel()
    ) {
        val questions = loadChapter(chapter, questionViewModel)
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
                        content = { QuestionCard(question, isFavourite, questionViewModel) }
                    )
                }
            }
        }
    }


    @Composable
    fun QuestionCard(
        question: Question,
        isFavourite: MutableState<Int>,
        questionViewModel: QuestionViewModel
    ) {
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
                        setFavourite(question, isFavourite, questionViewModel)
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
        questionViewModel: QuestionViewModel
    ) {
        if (question.favourite == 0) {
            question.favourite = 1
            isFavourite.value = question.favourite
        } else if (question.favourite == 1) {
            question.favourite = 0
            isFavourite.value = question.favourite
        }
        questionViewModel.updateQuestion(question)
    }

    private fun loadChapter(
        chapter: String,
        questionViewModel: QuestionViewModel,
    ): MutableList<Question> {
        var loadedChapter = mutableStateListOf<Question>()

        when (chapter) {
            Constants.CHAPTER_1 -> loadedChapter =
                questionViewModel.allQuestionsFromChapterOne.toMutableStateList()
            Constants.CHAPTER_2 -> loadedChapter =
                questionViewModel.allQuestionsFromChapterTwo.toMutableStateList()
            Constants.CHAPTER_3 -> loadedChapter =
                questionViewModel.allQuestionsFromChapterThree.toMutableStateList()
            Constants.CHAPTER_4 -> loadedChapter =
                questionViewModel.allQuestionsFromChapterFour.toMutableStateList()
            Constants.CHAPTER_5 -> loadedChapter =
                questionViewModel.allQuestionsFromChapterFive.toMutableStateList()
            Constants.CHAPTER_6 -> loadedChapter =
                questionViewModel.allQuestionsFromChapterSix.toMutableStateList()
        }
        return loadedChapter
    }
}