package com.artemis.hunterexam.helper

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.model.Screen.DrawerScreen.*
import com.artemis.hunterexam.ui.*
import com.artemis.hunterexam.viewModel.*

class NavHelper {

    @ExperimentalComposeUiApi
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
            isAssignmentDialogOpen: Boolean,
            onOpenAssignmentDialog: (Boolean) -> Unit,
            generalViewModel: GeneralViewModel,
            questionViewModel: QuestionViewModel,
            trainingViewModel: TrainingViewModel,
            assignmentViewModel: AssignmentViewModel,
            dictionaryViewModel: DictionaryViewModel
        ) {

            val startMenuComponent = StartMenuComponent()
            val questionComponent = TopicCatalogueComponent()
            val examComponent = AssignmentComponent()
            val statisticComponent = StatisticComponent()
            val questionListComponent = QuestionListComponent()
            val trainingComponent = TrainingComponent()
            val imprintComponent = ImprintComponent()
            val privacyComponent = PrivacyComponent()
            val dictionaryComponent = DictionaryComponent()

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
                                assignmentViewModel,
                                questionViewModel,
                                navController
                            )
                            QuestionCatalogue.route -> questionComponent.TopicCatalogueScreen(
                                questionViewModel, navController
                            )
                            Statistics.route -> statisticComponent.StatisticScreen(
                                questionViewModel
                            )
                            Imprint.route -> imprintComponent.ImprintScreen()
                            Privacy.route -> privacyComponent.PrivacyScreen()
                            Dictionary.route -> dictionaryComponent.DictionaryScreen(
                                dictionaryViewModel = dictionaryViewModel
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
                        generalViewModel.onChangeVisibilityOfTrainingCloseButton(
                            Pair(
                                Constants.ALPHA_INVISIBLE,
                                Constants.DISABLED
                            )
                        )
                        generalViewModel.onChangeVisibilityOfAssignmentCloseButton(
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
                                    AllQuestions.route -> questionListComponent.CurrentTopicScreen(
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
                                generalViewModel.onChangeVisibilityOfTrainingCloseButton(
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
                    composable(Assignment.route) {
                        examComponent.AssignmentScreen(
                            navController,
                            isAssignmentDialogOpen,
                            onOpenAssignmentDialog,
                            questionViewModel,
                            generalViewModel,
                            assignmentViewModel
                        )
                        generalViewModel.onTopBarTitleChange(Assignment.title)
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
                        generalViewModel.onChangeVisibilityOfAssignmentCloseButton(
                            Pair(
                                Constants.ALPHA_VISIBLE,
                                Constants.ENABLED
                            )
                        )
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
                        generalViewModel.onChangeVisibilityOfTrainingCloseButton(
                            Pair(
                                Constants.ALPHA_VISIBLE,
                                Constants.ENABLED
                            )
                        )
                        generalViewModel.onChangeVisibilityOfTrainingCloseButton(
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