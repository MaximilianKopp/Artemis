package com.ataraxia.artemis.data.configuration

class ConfigurationRepository(
    private val configurationDao: ConfigurationDao
) {
    suspend fun getVibrationConfig(): String = configurationDao.getVibrationConfig()

    suspend fun getSizePerTrainingUnit(): String = configurationDao.getSizePerTrainingUnit()

    suspend fun updateVibrationConfig(value: String) = configurationDao.updateVibrationConfig(value)

    suspend fun getShowHintConfig(): String = configurationDao.getShowHintConfig()

    suspend fun updateShowHints(value: String) = configurationDao.updateShowHints(value)

    suspend fun updateSizeOfTrainingUnit(value: String) =
        configurationDao.updateSizePerTrainingUnit(value)
}