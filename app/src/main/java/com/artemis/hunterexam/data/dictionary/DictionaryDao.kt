package com.artemis.hunterexam.data.dictionary

import androidx.room.Dao
import androidx.room.Query
import com.artemis.hunterexam.model.Dictionary

@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary ORDER BY id")
    suspend fun getAllDictionaryEntries(): List<Dictionary>
}