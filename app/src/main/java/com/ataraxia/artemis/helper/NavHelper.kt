package com.ataraxia.artemis.helper

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.model.Screen.DrawerScreen.*
import com.ataraxia.artemis.ui.*
import com.ataraxia.artemis.viewModel.*

class NavHelper {

    companion object {
        @ExperimentalFoundationApi
        @Composable
        fun LoadNavigationRoutes(
            navController: NavHostController,
            paddingValues: PaddingValues,
            isFilterDialogOpen: Boolean,
            onOpenFilterDialog: (Boolean) -> Unit,
            isTrainingDialogClosed: Boolean,
            onOpenTrainingDialog: (Boolean) -> Unit,
            generalViewModel: GeneralViewModel,
            questionViewModel: QuestionViewModel,
            trainingViewModel: TrainingViewModel,
            statisticViewModel: StatisticViewModel,
            assignmentViewModel: AssignmentViewModel
        ) {

            val startMenuComponent = StartMenuComponent()
            val questionComponent = TopicCatalogueComponent()
            val examComponent = AssignmentComponent()
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
                                statisticViewModel,
                                navController
                            )
                            Questions.route -> questionComponent.TopicCatalogueScreen(
                                questionViewModel, navController
                            )
                            Exam.route -> examComponent.AssignmentScreen(
                                navController,
                                questionViewModel,
                                trainingViewModel,
                                generalViewModel,
                                assignmentViewModel,
                                statisticViewModel
                            )
                            Statistics.route -> statisticComponent.StatisticScreen(
                                questionViewModel, statisticViewModel, navController
                            )
                            Configuration.route -> configComponent.ConfigScreen()
                            Training.route -> trainingComponent.TrainingScreen(
                                navController = navController,
                                isTrainingDialogOpen = isFilterDialogOpen,
                                onOpenTrainingDialog = onOpenTrainingDialog,
                                questionViewModel = questionViewModel,
                                trainingViewModel = trainingViewModel,
                                generalViewModel = generalViewModel,
                                statisticViewModel = statisticViewModel
                            )
                        }
                        generalViewModel.onHideSearchWidget(
                            Pair(
                                Constants.ALPHA_INVISIBLE,
                                Constants.DISABLED
                            )
                        )
                        generalViewModel.onHideFilter(
                            Pair(
                                Constants.ALPHA_INVISIBLE,
                                Constants.DISABLED
                            )
                        )
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
                                        generalViewModel = generalViewModel,
                                        trainingViewModel = trainingViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideSearchWidget(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                        generalViewModel.onHideFilter(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                    }
                                    TopicHuntingOperations.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        generalViewModel = generalViewModel,
                                        trainingViewModel = trainingViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideSearchWidget(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                        generalViewModel.onHideFilter(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                    }
                                    TopicWeaponsLawAndTechnology.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        generalViewModel = generalViewModel,
                                        trainingViewModel = trainingViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideSearchWidget(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                        generalViewModel.onHideFilter(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                    }
                                    TopicWildLifeTreatment.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        generalViewModel = generalViewModel,
                                        trainingViewModel = trainingViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideSearchWidget(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                        generalViewModel.onHideFilter(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                    }
                                    TopicHuntingLaw.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        generalViewModel = generalViewModel,
                                        trainingViewModel = trainingViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideSearchWidget(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                        generalViewModel.onHideFilter(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                    }
                                    TopicPreservationOfWildLifeAndNature.route -> questionListComponent.CurrentTopicScreen(
                                        navController = navController,
                                        isFilterDialogOpen = isFilterDialogOpen,
                                        onOpenFilterDialog = onOpenFilterDialog,
                                        questionViewModel = questionViewModel,
                                        generalViewModel = generalViewModel,
                                        trainingViewModel = trainingViewModel,
                                        currentTopic = topicScreen.topic
                                    ).apply {
                                        generalViewModel.onHideSearchWidget(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                        questionViewModel.onChangeTopic(topicScreen.topic)
                                        generalViewModel.onHideFilter(
                                            Pair(
                                                Constants.ALPHA_VISIBLE,
                                                Constants.ENABLED
                                            )
                                        )
                                    }

                                }
                                generalViewModel.onCloseTrainingScreen(
                                    Pair(
                                        Constants.ALPHA_INVISIBLE,
                                        Constants.DISABLED
                                    )
                                )
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
                            onOpenTrainingDialog = onOpenTrainingDialog,
                            statisticViewModel = statisticViewModel
                        )
                        generalViewModel.onTopBarTitleChange(Training.title)
                        generalViewModel.onHideSearchWidget(
                            Pair(
                                Constants.ALPHA_INVISIBLE,
                                Constants.DISABLED
                            )
                        )
                        generalViewModel.onHideFilter(
                            Pair(
                                Constants.ALPHA_INVISIBLE,
                                Constants.DISABLED
                            )
                        )
                        generalViewModel.onCloseTrainingScreen(
                            Pair(
                                Constants.ALPHA_VISIBLE,
                                Constants.ENABLED
                            )
                        )
                    }
                }
            }
        }
    }
}