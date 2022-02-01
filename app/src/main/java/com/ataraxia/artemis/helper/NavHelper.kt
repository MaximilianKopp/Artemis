package com.ataraxia.artemis.helper

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            scope: CoroutineScope
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
                    startMenuComposition.StartMenu(navController, scope).apply { onTitleChange(Home.title) }
                }
                composable(Questions.route) {
                    questionComposition.QuestionScreen(navController, scope).apply { onTitleChange(Questions.title) }
                }
                composable(Exam.route) {
                    examComposition.ExamScreen().apply { onTitleChange(Exam.title) }
                }
                composable(Statistics.route) {
                    statisticComposition.StatisticScreen().apply { onTitleChange(Statistics.title) }
                }
                composable(Configuration.route) {
                    configComposition.ConfigScreen().apply { onTitleChange(Configuration.title) }
                }
                composable(TopicWildLife.route) {
                    questionListComposition.LoadTopicList(Constants.CHAPTER_1).apply { onTitleChange(TopicWildLife.title) }
                }
            }
        }
    }
}