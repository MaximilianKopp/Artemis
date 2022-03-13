package com.ataraxia.artemis.helper

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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

            NavHost(
                navController,
                Home.route,
                Modifier.padding(paddingValues = paddingValues)
            ) {
                for (generalScreen in Screen.GENERAL_SCREENS) {
                    composable(generalScreen.route) {
                        when (generalScreen.route) {
                            Home.route -> startMenuComponent.StartMenu(
                                generalViewModel,
                                navController
                            )
                                .apply { generalViewModel.onShowStartScreenInfo(true) }
                            Questions.route -> questionComponent.TopicCatalogueScreen(
                                generalViewModel, questionViewModel, navController
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
                        generalViewModel.onTopBarTitleChange(generalScreen.title)
                    }
                    for (topicScreen in Screen.TOPIC_SCREENS) {
                        if (topicScreen.route != Training.route) {
                            composable(topicScreen.route) {
                                when (topicScreen.route) {
                                    TopicWildLife.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        trainingViewModel = trainingViewModel,
                                        generalViewModel = generalViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                    }
                                    TopicHuntingOperations.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        trainingViewModel = trainingViewModel,
                                        generalViewModel = generalViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                    }
                                    TopicWeaponsLawAndTechnology.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        trainingViewModel = trainingViewModel,
                                        generalViewModel = generalViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                    }
                                    TopicWildLifeTreatment.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        trainingViewModel = trainingViewModel,
                                        generalViewModel = generalViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                    }
                                    TopicHuntingLaw.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        trainingViewModel = trainingViewModel,
                                        generalViewModel = generalViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                    }
                                    TopicPreservationOfWildLifeAndNature.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        trainingViewModel = trainingViewModel,
                                        generalViewModel = generalViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(Constants.ALPHA_VISIBLE)
                                    }

                                }
                                generalViewModel.onCloseTrainingScreen(Constants.ALPHA_INVISIBLE)
                                generalViewModel.onTopBarTitleChange(topicScreen.title)
                            }
                            generalViewModel.onTopBarTitleChange(topicScreen.title)
                        }
                    }
                    composable(Training.route) {
                        trainingComponent.TrainingScreen(
                            navController = navController,
                            questionViewModel = questionViewModel,
                            trainingViewModel = trainingViewModel,
                            generalViewModel = generalViewModel,
                            isTrainingDialogOpen = isTrainingDialogClosed,
                            onOpenTrainingDialog = onOpenTrainingDialog
                        )
                        generalViewModel.onTopBarTitleChange(Training.title)
                        generalViewModel.onHideFilter(Constants.ALPHA_INVISIBLE)
                        generalViewModel.onCloseTrainingScreen(Constants.ALPHA_VISIBLE)
                    }
                }
            }
        }
    }
}