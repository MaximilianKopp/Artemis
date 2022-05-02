package com.ataraxia.artemis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ataraxia.artemis.ui.StartMenuComponent
import com.ataraxia.artemis.ui.theme.ArtemisTheme
import com.ataraxia.artemis.viewModel.*

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    private val startMenuComposition = StartMenuComponent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val generalViewModel: GeneralViewModel = viewModel()
            val questionViewModel: QuestionViewModel = viewModel()
            val trainingViewModel: TrainingViewModel = viewModel()
            val statisticViewModel: StatisticViewModel = viewModel()
            val assignmentViewModel: AssignmentViewModel = viewModel()

            ArtemisTheme {
                startMenuComposition.StartScreen(
                    generalViewModel,
                    questionViewModel,
                    trainingViewModel,
                    statisticViewModel,
                    assignmentViewModel
                )
            }
        }
    }
}