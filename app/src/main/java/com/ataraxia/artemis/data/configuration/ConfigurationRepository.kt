package com.ataraxia.artemis.data.configuration

class ConfigurationRepository(
    private val configurationDao: ConfigurationDao
) {
    suspend fun getVibrationConfig(): String = configurationDao.getVibrationConfig()

    suspend fun getSizePerTrainingUnit(): String = configurationDao.getSizePerTrainingUnit()

    suspend fun updateVibrationConfig(value: String) = configurationDao.updateVibrationConfig(value)

    suspend fun updateSizeOfTrainingUnit(value: String) =
        configurationDao.updateSizePerTrainingUnit(value)
}