package com.ataraxia.artemis.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataraxia.artemis.data.questions.QuestionDao
import com.ataraxia.artemis.data.statistics.StatisticDao
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.model.Statistic

@Database(entities = [Question::class, Statistic::class], version = 1, exportSchema = false)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun statisticDao(): StatisticDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionDatabase? = null

        fun getDatabase(context: Context): QuestionDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, QuestionDatabase::class.java, "artemisRlp.db"
                ).createFromAsset("database/artemisRlp.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}