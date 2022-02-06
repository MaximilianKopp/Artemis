package com.ataraxia.artemis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ataraxia.artemis.ui.StartMenuComposition
import com.ataraxia.artemis.ui.theme.ArtemisTheme

class MainActivity : ComponentActivity() {
    private val startMenuComposition = StartMenuComposition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArtemisTheme {
                startMenuComposition.StartScreen()
            }
        }
    }
}