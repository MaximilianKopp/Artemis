package com.ataraxia.artemis.helper

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ataraxia.artemis.data.GeneralViewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.model.Question
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.model.Screen.DrawerScreen.*
import com.ataraxia.artemis.ui.*

class NavHelper {

    companion object {
        lateinit var trainingData: List<Question>

        @Composable
        fun LoadNavigationRoutes(
            navController: NavHostController,
            paddingValues: PaddingValues,
            isFilterDialogOpen: Boolean,
            onOpenFilterDialog: (Boolean) -> Unit,
            isTrainingDialogClosed: Boolean,
            onCloseTrainingDialog: (Boolean) -> Unit
        ) {
            val generalViewModel: GeneralViewModel = viewModel()
            val questionViewModel: QuestionViewModel = viewModel()
            val startMenuComponent = StartMenuComponent()
            val questionComponent = QuestionCatalogueComponent()
            val examComponent = ExamComponent()
            val statisticComponent = StatisticComponent()
            val configComponent = ConfigComponent()
            val questionListComponent = QuestionListComponent()
            val trainingComponent = TrainingComponent()

            NavHost(
                navController,
                Home.route,
                Modifier.padding(paddingValues = paddingValues)
            ) {
                for (screen in Screen.GENERAL_SCREENS) {
                    composable(screen.route) {
                        when (screen.route) {
                            Home.route -> startMenuComponent.StartMenu(navController = navController)
                                .apply { generalViewModel.onShowStartScreenInfo(true) }
                            Questions.route -> questionComponent.TopicCatalogueScreen(
                                questionViewModel,
                                navController
                            ).apply { generalViewModel.onShowStartScreenInfo(false) }
                            Exam.route -> examComponent.ExamScreen()
                                .apply { generalViewModel.onShowStartScreenInfo(false) }
                            Statistics.route -> statisticComponent.StatisticScreen(questionViewModel = questionViewModel)
                                .apply { generalViewModel.onShowStartScreenInfo(false) }
                            Configuration.route -> configComponent.ConfigScreen()
                                .apply { generalViewModel.onShowStartScreenInfo(false) }
                        }
                        generalViewModel.onTopBarTitleChange(screen.title)
                        generalViewModel.onHideFilter(Constants.ALPHA_INVISIBLE)
                        generalViewModel.onCloseTrainingScreen(Constants.ALPHA_INVISIBLE)
                    }
                }
                Screen.TOPIC_SCREENS.forEach { screen ->
                    composable(screen.route) {
                        val filter: CriteriaFilter by questionViewModel.filter.observeAsState(
                            CriteriaFilter.ALL_QUESTIONS
                        )
                        if (screen.route != Training.route && filter != CriteriaFilter.SINGLE_QUESTION) {
                            screen.topic.let { topic ->
                                questionViewModel.onChangeChapter(topic)
                                trainingData =
                                    questionViewModel.selectTopic(topic, filter)
                                questionViewModel.onChangeQuestionList(trainingData)
                                questionListComponent.ChapterScreen(
                                    navController,
                                    isFilterDialogOpen,
                                    onOpenFilterDialog,
                                    questionViewModel,
                                    trainingData
                                )
                            }
                        }
                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                        if (screen.route == Training.route) {

                            if (filter == CriteriaFilter.SINGLE_QUESTION) {
                                val currentQuestion: Question by questionViewModel.currentQuestion.observeAsState(
                                    trainingData[0]
                                )
                                trainingData = questionViewModel.loadSingleQuestion(currentQuestion)
                                questionViewModel.onChangeQuestionList(trainingData)
                            }
                            trainingComponent.TrainingScreen(
                                navController,
                                questionViewModel,
                                filter,
                                isTrainingDialogClosed,
                                onCloseTrainingDialog,
                                trainingData
                            )
                            generalViewModel.onHideFilter(Constants.ALPHA_INVISIBLE)
                            generalViewModel.onCloseTrainingScreen(Constants.ALPHA_VISIBLE)
                        }
                        generalViewModel.onTopBarTitleChange(screen.title)
                    }
                }
            }
        }
    }
}