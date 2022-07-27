package com.artemis.hunterexam.data.dictionary

class DictionaryRepository(
    private val dictionaryDao: DictionaryDao
) {
    suspend fun getAllDictionaryEntries() = dictionaryDao.getAllDictionaryEntries()
}