package com.ataraxia.hunterexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.ataraxia.hunterexam.ui.AppBarComposition.AppBarViewModel
import com.ataraxia.hunterexam.ui.StartMenuComposition
import com.ataraxia.hunterexam.ui.theme.HunterExamTheme

class MainActivity : ComponentActivity() {
    private val appBarViewModel: AppBarViewModel by viewModels()
    private val startMenuComposition = StartMenuComposition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HunterExamTheme {
                startMenuComposition.StartScreen(appBarViewModel)
            }
        }
    }
}