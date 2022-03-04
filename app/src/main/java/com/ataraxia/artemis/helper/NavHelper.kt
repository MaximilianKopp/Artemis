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
import kotlinx.coroutines.CoroutineScope

class NavHelper {

    companion object {

        @Composable
        fun LoadNavigationRoutes(
            showStartScreenInfo: (Boolean) -> Unit,
            navController: NavHostController,
            paddingValues: PaddingValues,
            scope: CoroutineScope,
            isFilterDialogOpen: Boolean,
            onOpenFilterDialog: (Boolean) -> Unit,
            isTrainingDialogClosed: Boolean,
            onCloseTrainingDialog: (Boolean) -> Unit
        ) {
            val generalViewModel: GeneralViewModel = viewModel()
            val startMenuComponent = StartMenuComponent()
            val questionComponent = QuestionCatalogueComponent()
            val examComponent = ExamComponent()
            val statisticComponent = StatisticComponent()
            val configComponent = ConfigComponent()
            val questionListComponent = QuestionListComponent()
            val trainingComponent = TrainingComponent()
            val questionViewModel: QuestionViewModel = viewModel()

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
                            Questions.route -> questionComponent.QuestionScreen(
                                navController,
                                scope
                            ).apply { generalViewModel.onShowStartScreenInfo(false) }
                            Exam.route -> examComponent.ExamScreen()
                                .apply { generalViewModel.onShowStartScreenInfo(false) }
                            Statistics.route -> statisticComponent.StatisticScreen()
                                .apply { generalViewModel.onShowStartScreenInfo(false) }
                            Configuration.route -> configComponent.ConfigScreen()
                                .apply { generalViewModel.onShowStartScreenInfo(false) }
                        }
                        generalViewModel.onTopBarTitleChange(screen.title)
                        generalViewModel.onHideFilter(Constants.ALPHA_INVISIBLE)
                        generalViewModel.onCloseTrainingScreen(Constants.ALPHA_INVISIBLE)

                    }
                }
                for (screen in Screen.CHAPTER_SCREENS) {
                    composable(screen.route) {
                        screen.chapter?.let { chapter ->
                            val questionsByChapter: List<Question> =
                                questionViewModel.selectChapter(chapter)
                            val questions: List<Question> by questionViewModel.questions.observeAsState(
                                questionsByChapter
                            )
                            questionViewModel.onChangeQuestionList(questionsByChapter)
                            questionListComponent.ChapterScreen(
                                navController,
                                isFilterDialogOpen,
                                onOpenFilterDialog,
                                questionViewModel,
                                questionsByChapter,
                                questions
                            )
                        }
                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                        if (screen.route == Constants.TRAINING) {
                            trainingComponent.TrainingScreen(
                                navController,
                                questionViewModel,
                                isTrainingDialogClosed,
                                onCloseTrainingDialog
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