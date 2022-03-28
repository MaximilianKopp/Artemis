package com.ataraxia.artemis.data.configuration

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ConfigurationDao {

    @Query("SELECT value FROM configuration WHERE name = 'vibration'")
    suspend fun getVibrationConfig(): String

    @Query("SELECT value FROM configuration WHERE name = 'sizeOfTrainingUnit'")
    suspend fun getSizePerTrainingUnit(): String

    @Query("UPDATE configuration set value =:value WHERE name = 'vibration'")
    suspend fun updateVibrationConfig(value: String)

    @Query("UPDATE configuration set value =:value WHERE name = 'sizeOfTrainingUnit'")
    suspend fun updateSizePerTrainingUnit(value: String)
}