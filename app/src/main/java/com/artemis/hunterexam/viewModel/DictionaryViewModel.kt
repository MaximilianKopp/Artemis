package com.artemis.hunterexam.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.artemis.hunterexam.data.db.ArtemisDatabase
import com.artemis.hunterexam.data.dictionary.DictionaryRepository
import com.artemis.hunterexam.model.Dictionary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var dictionaryItems: List<Dictionary>
    private lateinit var dictionaryRepository: DictionaryRepository

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val dictionaryDao = ArtemisDatabase.getDatabase(application).dictionaryDao()
            dictionaryRepository = DictionaryRepository(dictionaryDao)
            dictionaryItems = dictionaryRepository.getAllDictionaryEntries().sortedBy { it.item }
        }
    }
}