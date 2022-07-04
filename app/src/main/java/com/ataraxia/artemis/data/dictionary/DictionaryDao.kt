package com.ataraxia.artemis.data.dictionary

import androidx.room.Dao
import androidx.room.Query
import com.ataraxia.artemis.model.Dictionary

@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary ORDER BY id")
    suspend fun getAllDictionaryEntries(): List<Dictionary>
}