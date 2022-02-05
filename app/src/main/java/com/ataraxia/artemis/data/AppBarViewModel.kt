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

    private val _filter = MutableLiveData(0F)
    val filter: LiveData<Float> = _filter

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
            onHideTopBarFilter(visible)
        }
    }

    private suspend fun onHideTopBarFilter(visible: Float) = withContext(Dispatchers.IO) {
        _filter.postValue(visible)
    }
}