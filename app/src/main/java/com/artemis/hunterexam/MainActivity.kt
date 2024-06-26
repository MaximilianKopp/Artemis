package com.artemis.hunterexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artemis.hunterexam.ui.StartMenuComponent
import com.artemis.hunterexam.ui.theme.ArtemisTheme
import com.artemis.hunterexam.viewModel.*

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    private val startMenuComposition = StartMenuComponent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val generalViewModel: GeneralViewModel = viewModel()
            val questionViewModel: QuestionViewModel = viewModel()
            val trainingViewModel: TrainingViewModel = viewModel()
            val assignmentViewModel: AssignmentViewModel = viewModel()
            val dictionaryViewModel: DictionaryViewModel = viewModel()

            ArtemisTheme {
                startMenuComposition.StartScreen(
                    generalViewModel,
                    questionViewModel,
                    trainingViewModel,
                    assignmentViewModel,
                    dictionaryViewModel
                )
            }
        }
    }
}