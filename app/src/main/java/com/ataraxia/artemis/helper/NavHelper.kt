package com.ataraxia.artemis.helper

import AppBarViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.model.Screen.DrawerScreen.*
import com.ataraxia.artemis.ui.*
import kotlinx.coroutines.CoroutineScope

class NavHelper {

    companion object {

        @Composable
        fun LoadNavigationRoutes(
            navController: NavHostController,
            paddingValues: PaddingValues,
            onTitleChange: (String) -> Unit,
            onHideFilter: (Float) -> Unit,
            scope: CoroutineScope,
            questionViewModel: QuestionViewModel,
            topBarViewModel: AppBarViewModel = viewModel()
        ) {
            val startMenuComposition = StartMenuComposition()
            val questionComposition = QuestionCatalogueComposition()
            val examComposition = ExamComposition()
            val statisticComposition = StatisticComposition()
            val configComposition = ConfigComposition()
            val questionListComposition = QuestionListComposition()

            NavHost(
                navController,
                Home.route,
                Modifier.padding(paddingValues = paddingValues)
            ) {
                composable(Home.route) {
                    startMenuComposition.StartMenu(navController)
                        .apply {
                            onTitleChange(Home.title)
                            onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                        }
                }

                composable(Questions.route) {
                    questionComposition.QuestionScreen(navController, scope)
                        .apply {
                            onTitleChange(Questions.title)
                            onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                        }
                }
                composable(Exam.route) {
                    examComposition.ExamScreen().apply {
                        onTitleChange(Exam.title)
                        onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                    }
                }

                composable(Statistics.route) {
                    statisticComposition.StatisticScreen().apply {
                        onTitleChange(Statistics.title)
                        onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                    }
                }

                composable(Configuration.route) {
                    configComposition.ConfigScreen().apply {
                        onTitleChange(Configuration.title)
                        onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                    }
                }

                composable(TopicWildLife.route) {
                    questionListComposition.LoadChapterList(
                        Constants.CHAPTER_1,
                        questionViewModel,
                        topBarViewModel
                    ).apply {
                        onTitleChange(TopicWildLife.title)
                        onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }

                composable(TopicHuntingOperations.route) {
                    questionListComposition.LoadChapterList(
                        Constants.CHAPTER_2,
                        questionViewModel,
                        topBarViewModel
                    ).apply {
                        onTitleChange(TopicHuntingOperations.title)
                        onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }

                composable(TopicWeaponsLawAndTechnology.route) {
                    questionListComposition.LoadChapterList(
                        Constants.CHAPTER_3,
                        questionViewModel,
                        topBarViewModel
                    ).apply {
                        onTitleChange(TopicWeaponsLawAndTechnology.title)
                        onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }

                composable(TopicWildLifeTreatment.route) {
                    questionListComposition.LoadChapterList(
                        Constants.CHAPTER_4,
                        questionViewModel,
                        topBarViewModel
                    ).apply {
                        onTitleChange(TopicWildLifeTreatment.title)
                        onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }

                composable(TopicHuntingLaw.route) {
                    questionListComposition.LoadChapterList(
                        Constants.CHAPTER_5,
                        questionViewModel,
                        topBarViewModel
                    ).apply {
                        onTitleChange(TopicHuntingLaw.title)
                        onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }

                composable(TopicPreservationOfWildLifeAndNature.route) {
                    questionListComposition.LoadChapterList(
                        Constants.CHAPTER_6,
                        questionViewModel,
                        topBarViewModel
                    ).apply {
                        onTitleChange(TopicPreservationOfWildLifeAndNature.title)
                        onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }
            }
        }
    }
}