package com.ataraxia.artemis.viewModel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ataraxia.artemis.data.configuration.ConfigurationRepository
import com.ataraxia.artemis.data.db.ArtemisDatabase
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.model.Topic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeneralViewModel(application: Application) : AndroidViewModel(application) {
    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _questionFilter = MutableLiveData<Pair<Float, Boolean>>()
    val questionFilter: LiveData<Pair<Float, Boolean>> = _questionFilter

    private val _filterDialog = MutableLiveData(false)
    val filterDialog: LiveData<Boolean> = _filterDialog

    private val _openTrainingDialog = MutableLiveData(false)
    val closeTrainingDialog: LiveData<Boolean> = _openTrainingDialog

    private val _closeTrainingScreen = MutableLiveData<Pair<Float, Boolean>>()
    val closeTrainingScreen: LiveData<Pair<Float, Boolean>> = _closeTrainingScreen

    private val _searchWidget = MutableLiveData<Pair<Float, Boolean>>()
    val searchWidget: LiveData<Pair<Float, Boolean>> = _searchWidget

    private val _searchWidgetState: MutableState<Boolean> =
        mutableStateOf(false)
    val searchWidgetState: State<Boolean> = _searchWidgetState

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    private val _isVibrating = MutableLiveData<Int>()
    val isVibrating: LiveData<Int> = _isVibrating

    private val _sizeOfTrainingUnit = MutableLiveData<Int>()
    val sizeOfTrainingUnit: LiveData<Int> = _sizeOfTrainingUnit

    private val configurationRepository: ConfigurationRepository

    init {
        val configurationDao = ArtemisDatabase.getDatabase(application).configurationDao()
        configurationRepository = ConfigurationRepository(configurationDao)
        CoroutineScope(Dispatchers.IO).launch {
            _isVibrating.postValue(configurationRepository.getVibrationConfig().toInt())
            _sizeOfTrainingUnit.postValue(configurationRepository.getSizePerTrainingUnit().toInt())
        }
    }

    fun loadScreenByTopic(topic: Int): Screen.DrawerScreen {
        var currentScreen: Screen.DrawerScreen = Screen.DrawerScreen.TopicWildLife
        when (topic) {
            Topic.TOPIC_1.ordinal -> currentScreen = Screen.DrawerScreen.TopicWildLife
            Topic.TOPIC_2.ordinal -> currentScreen = Screen.DrawerScreen.TopicHuntingOperations
            Topic.TOPIC_3.ordinal -> currentScreen =
                Screen.DrawerScreen.TopicWeaponsLawAndTechnology
            Topic.TOPIC_4.ordinal -> currentScreen = Screen.DrawerScreen.TopicWildLifeTreatment
            Topic.TOPIC_5.ordinal -> currentScreen = Screen.DrawerScreen.TopicHuntingLaw
            Topic.TOPIC_6.ordinal -> currentScreen =
                Screen.DrawerScreen.TopicPreservationOfWildLifeAndNature
        }
        return currentScreen
    }

    fun onChangeSizeOfTrainingUnit(size: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeSizeOfTrainingUnitCoroutine(size)
        }
    }

    private suspend fun onChangeSizeOfTrainingUnitCoroutine(size: Int) =
        withContext(Dispatchers.IO) {
            _sizeOfTrainingUnit.postValue(size)
            configurationRepository.updateSizeOfTrainingUnit(size.toString())
        }

    fun onChangeEnableVibration(isVibrating: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeEnableVibrationCoroutine(isVibrating)
        }
    }

    private suspend fun onChangeEnableVibrationCoroutine(isVibrating: Int) =
        withContext(Dispatchers.IO) {
            _isVibrating.postValue(isVibrating)
            configurationRepository.updateVibrationConfig(isVibrating.toString())
        }

    fun onTopBarTitleChange(newTitle: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onTopBarTitleChangeCoroutine(newTitle)
        }
    }

    private suspend fun onTopBarTitleChangeCoroutine(newTitle: String) =
        withContext(Dispatchers.IO) {
            _title.postValue(newTitle)
        }

    fun onHideFilter(visibility: Pair<Float, Boolean>) {
        CoroutineScope(Dispatchers.IO).launch {
            onHideFilterCoroutine(visibility)
        }
    }

    private suspend fun onHideFilterCoroutine(visible: Pair<Float, Boolean>) =
        withContext(Dispatchers.IO) {
            _questionFilter.postValue(visible)
        }

    fun onHideSearchWidget(visibility: Pair<Float, Boolean>) {
        CoroutineScope(Dispatchers.IO).launch {
            onHideSearchWidgetCoroutine(visibility)
        }
    }

    private suspend fun onHideSearchWidgetCoroutine(visibility: Pair<Float, Boolean>) =
        withContext(Dispatchers.IO) {
            _searchWidget.postValue(visibility)
        }

    fun onCloseTrainingScreen(visibility: Pair<Float, Boolean>) {
        CoroutineScope(Dispatchers.IO).launch {
            onCloseTrainingScreenCoroutine(visibility)
        }
    }

    private suspend fun onCloseTrainingScreenCoroutine(visible: Pair<Float, Boolean>) =
        withContext(Dispatchers.IO) {
            _closeTrainingScreen.postValue(visible)
        }

    fun onOpenFilterDialog(isOpen: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onOpenFilterDialogCoroutine(isOpen)
        }
    }

    private suspend fun onOpenFilterDialogCoroutine(isOpen: Boolean) =
        withContext(Dispatchers.IO) {
            _filterDialog.postValue(isOpen)
        }

    fun onOpenTrainingDialog(isOpen: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onOpenTrainingDialogCoroutine(isOpen)
        }
    }

    private suspend fun onOpenTrainingDialogCoroutine(isOpen: Boolean) =
        withContext(Dispatchers.IO) {
            _openTrainingDialog.postValue(isOpen)
        }

    fun onChangeSearchWidgetState(newValue: Boolean) {
        _searchWidgetState.value = newValue
    }

    fun onChangeSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }
}