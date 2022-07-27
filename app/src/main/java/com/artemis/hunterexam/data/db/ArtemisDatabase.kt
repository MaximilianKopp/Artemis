package com.artemis.hunterexam.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artemis.hunterexam.data.configuration.ConfigurationDao
import com.artemis.hunterexam.data.dictionary.DictionaryDao
import com.artemis.hunterexam.data.questions.QuestionDao
import com.artemis.hunterexam.model.Configuration
import com.artemis.hunterexam.model.Dictionary
import com.artemis.hunterexam.model.Question

@Database(
    entities = [Question::class, Dictionary::class, Configuration::class],
    version = 1,
    exportSchema = false
)
abstract class ArtemisDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun configurationDao(): ConfigurationDao
    abstract fun dictionaryDao(): DictionaryDao

    companion object {
        @Volatile
        private var INSTANCE: ArtemisDatabase? = null

        fun getDatabase(context: Context): ArtemisDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArtemisDatabase::class.java,
                    "artemisRlp_2022_05_15.db"
                ).createFromAsset("database/artemisRlp_2022_05_15.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}