package com.ataraxia.artemis.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hunter_exam")
data class Question(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val topic: String,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswers: String
)