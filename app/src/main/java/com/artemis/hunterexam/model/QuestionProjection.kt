package com.artemis.hunterexam.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.artemis.hunterexam.helper.Constants

class QuestionProjection(
    var id: Int,
    var text: String,
    var optionA: String,
    var optionB: String,
    var optionC: String,
    var optionD: String,
    var correctAnswers: String,
    var topic: Int,
    var favourite: Int,
    var learnedOnce: Int,
    var learnedTwice: Int,
    var failed: Int,
    var lastViewed: String,
    var favouriteState: MutableState<Color>,

    var checkboxA: QuestionCheckbox = QuestionCheckbox(false, "a", Color.Black),
    var checkboxB: QuestionCheckbox = QuestionCheckbox(false, "b", Color.Black),
    var checkboxC: QuestionCheckbox = QuestionCheckbox(false, "c", Color.Black),
    var checkboxD: QuestionCheckbox = QuestionCheckbox(false, "d", Color.Black),
    var checkboxList: List<QuestionCheckbox> = listOf(
        checkboxA,
        checkboxB,
        checkboxC,
        checkboxD
    ).shuffled(),
    var currentSelection: String
) {
    companion object {
        fun entityToModel(questionEntity: Question): QuestionProjection {
            return QuestionProjection(
                questionEntity.id,
                questionEntity.text,
                questionEntity.optionA,
                questionEntity.optionB,
                questionEntity.optionC,
                questionEntity.optionD,
                questionEntity.correctAnswers,
                questionEntity.topic,
                questionEntity.favourite,
                questionEntity.learnedOnce,
                questionEntity.learnedTwice,
                questionEntity.failed,
                questionEntity.lastViewed,
                mutableStateOf(if (questionEntity.favourite == 1) Color.Yellow else Color.Black),
                QuestionCheckbox(false, "a", Color.Black),
                QuestionCheckbox(false, "b", Color.Black),
                QuestionCheckbox(false, "c", Color.Black),
                QuestionCheckbox(false, "d", Color.Black),
                listOf(
                    QuestionCheckbox(false, "a", Color.Black),
                    QuestionCheckbox(false, "b", Color.Black),
                    QuestionCheckbox(false, "c", Color.Black),
                    QuestionCheckbox(false, "d", Color.Black)
                ).shuffled(),
                currentSelection = Constants.EMPTY_STRING
            )
        }

        fun modelToEntity(question: QuestionProjection): Question {
            return Question(
                question.id,
                question.text,
                question.optionA,
                question.optionB,
                question.optionC,
                question.optionD,
                question.correctAnswers,
                question.topic,
                question.favourite,
                question.learnedOnce,
                question.learnedTwice,
                question.failed,
                question.lastViewed
            )
        }
    }
}