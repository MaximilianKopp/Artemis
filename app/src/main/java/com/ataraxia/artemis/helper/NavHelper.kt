package com.ataraxia.artemis.helper

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ataraxia.artemis.data.GeneralViewModel
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.data.TrainingViewModel
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.model.Screen.DrawerScreen.*
import com.ataraxia.artemis.ui.*

class NavHelper {

    companion object {
        @Composable
        fun LoadNavigationRoutes(
            navController: NavHostController,
            paddingValues: PaddingValues,
            isFilterDialogOpen: Boolean,
            onOpenFilterDialog: (Boolean) -> Unit,
            isTrainingDialogClosed: Boolean,
            onOpenTrainingDialog: (Boolean) -> Unit
        ) {
            val generalViewModel: GeneralViewModel = viewModel()
            val questionViewModel: QuestionViewModel = viewModel()
            val trainingViewModel: TrainingViewModel = viewModel()
            val startMenuComponent = StartMenuComponent()
            val questionComponent = TopicCatalogueComponent()
            val examComponent = ExamComponent()
            val statisticComponent = StatisticComponent()
            val configComponent = ConfigComponent()
            val questionListComponent = QuestionListComponent()
            val trainingComponent = TrainingComponent()
            val currentScreen = generalViewModel.currentScreen.observeAsState(Home)

            NavHost(
                navController,
                currentScreen.value.route,
                Modifier.padding(paddingValues = paddingValues)
            ) {
                composable(currentScreen.value.route) {
                    when (currentScreen.value.route) {
                        Home.route -> startMenuComponent.StartMenu(generalViewModel)
                            .apply { generalViewModel.onShowStartScreenInfo(true) }
                        Questions.route -> questionComponent.TopicCatalogueScreen(
                            generalViewModel, questionViewModel
                        ).apply { generalViewModel.onShowStartScreenInfo(false) }
                        Exam.route -> examComponent.ExamScreen()
                            .apply {
                                generalViewModel.onShowStartScreenInfo(false)
                            }
                        Statistics.route -> statisticComponent.StatisticScreen(questionViewModel = questionViewModel)
                            .apply { generalViewModel.onShowStartScreenInfo(false) }
                        Configuration.route -> configComponent.ConfigScreen()
                            .apply { generalViewModel.onShowStartScreenInfo(false) }
                        Training.route -> trainingComponent.TrainingScreen(
                            navController = navController,
                            isTrainingDialogOpen = isFilterDialogOpen,
                            onOpenTrainingDialog = onOpenTrainingDialog,
                            questionViewModel = questionViewModel,
                            trainingViewModel = trainingViewModel,
                            generalViewModel = generalViewModel
                        )
                    }
                    generalViewModel.onHideFilter(Constants.ALPHA_INVISIBLE)
                    if (currentScreen.value.route != Training.route) {
                        for (topicScreen in Screen.TOPIC_SCREENS) {
                            when (currentScreen.value.route) {
                                topicScreen.route -> questionListComponent.CurrentTopicScreen(
                                    navController = navController,
                                    isFilterDialogOpen = isFilterDialogOpen,
                                    onOpenFilterDialog = onOpenFilterDialog,
                                    questionViewModel = questionViewModel,
                                    trainingViewModel = trainingViewModel,
                                    generalViewModel = generalViewModel,
                                    currentTopic = currentScreen.value.topic
                                ).apply {
                                    questionViewModel.onChangeTopic(topicScreen.topic)
                                    generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                }
                            }
                            generalViewModel.onCloseTrainingScreen(Constants.ALPHA_INVISIBLE)
                            generalViewModel.onTopBarTitleChange(currentScreen.value.title)
                        }
                    } else if (currentScreen.value.route == Training.route) {
                        generalViewModel.onCloseTrainingScreen(Constants.ALPHA_VISIBLE)
                    }
                }
                Log.v("Load CurrentTopicScreen", "Value: " + currentScreen.value.title)
                generalViewModel.onTopBarTitleChange(currentScreen.value.title)
            }
        }
    }
}