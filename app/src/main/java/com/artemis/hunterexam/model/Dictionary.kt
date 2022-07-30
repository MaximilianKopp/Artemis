package com.artemis.hunterexam.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Dictionary(
    @PrimaryKey
    val id: Int,
    var item: String,
    var definition: String,
    var url: String
)
