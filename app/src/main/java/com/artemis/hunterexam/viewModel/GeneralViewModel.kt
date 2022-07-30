package com.artemis.hunterexam.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artemis.hunterexam.data.configuration.ConfigurationRepository
import com.artemis.hunterexam.data.db.ArtemisDatabase
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.NavigationButton
import com.artemis.hunterexam.model.QuestionCheckbox
import com.artemis.hunterexam.model.QuestionProjection
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.model.Topic
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeneralViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentScreen = MutableLiveData<Screen.DrawerScreen>()
    val currentScreen: LiveData<Screen.DrawerScreen> = _currentScreen

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _questionFilter = MutableLiveData<Pair<Float, Boolean>>()
    val questionFilter: LiveData<Pair<Float, Boolean>> = _questionFilter

    private val _filterDialog = MutableLiveData(false)
    val filterDialog: LiveData<Boolean> = _filterDialog

    private val _openTrainingDialog = MutableLiveData(false)
    val openTrainingDialog: LiveData<Boolean> = _openTrainingDialog

    private val _closeTrainingScreen = MutableLiveData<Pair<Float, Boolean>>()
    val closeTrainingScreen: LiveData<Pair<Float, Boolean>> = _closeTrainingScreen

    private val _openAssignmentDialog = MutableLiveData(false)
    val openAssignmentDialog: LiveData<Boolean> = _openAssignmentDialog

    private val _closeAssignmentScreen = MutableLiveData<Pair<Float, Boolean>>()
    val closeAssignmentScreen: LiveData<Pair<Float, Boolean>> = _closeAssignmentScreen

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

    private val _isHintShow = MutableLiveData<Int>()
    val isHintShow: LiveData<Int> = _isHintShow

    private val _sizeOfTrainingUnit = MutableLiveData<Int>()
    val sizeOfTrainingUnit: LiveData<Int> = _sizeOfTrainingUnit

    private val _showAppBar = MutableLiveData<Boolean>()
    val showAppBar: LiveData<Boolean> = _showAppBar

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _answerBtnText = MutableLiveData("Antworten")
    val answerBtnText: LiveData<String> = _answerBtnText

    private val _currentQuestion = MutableLiveData<QuestionProjection>()
    val currentQuestion: LiveData<QuestionProjection> = _currentQuestion

    private val _favouriteColor = MutableLiveData<Int>()
    val favouriteColor: LiveData<Int> = _favouriteColor

    private val _checkedAnswers = mutableSetOf<String>()
    private val checkedAnswers: Set<String> = _checkedAnswers as HashSet<String>

    private val _topicWildlifeButtonColor = MutableLiveData<Color>()
    val topicWildlifeButtonColor: LiveData<Color> = _topicWildlifeButtonColor

    private val _topicHuntingOperations = MutableLiveData<Color>()
    val topicHuntingOperations: LiveData<Color> = _topicHuntingOperations

    private val _topicWildLifeTreatment = MutableLiveData<Color>()
    val topicWildLifeTreatment: LiveData<Color> = _topicWildLifeTreatment

    private val _topicWeaponsLawAndTechnology = MutableLiveData<Color>()
    val topicWeaponsLawAndTechnology: LiveData<Color> = _topicWeaponsLawAndTechnology

    private val _topicHuntingLaw = MutableLiveData<Color>()
    val topicHuntingLaw: LiveData<Color> = _topicHuntingLaw

    private val _topicPreservationOfWildLifeAndNature = MutableLiveData<Color>()
    val topicPreservationOfWildLifeAndNature: LiveData<Color> =
        _topicPreservationOfWildLifeAndNature

    private val configurationRepository: ConfigurationRepository

    init {
        val configurationDao = ArtemisDatabase.getDatabase(application).configurationDao()
        configurationRepository = ConfigurationRepository(configurationDao)
        CoroutineScope(Dispatchers.IO).launch {
            _isVibrating.postValue(configurationRepository.getVibrationConfig().toInt())
            _isHintShow.postValue(configurationRepository.getShowHintConfig().toInt())
            _sizeOfTrainingUnit.postValue(configurationRepository.getSizePerTrainingUnit().toInt())
        }
    }

    fun onChangeAppBarAppearance(showAppBar: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeAppBarAppearanceCoroutine(showAppBar)
        }
    }

    private suspend fun onChangeAppBarAppearanceCoroutine(showAppBar: Boolean) =
        withContext(Dispatchers.IO) {
            _showAppBar.postValue(showAppBar)
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
            Topic.TOPIC_7.ordinal -> currentScreen =
                Screen.DrawerScreen.AllQuestions
        }
        return currentScreen
    }

    fun onChangeCurrentScreen(screen: Screen.DrawerScreen) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCurrentScreenCoroutine(screen)
        }
    }

    private suspend fun onChangeCurrentScreenCoroutine(screen: Screen.DrawerScreen) =
        withContext(Dispatchers.IO) {
            _currentScreen.postValue(screen)
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

    fun onChangeIndex(newIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeIndexCoroutine(newIndex)
        }
        Log.v("Current index", (newIndex + 1).toString())
    }

    private suspend fun onChangeIndexCoroutine(newIndex: Int) =
        withContext(Dispatchers.IO) {
            _index.postValue(newIndex)
        }

    fun onChangeCurrentQuestion(newQuestion: QuestionProjection) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCurrentQuestionCoroutine(newQuestion)
        }
    }

    fun onChangeCheckboxes(
        checkbox: QuestionCheckbox,
        currentQuestion: QuestionProjection,
        checkedState: MutableState<Boolean>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeCheckboxesCoroutine(checkbox, currentQuestion, checkedState)
        }
    }

    private suspend fun onChangeCheckboxesCoroutine(
        checkbox: QuestionCheckbox,
        currentQuestion: QuestionProjection,
        checkedState: MutableState<Boolean>
    ) = withContext(Dispatchers.IO) {
        checkbox.checked = !checkbox.checked
        when (checkbox.option) {
            Constants.TRAINING_SELECTION_A -> currentQuestion.checkboxA = checkbox.apply {
                checkedState.value = checkbox.checked
            }
            Constants.TRAINING_SELECTION_B -> currentQuestion.checkboxB = checkbox.apply {
                checkedState.value = checkbox.checked
            }
            Constants.TRAINING_SELECTION_C -> currentQuestion.checkboxC = checkbox.apply {
                checkedState.value = checkbox.checked
            }
            Constants.TRAINING_SELECTION_D -> currentQuestion.checkboxD = checkbox.apply {
                checkedState.value = checkbox.checked
            }
        }
        _currentQuestion.postValue(currentQuestion)
        onChangeCurrentQuestion(currentQuestion)
    }

    private suspend fun onChangeCurrentQuestionCoroutine(newQuestion: QuestionProjection) =
        withContext(Dispatchers.IO) {
            restoreSelection(newQuestion)
            _currentQuestion.postValue(newQuestion)
        }


    fun onChangeAnswerButtonText(answerBtnText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeAnswerButtonTextCoroutine(answerBtnText)
        }
    }

    private suspend fun onChangeAnswerButtonTextCoroutine(answerBtnText: String) =
        withContext(Dispatchers.IO) {
            _answerBtnText.postValue(answerBtnText)
        }

    fun onChangeEnableNavButtons(enabled: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeEnableNavButtonsCoroutine(enabled)
        }
    }

    private suspend fun onChangeEnableNavButtonsCoroutine(enabled: Boolean) =
        withContext(Dispatchers.IO) {
            _isButtonEnabled.postValue(enabled)
        }

    @Suppress("JavaCollectionsStaticMethodOnImmutableList")
    private fun restoreSelection(currentQuestion: QuestionProjection) {
        _checkedAnswers.clear()
        for (cb in currentQuestion.checkboxList) {
            if (cb.checked) {
                _checkedAnswers.add(cb.option)
            }
        }
        checkedAnswers.toSortedSet().toString()
        currentQuestion.currentSelection = _checkedAnswers.toString()
    }

    fun onChangeFavouriteState(isFavourite: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeFavouriteStateCoroutine(isFavourite)
        }
    }

    private suspend fun onChangeFavouriteStateCoroutine(isFavourite: Int) =
        withContext(Dispatchers.IO) {
            _favouriteColor.postValue(isFavourite)
        }

    fun setTopicBoxButton(
        direction: NavigationButton,
        topic: Int,
        questions: List<QuestionProjection>
    ) {
        var index = 0
        when (topic) {
            0 -> index = 0
            1 -> index = 21
            2 -> index = 41
            3 -> index = 61
            4 -> index = 81
            5 -> index = 101
        }
        setDirection(direction, questions, index)
    }

    private fun onChangeTopicButtonColor(newValue: Color, topic: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeTopicButtonColorCoroutine(newValue, topic)
        }
    }

    private suspend fun onChangeTopicButtonColorCoroutine(newValue: Color, topic: Int) =
        withContext(Dispatchers.IO) {
            when (topic) {
                Topic.TOPIC_1.ordinal -> _topicWildlifeButtonColor.postValue(newValue)
                Topic.TOPIC_2.ordinal -> _topicHuntingOperations.postValue(newValue)
                Topic.TOPIC_3.ordinal -> _topicWeaponsLawAndTechnology.postValue(newValue)
                Topic.TOPIC_4.ordinal -> _topicWildLifeTreatment.postValue(newValue)
                Topic.TOPIC_5.ordinal -> _topicHuntingLaw.postValue(newValue)
                Topic.TOPIC_6.ordinal -> _topicPreservationOfWildLifeAndNature.postValue(newValue)
            }
        }

    fun checkTopicButtonColors(
        assignmentQuestions: List<QuestionProjection>,
        currentQuestion: QuestionProjection
    ) {
        var count = 0
        when (currentQuestion.topic) {
            Topic.TOPIC_1.ordinal -> {
                count = assignmentQuestions.subList(0, 20).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_2.ordinal -> {
                count = assignmentQuestions.subList(20, 40).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_3.ordinal -> {
                count = assignmentQuestions.subList(40, 60).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_4.ordinal -> {
                count = assignmentQuestions.subList(60, 80).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_5.ordinal -> {
                count = assignmentQuestions.subList(80, 100).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
            Topic.TOPIC_6.ordinal -> {
                count = assignmentQuestions.subList(100, 120).count {
                    it.checkboxList.stream().anyMatch { cb -> cb.checked }
                }
            }
        }
        Log.v("Current count", count.toString() + " " + currentQuestion.text)
        if (count == 20) {
            onChangeTopicButtonColor(Artemis_Yellow, currentQuestion.topic)
        } else {
            onChangeTopicButtonColor(Color.White, currentQuestion.topic)
        }
    }

    private fun setDirection(
        direction: NavigationButton,
        questions: List<QuestionProjection>,
        index: Int
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.SKIP_TEN_BACKWARD -> skipTenBackward(index, questions)
            NavigationButton.SKIP_TEN_FORWARD -> skipTenForward(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
            NavigationButton.SKIPPED_INDEX -> skippedIndex(questions, index)
        }
    }

    private fun skippedIndex(questions: List<QuestionProjection>, skippedIndex: Int) {
        val index = if (skippedIndex == 0) 0 else skippedIndex - 1
        onChangeIndex(index)
        onChangeCurrentQuestion(questions[index])
        onChangeFavouriteState(questions[index].favourite)
    }

    //Navigation bar with buttons
    fun setNavigationButton(
        direction: NavigationButton,
        index: Int,
        questions: List<QuestionProjection>
    ) {
        when (direction) {
            NavigationButton.FIRST_PAGE -> firstPage(questions)
            NavigationButton.PREV_PAGE -> prevPage(index, questions)
            NavigationButton.SKIP_TEN_BACKWARD -> skipTenBackward(index, questions)
            NavigationButton.SKIP_TEN_FORWARD -> skipTenForward(index, questions)
            NavigationButton.NEXT_PAGE -> nextPage(index, questions)
            NavigationButton.LAST_PAGE -> lastPage(questions)
            else -> {}
        }
    }

    private fun firstPage(questions: List<QuestionProjection>) {
        onChangeIndex(0)
        onChangeCurrentQuestion(questions[0].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
        onChangeFavouriteState(questions[0].favourite)
    }

    private fun prevPage(index: Int, questions: List<QuestionProjection>) {
        if (index > 0) {
            onChangeIndex(index - 1)
            onChangeCurrentQuestion(questions[index - 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
            onChangeFavouriteState(questions[index - 1].favourite)
        }
    }

    private fun skipTenBackward(index: Int, questions: List<QuestionProjection>) {
        var offset = 10
        if ((index - offset) < 0) {
            offset = index
        }
        onChangeIndex(index - offset)
        onChangeCurrentQuestion(questions[index - offset].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
        onChangeFavouriteState(questions[index - offset].favourite)

    }

    private fun skipTenForward(index: Int, questions: List<QuestionProjection>) {
        var offset = 10
        if (index < questions.size - 10) {
            if (index == 0) {
                offset = 9
            }
            onChangeIndex(index + offset)
            onChangeCurrentQuestion(questions[index + offset].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
            onChangeFavouriteState(questions[index + offset].favourite)
        }
    }

    private fun nextPage(index: Int, questions: List<QuestionProjection>) {
        if (index < questions.size - 1) {
            onChangeIndex(index + 1)
            onChangeCurrentQuestion(questions[index + 1].apply {
                this.checkboxList = this.checkboxList.shuffled()
            })
            onChangeFavouriteState(questions[index + 1].favourite)
        }
    }

    private fun lastPage(questions: List<QuestionProjection>) {
        onChangeIndex(questions.size - 1)
        onChangeCurrentQuestion(questions[questions.size - 1].apply {
            this.checkboxList = this.checkboxList.shuffled()
        })
        onChangeFavouriteState(questions[questions.size - 1].favourite)
    }

    fun onChangeShowHints(isHintShow: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeShowHintsCoroutine(isHintShow)
        }
    }

    private suspend fun onChangeShowHintsCoroutine(isHintShow: Int) =
        withContext(Dispatchers.IO) {
            _isHintShow.postValue(isHintShow)
            configurationRepository.updateShowHints(isHintShow.toString())
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

    fun onCloseAssignmentScreen(visibility: Pair<Float, Boolean>) {
        CoroutineScope(Dispatchers.IO).launch {
            onCloseAssignmentScreenCoroutine(visibility)
        }
    }

    private suspend fun onCloseAssignmentScreenCoroutine(visibility: Pair<Float, Boolean>) =
        withContext(Dispatchers.IO) {
            _closeAssignmentScreen.postValue(visibility)
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

    fun onOpenAssignmentDialog(isOpen: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            onOpenAssignmentDialogCoroutine(isOpen)
        }
    }

    private suspend fun onOpenAssignmentDialogCoroutine(isOpen: Boolean) =
        withContext(Dispatchers.IO) {
            _openAssignmentDialog.postValue(isOpen)
        }

    fun onChangeSearchWidgetState(newValue: Boolean) {
        _searchWidgetState.value = newValue
    }

    fun onChangeSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }
}