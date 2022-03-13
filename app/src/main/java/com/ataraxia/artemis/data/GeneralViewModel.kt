package com.ataraxia.artemis.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.model.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeneralViewModel : ViewModel() {
    private val _title = MutableLiveData("Artemis-Jägerprüfung")
    val title: LiveData<String> = _title

    private val _questionFilter = MutableLiveData<Float>()
    val questionFilter: LiveData<Float> = _questionFilter

    private val _filterDialog = MutableLiveData(false)
    val filterDialog: LiveData<Boolean> = _filterDialog

    private val _openTrainingDialog = MutableLiveData(false)
    val closeTrainingDialog: LiveData<Boolean> = _openTrainingDialog

    private val _closeTrainingScreen = MutableLiveData<Float>()
    val closeTrainingScreen: LiveData<Float> = _closeTrainingScreen

    private val _showStartScreenInfo = MutableLiveData(true)
    val showStartScreenInfo: LiveData<Boolean> = _showStartScreenInfo

    private val _currentScreen = MutableLiveData<Screen.DrawerScreen>()
    val currentScreen: LiveData<Screen.DrawerScreen> = _currentScreen

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

    fun onChangeCurrentScreen(currentScreen: Screen.DrawerScreen) {
        viewModelScope.launch {
            onChangeCurrentScreenCoroutine(currentScreen)
        }
    }

    private suspend fun onChangeCurrentScreenCoroutine(currentScreen: Screen.DrawerScreen) =
        withContext(Dispatchers.IO) {
            _currentScreen.postValue(currentScreen)
        }

    fun onTopBarTitleChange(newTitle: String) {
        viewModelScope.launch {
            onTopBarTitleChangeCoroutine(newTitle)
        }
    }

    private suspend fun onTopBarTitleChangeCoroutine(newTitle: String) =
        withContext(Dispatchers.IO) {
            _title.postValue(newTitle)
        }

    fun onHideFilter(visible: Float) {
        viewModelScope.launch {
            onHideFilterCoroutine(visible)
        }
    }

    private suspend fun onHideFilterCoroutine(visible: Float) =
        withContext(Dispatchers.IO) {
            _questionFilter.postValue(visible)
        }

    fun onCloseTrainingScreen(visible: Float) {
        viewModelScope.launch {
            onCloseTrainingScreenCoroutine(visible)
        }
    }

    private suspend fun onCloseTrainingScreenCoroutine(visible: Float) =
        withContext(Dispatchers.IO) {
            _closeTrainingScreen.postValue(visible)
        }

    fun onOpenFilterDialog(isOpen: Boolean) {
        viewModelScope.launch {
            onOpenFilterDialogCoroutine(isOpen)
        }
    }

    private suspend fun onOpenFilterDialogCoroutine(isOpen: Boolean) =
        withContext(Dispatchers.IO) {
            _filterDialog.postValue(isOpen)
        }

    fun onOpenTrainingDialog(isOpen: Boolean) {
        viewModelScope.launch {
            onOpenTrainingDialogCoroutine(isOpen)
        }
    }

    private suspend fun onOpenTrainingDialogCoroutine(isOpen: Boolean) =
        withContext(Dispatchers.IO) {
            _openTrainingDialog.postValue(isOpen)
        }

    fun onShowStartScreenInfo(showStartInfo: Boolean) {
        viewModelScope.launch {
            onShowStartScreenInfoCoroutine(showStartInfo)
        }
    }

    private suspend fun onShowStartScreenInfoCoroutine(showStartInfo: Boolean) =
        withContext(Dispatchers.IO) {
            _showStartScreenInfo.postValue(showStartInfo)
        }
}