package com.ataraxia.artemis.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "artemis_rlp")
data class Question @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val topic: String,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswers: String,
    var favourite: Int,
    var learnedOnce: Int,
    var learnedTwice: Int,
    var failed: Int,
    val lastseen: String,
    @Ignore
    var isSelected: Int = 1,
)