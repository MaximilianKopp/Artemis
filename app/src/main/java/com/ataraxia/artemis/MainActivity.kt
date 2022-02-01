package com.ataraxia.artemis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.ataraxia.artemis.ui.AppBarComposition.AppBarViewModel
import com.ataraxia.artemis.ui.QuestionListComposition
import com.ataraxia.artemis.ui.StartMenuComposition
import com.ataraxia.artemis.ui.theme.ArtemisTheme

class MainActivity : ComponentActivity() {
    private val appBarViewModel: AppBarViewModel by viewModels()
    private val startMenuComposition = StartMenuComposition()
    private val questionListComposition = QuestionListComposition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArtemisTheme {
                startMenuComposition.StartScreen(appBarViewModel)
            }
        }
    }
}