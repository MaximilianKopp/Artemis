package com.artemis.hunterexam.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuration")
data class Configuration(
    @PrimaryKey
    val id: Int,
    val name: String,
    val value: String,
    val description: String
)