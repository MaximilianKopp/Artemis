package com.artemis.hunterexam.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artemis.hunterexam.helper.Constants

@Entity(tableName = Constants.TABLE_NAME_QUESTIONS)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswers: String,
    val topic: Int,
    var favourite: Int,
    var learnedOnce: Int,
    var learnedTwice: Int,
    var failed: Int,
    var lastViewed: String
)