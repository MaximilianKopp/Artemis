package com.ataraxia.artemis

import AppBarViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.ataraxia.artemis.data.QuestionViewModel
import com.ataraxia.artemis.ui.StartMenuComposition
import com.ataraxia.artemis.ui.theme.ArtemisTheme

class MainActivity : ComponentActivity() {
    private val appBarViewModel: AppBarViewModel by viewModels()
    private val questionViewModel: QuestionViewModel by viewModels()
    private val startMenuComposition = StartMenuComposition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArtemisTheme {
                startMenuComposition.StartScreen(appBarViewModel, questionViewModel)
            }
        }
    }
}