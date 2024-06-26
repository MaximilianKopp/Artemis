package com.artemis.hunterexam.viewModel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artemis.hunterexam.data.db.ArtemisDatabase
import com.artemis.hunterexam.data.dictionary.DictionaryRepository
import com.artemis.hunterexam.data.questions.QuestionRepository
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.CriteriaFilter
import com.artemis.hunterexam.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    private val _questions = MutableLiveData<List<QuestionProjection>>()
    val questions: LiveData<List<QuestionProjection>> = _questions

    private val _questionsForAssignment = MutableLiveData<List<QuestionProjection>>()
    val questionsForAssignment: LiveData<List<QuestionProjection>> = _questionsForAssignment

    private val _currentTopic = MutableLiveData<Int>()
    val currentTopic: LiveData<Int> = _currentTopic

    private val _filter = MutableLiveData<CriteriaFilter>()
    val filter: LiveData<CriteriaFilter> = _filter

    private lateinit var questionRepository: QuestionRepository
    private lateinit var dictionaryRepository: DictionaryRepository

    lateinit var allQuestions: List<QuestionProjection>
    private lateinit var allDictionaryEntries: List<Dictionary>

    private var onceLearnedQuestions: Int = 0
    private var learnedQuestions: Int = 0
    private var failedQuestions: Int = 0
    private var progressInPercent: BigDecimal = BigDecimal.ZERO

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val questionDao =
                ArtemisDatabase.getDatabase(application.applicationContext).questionDao()
            questionRepository = QuestionRepository(questionDao)

            val dictionaryDao =
                ArtemisDatabase.getDatabase(application.applicationContext).dictionaryDao()
            dictionaryRepository = DictionaryRepository(dictionaryDao)

            allQuestions =
                questionRepository.getAllQuestions().map { QuestionProjection.entityToModel(it) }
            onceLearnedQuestions = allQuestions.count { it.learnedTwice == 1 }
            learnedQuestions = allQuestions.count { it.learnedTwice == 1 }
            failedQuestions = allQuestions.count { it.failed == 1 }
            progressInPercent = if (allQuestions.isNotEmpty()) {
                calculatePercentagePerTopic(learnedQuestions, allQuestions.count())
            } else {
                BigDecimal.ZERO
            }
            allDictionaryEntries = dictionaryRepository.getAllDictionaryEntries()
        }

    }

    fun onChangeQuestionsForAssignment(questions: List<QuestionProjection>) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeQuestionsForAssignmentCoroutines(questions)
        }
    }

    private suspend fun onChangeQuestionsForAssignmentCoroutines(questions: List<QuestionProjection>) =
        withContext(Dispatchers.IO) {
            _questionsForAssignment.postValue(questions)
        }

    fun prepareQuestionsForAssignment(): List<QuestionProjection> {
        //Algorithm: Take 20 Questions from each chapter by random
        val chapter1 = allQuestions.filter { it.topic == 0 }.shuffled().take(20)
        val chapter2 = allQuestions.filter { it.topic == 1 }.shuffled().take(20)
        val chapter3 = allQuestions.filter { it.topic == 2 }.shuffled().take(20)
        val chapter4 = allQuestions.filter { it.topic == 3 }.shuffled().take(20)
        val chapter5 = allQuestions.filter { it.topic == 4 }.shuffled().take(20)
        val chapter6 = allQuestions.filter { it.topic == 5 }.shuffled().take(20)

        return (chapter1 + chapter2 + chapter3 + chapter4 + chapter5 + chapter6)
    }

    fun checkDictionary(matchedText: String): Dictionary {
        val dictionary =
            Dictionary(0, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING)
        var matched = false
        for (entry in allDictionaryEntries) {
            if (matchedText.contains(entry.item) && !matched) {
                dictionary.item = entry.item
                dictionary.definition = entry.definition
                dictionary.url = entry.url
                matched = true
            }
        }
        return dictionary
    }

    private fun calculatePercentagePerTopic(learnedQuestions: Int, allQuestions: Int): BigDecimal {
        val learnedQuestionsInPercent =
            BigDecimal(
                (learnedQuestions.toDouble() / allQuestions.toDouble()) * 100.0
            ).setScale(
                2,
                RoundingMode.HALF_UP
            )
        return learnedQuestionsInPercent.setScale(
            if (
                learnedQuestionsInPercent.compareTo(BigDecimal(100.0)) == 0 ||
                learnedQuestionsInPercent.compareTo(BigDecimal(0.0)) == 0
            )
                0 else 2,
            RoundingMode.HALF_UP
        )
    }

    fun onChangeTopic(newChapter: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeChapterCoroutine(newChapter)
        }
    }

    private suspend fun onChangeChapterCoroutine(newChapter: Int) = withContext(Dispatchers.IO) {
        _currentTopic.postValue(newChapter)
    }

    fun onChangeFilter(newCriteriaFilter: CriteriaFilter) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeFilterCoroutine(newCriteriaFilter)
        }
    }

    private suspend fun onChangeFilterCoroutine(newCriteriaFilter: CriteriaFilter) =
        withContext(Dispatchers.IO) {
            _filter.postValue(newCriteriaFilter)
        }

    fun onChangeQuestionList(newValue: List<QuestionProjection>) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeQuestionListCoroutine(newValue)
        }
    }

    private suspend fun onChangeQuestionListCoroutine(newValue: List<QuestionProjection>) =
        withContext(Dispatchers.IO) {
            _questions.postValue(newValue)
        }

    fun updateQuestion(question: Question) {
        CoroutineScope(Dispatchers.IO).launch {
            questionRepository.updateQuestion(question)
        }
    }

    fun selectTopic(topic: Int, criteriaFilter: CriteriaFilter): List<QuestionProjection> {
        var questions = listOf<QuestionProjection>()
        when (topic) {
            Topic.TOPIC_1.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions.filter { it.topic == Topic.TOPIC_1.ordinal })
            Topic.TOPIC_2.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions.filter { it.topic == Topic.TOPIC_2.ordinal })
            Topic.TOPIC_3.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions.filter { it.topic == Topic.TOPIC_3.ordinal })
            Topic.TOPIC_4.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions.filter { it.topic == Topic.TOPIC_4.ordinal })
            Topic.TOPIC_5.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions.filter { it.topic == Topic.TOPIC_5.ordinal })
            Topic.TOPIC_6.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions.filter { it.topic == Topic.TOPIC_6.ordinal })
            Topic.TOPIC_7.ordinal -> questions = filterQuestions(
                criteriaFilter,
                allQuestions
            )
        }
        return questions
    }

    private fun filterQuestions(
        criteriaFilter: CriteriaFilter,
        questions: List<QuestionProjection>
    ): List<QuestionProjection> {
        val filteredQuestions: List<QuestionProjection> = when (criteriaFilter) {
            CriteriaFilter.ALL_QUESTIONS_SHUFFLED -> questions
            CriteriaFilter.NOT_LEARNED -> questions.filter { it.learnedOnce == 1 }
            CriteriaFilter.FAILED -> questions.filter { it.failed == 1 }
            CriteriaFilter.FAVOURITES -> questions.filter { it.favourite == 1 }

            else -> {
                questions
            }
        }
        return filteredQuestions
    }

    fun getCurrentCriteriaFilter(currentCriteriaFilter: CriteriaFilter): String {
        val currentFilterAsString: String = when (currentCriteriaFilter) {
            CriteriaFilter.ALL_QUESTIONS_SHUFFLED -> Constants.SHUFFLED
            CriteriaFilter.ALL_QUESTIONS_CHRONOLOGICAL -> Constants.CHRONOLOGICAL
            CriteriaFilter.NOT_LEARNED -> Constants.NOT_LEARNED
            CriteriaFilter.ONCE_LEARNED -> Constants.ONCE_LEARNED
            CriteriaFilter.FAILED -> Constants.FAILED
            CriteriaFilter.FAVOURITES -> Constants.FAVOURITES
            CriteriaFilter.LAST_VIEWED -> Constants.LAST_VIEWED
            CriteriaFilter.CUSTOM_SEARCH -> Constants.CUSTOM_SEARCH
            else -> {
                Constants.NO_SELECTION
            }
        }
        return currentFilterAsString
    }

    fun getTopicOfQuestion(currentQuestionNumericTopic: Int): String {
        var topic = Constants.EMPTY_STRING
        when (currentQuestionNumericTopic) {
            0 -> topic = Screen.DrawerScreen.TopicWildLife.title
            1 -> topic = Screen.DrawerScreen.TopicHuntingOperations.title
            2 -> topic = Screen.DrawerScreen.TopicWeaponsLawAndTechnology.title
            3 -> topic = Screen.DrawerScreen.TopicWildLifeTreatment.title
            4 -> topic = Screen.DrawerScreen.TopicHuntingLaw.title
            5 -> topic = Screen.DrawerScreen.TopicPreservationOfWildLifeAndNature.title
        }
        return topic
    }

    fun extractStatisticsFromTopics(): List<StatisticProjection> {
        val topics = listOf(
            Pair(Screen.DrawerScreen.TopicWildLife, Topic.TOPIC_1),
            Pair(Screen.DrawerScreen.TopicHuntingOperations, Topic.TOPIC_2),
            Pair(Screen.DrawerScreen.TopicWeaponsLawAndTechnology, Topic.TOPIC_3),
            Pair(Screen.DrawerScreen.TopicWildLifeTreatment, Topic.TOPIC_4),
            Pair(Screen.DrawerScreen.TopicHuntingLaw, Topic.TOPIC_5),
            Pair(Screen.DrawerScreen.TopicPreservationOfWildLifeAndNature, Topic.TOPIC_6)
        )
        val statistics = mutableListOf<StatisticProjection>()
        topics.forEach { item ->
            var filteredQuestions = allQuestions
            filteredQuestions = filteredQuestions.filter { it.topic == item.second.ordinal }
            val allQuestions: Int = filteredQuestions.size
            val onceLearnedQuestions: Int =
                filteredQuestions.count { it.learnedOnce == 1 && it.learnedTwice == 0 }
            val learnedQuestions: Int =
                filteredQuestions.count { it.learnedOnce == 0 && it.learnedTwice == 1 }
            val failedQuestions: Int = filteredQuestions.count { it.failed == 1 }

            statistics.add(
                StatisticProjection(
                    item.first.title,
                    onceLearnedQuestions,
                    learnedQuestions,
                    failedQuestions,
                    calculatePercentagePerTopic(learnedQuestions, allQuestions)
                )
            )
        }
        return statistics
    }

    fun extractTotalStatistics(): Map<String, BigDecimal> {
        val statisticsByTopic: List<StatisticProjection> = extractStatisticsFromTopics()
        val onceLearnedQuestionsTotal =
            statisticsByTopic.sumOf { it.totalOnceLearned }.toBigDecimal()
                .setScale(0, RoundingMode.HALF_UP)
        val failedQuestionsTotal = statisticsByTopic.sumOf { it.totalFailed }.toBigDecimal()
            .setScale(0, RoundingMode.HALF_UP)
        val twiceLearnedQuestionsTotal = statisticsByTopic.sumOf { it.totalLearned }.toBigDecimal()
            .setScale(0, RoundingMode.HALF_UP)
        val totalPercentage =
            calculatePercentagePerTopic(twiceLearnedQuestionsTotal.toInt(), allQuestions.size)
        return hashMapOf(
            Constants.STATISTICS_ONCE_LEARNED_TOTAL to onceLearnedQuestionsTotal,
            Constants.STATISTICS_FAILED_TOTAL to failedQuestionsTotal,
            Constants.STATISTICS_TWICE_LEARNED_TOTAL to twiceLearnedQuestionsTotal,
            Constants.STATISTICS_TOTAL_PERCENTAGE to totalPercentage
        )
    }

    fun setQuestionStateColor(question: QuestionProjection): Color {
        var result = Color.Black
        if (question.learnedOnce == 1 && question.failed == 0) {
            result = Color.Yellow
        }
        if (question.learnedTwice == 1 && question.failed == 0) {
            result = Color.Green
        }
        return result
    }
}