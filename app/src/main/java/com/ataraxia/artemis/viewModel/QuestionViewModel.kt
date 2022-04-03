package com.ataraxia.artemis.viewModel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ataraxia.artemis.data.db.ArtemisDatabase
import com.ataraxia.artemis.data.questions.QuestionRepository
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _questionsForAssignment = MutableLiveData<List<QuestionProjection>>()
    val questionsForAssignment: LiveData<List<QuestionProjection>> = _questionsForAssignment

    private val _currentTopic = MutableLiveData<Int>()
    val currentTopic: LiveData<Int> = _currentTopic

    private val _filter = MutableLiveData<CriteriaFilter>()
    val filter: LiveData<CriteriaFilter> = _filter

    private lateinit var questionRepository: QuestionRepository

    lateinit var allQuestions: List<Question>

    var onceLearnedQuestions: Int = 0
    var learnedQuestions: Int = 0
    var failedQuestions: Int = 0
    var progressInPercent: BigDecimal = BigDecimal.ZERO

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val questionDao =
                ArtemisDatabase.getDatabase(application.applicationContext).questionDao()
            questionRepository = QuestionRepository(questionDao)
            allQuestions = questionRepository.getAllQuestions()
            onceLearnedQuestions = allQuestions.filter { it.learnedTwice == 1 }.count()
            learnedQuestions = allQuestions.filter { it.learnedTwice == 1 }.count()
            failedQuestions = allQuestions.filter { it.failed == 1 }.count()
            progressInPercent = if (allQuestions.isNotEmpty()) {
                calculatePercentage(learnedQuestions, allQuestions.count())
            } else {
                BigDecimal.ZERO
            }
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

    fun prepareQuestionsForAssignment(): List<Question> {
        //Algorithm: Take 20 Questions from each chapter by random
        val chapter1 = allQuestions.filter { it.topic == 0 }.shuffled().take(20)
        val chapter2 = allQuestions.filter { it.topic == 1 }.shuffled().take(20)
        val chapter3 = allQuestions.filter { it.topic == 2 }.shuffled().take(20)
        val chapter4 = allQuestions.filter { it.topic == 3 }.shuffled().take(20)
        val chapter5 = allQuestions.filter { it.topic == 4 }.shuffled().take(20)
        val chapter6 = allQuestions.filter { it.topic == 5 }.shuffled().take(20)

        val assignmentQuestions: List<Question> =
            (chapter1 + chapter2 + chapter3 + chapter4 + chapter5 + chapter6)

        return removeIndexNumberFromQuestion(assignmentQuestions)
    }

    private fun removeIndexNumberFromQuestion(questions: List<Question>): List<Question> {
        for (question in questions) {
            val subString = question.text.substring(0, 4)
            when (')') {
                subString[1] -> {
                    question.text = question.text.removeRange(0, 3)
                }
                subString[2] -> {
                    question.text = question.text.removeRange(0, 4)
                }
                subString[3] -> {
                    question.text = question.text.removeRange(0, 5)
                }
            }
        }
        return questions
    }

    private fun calculatePercentage(learnedQuestions: Int, allQuestions: Int): BigDecimal {
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

    fun onChangeQuestionList(newValue: List<Question>) {
        CoroutineScope(Dispatchers.IO).launch {
            onChangeQuestionListCoroutine(newValue)
        }
    }

    private suspend fun onChangeQuestionListCoroutine(newValue: List<Question>) =
        withContext(Dispatchers.IO) {
            _questions.postValue(newValue)
        }

    fun updateQuestion(question: Question) {
        CoroutineScope(Dispatchers.IO).launch {
            questionRepository.updateQuestion(question)
        }
    }

    fun selectTopic(topic: Int, criteriaFilter: CriteriaFilter): List<Question> {
        var questions = listOf<Question>()
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
        }
        return questions
    }


    private fun filterQuestions(
        criteriaFilter: CriteriaFilter,
        questions: List<Question>
    ): List<Question> {
        val filteredQuestions: List<Question> = when (criteriaFilter) {
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

    fun prepareQuestionData(
        criteriaFilter: CriteriaFilter,
        questions: List<Question>,
        trainingSize: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            prepareQuestionDataCoroutine(criteriaFilter, questions, trainingSize)
        }
    }

    private suspend fun prepareQuestionDataCoroutine(
        criteriaFilter: CriteriaFilter,
        questions: List<Question>,
        trainingSize: Int
    ) = withContext(Dispatchers.IO) {
        val learnedOnceQuestions = questions.filter { it.learnedOnce == 1 }
        val learnedTwiceQuestions = questions.filter { it.learnedTwice == 1 }
        val failedQuestions = questions.filter { it.failed == 1 }
        val favourites = questions.filter { it.favourite == 1 }
        val remainingQuestions = questions.toMutableList()
        remainingQuestions.also {
            it.removeAll(learnedOnceQuestions)
            it.removeAll(learnedTwiceQuestions)
            it.removeAll(failedQuestions)
        }
        val trainingDataWithoutFilter = mutableListOf<Question>()
        trainingDataWithoutFilter.addAll(failedQuestions.take(8))
        trainingDataWithoutFilter.addAll(learnedOnceQuestions.take(5))
        trainingDataWithoutFilter.addAll(learnedTwiceQuestions.take(2))
        trainingDataWithoutFilter.addAll(remainingQuestions.take(trainingSize))

        when (criteriaFilter) {
            CriteriaFilter.ALL_QUESTIONS_SHUFFLED -> _questions.postValue(trainingDataWithoutFilter)
            CriteriaFilter.NOT_LEARNED -> _questions.postValue(learnedOnceQuestions)
            CriteriaFilter.FAILED -> _questions.postValue(failedQuestions)
            CriteriaFilter.FAVOURITES -> _questions.postValue(favourites)
            else -> {

            }
        }
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
            val onceLearnedQuestions: Int = filteredQuestions.filter { it.learnedOnce == 1 }.count()
            val learnedQuestions: Int = filteredQuestions.filter { it.learnedTwice == 1 }.count()
            val failedQuestions: Int = filteredQuestions.filter { it.failed == 1 }.count()

            statistics.add(
                StatisticProjection(
                    item.first.title,
                    allQuestions,
                    onceLearnedQuestions,
                    learnedQuestions,
                    failedQuestions,
                    calculatePercentage(learnedQuestions, allQuestions)
                )
            )
        }
        return statistics
    }

    fun setQuestionStateColor(question: Question): Color {
        var result = Color.Black
        if (question.learnedOnce == 1 && question.failed == 0) {
            result = Color.Yellow
        }
        if (question.learnedTwice == 1 && question.failed == 0) {
            result = Color.Green
        }
        return result
    }

    fun setFavourite(
        question: Question,
        isFavourite: MutableState<Int>,
        currentFilter: CriteriaFilter
    ) {
        if (question.favourite == 0) {
            question.favourite = 1
            isFavourite.value = question.favourite
        } else if (question.favourite == 1) {
            question.favourite = 0
            isFavourite.value = question.favourite
        }

        if (question.favourite == 1 && currentFilter == CriteriaFilter.FAVOURITES) {
            question.favourite = 0
            isFavourite.value = question.favourite
        }
        updateQuestion(question)
    }
}