package com.ataraxia.artemis.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistics")
data class Statistic(
    @PrimaryKey
    val id: Int,
    val topic: Int,
    val name: String,
    val value: Int,
    val description: String
)