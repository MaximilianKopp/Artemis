import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataraxia.artemis.helper.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppBarViewModel : ViewModel() {
    private val _title = MutableLiveData("Artemis-Jägerprüfung")
    val title: LiveData<String> = _title

    private val _filter = MutableLiveData(Constants.FILTER_ALPHA_INVISIBLE)
    val filter: LiveData<Float> = _filter

    private val _filterDialog = MutableLiveData(false)
    val filterDialog = _filterDialog

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
            _filter.postValue(visible)
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
}