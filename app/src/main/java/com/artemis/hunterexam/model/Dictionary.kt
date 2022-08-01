package com.artemis.hunterexam.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artemis.hunterexam.helper.Constants

@Entity(tableName = Constants.TABLE_NAME_DICTIONARY)
data class Dictionary(
    @PrimaryKey
    val id: Int,
    var item: String,
    var definition: String,
    var url: String
)
