package com.ataraxia.artemis.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppBarViewModel : ViewModel() {
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
}