package com.ataraxia.artemis.helper

import com.ataraxia.artemis.data.AppBarViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            val topBarViewModel: AppBarViewModel = viewModel()
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

                for (screen in Screen.GENERAL_SCREENS) {
                    composable(screen.route) {
                        when (screen.route) {
                            Home.route -> startMenuComposition.StartMenu(navController = navController)
                            Questions.route -> questionComposition.QuestionScreen(navController, scope)
                            Exam.route -> examComposition.ExamScreen()
                            Statistics.route -> statisticComposition.StatisticScreen()
                            Configuration.route -> configComposition.ConfigScreen()
                        }
                        topBarViewModel.onTopBarTitleChange(screen.title)
                        topBarViewModel.onHideFilter(Constants.FILTER_ALPHA_INVISIBLE)
                    }
                }

                for (screen in Screen.CHAPTER_SCREENS) {
                    composable(screen.route) {
                        screen.chapter?.let { chapter -> questionListComposition.LoadChapterList(chapter, isDialogOpen, onOpenDialog) }
                        topBarViewModel.onTopBarTitleChange(screen.title)
                        topBarViewModel.onHideFilter(Constants.FILTER_ALPHA_VISIBLE)
                    }
                }
            }
        }
    }
}