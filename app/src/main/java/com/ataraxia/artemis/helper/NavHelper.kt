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
import com.ataraxia.artemis.data.AppBarViewModel
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
            navController: NavHostController,
            paddingValues: PaddingValues,
            scope: CoroutineScope,
            isDialogOpen: Boolean,
            onOpenDialog: (Boolean) -> Unit
        ) {
            val appBarViewModel: AppBarViewModel = viewModel()
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
                            Questions.route -> questionComponent.QuestionScreen(
                                navController,
                                scope
                            )
                            Exam.route -> examComponent.ExamScreen()
                            Statistics.route -> statisticComponent.StatisticScreen()
                            Configuration.route -> configComponent.ConfigScreen()
                        }
                        appBarViewModel.onTopBarTitleChange(screen.title)
                        appBarViewModel.onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
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
                            questionViewModel.onChangeQuestionList(questions)
                            questionListComponent.ChapterScreen(
                                navController,
                                isDialogOpen,
                                onOpenDialog,
                                questionViewModel,
                                questionsByChapter,
                                questions
                            )
                        }
                        appBarViewModel.onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                        if (screen.route == Constants.TRAINING) {
                            trainingComponent.TrainingScreen(navController, questionViewModel)
                            appBarViewModel.onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                        }
                        appBarViewModel.onTopBarTitleChange(screen.title)
                    }
                }
            }
        }
    }
}