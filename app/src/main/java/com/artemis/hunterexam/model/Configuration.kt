package com.artemis.hunterexam.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artemis.hunterexam.helper.Constants

@Entity(tableName = Constants.TABLE_NAME_CONFIGURATION)
data class Configuration(
    @PrimaryKey
    val id: Int,
    val name: String,
    val value: String,
    val description: String
)