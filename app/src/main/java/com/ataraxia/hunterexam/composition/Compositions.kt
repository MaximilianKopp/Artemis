package com.ataraxia.hunterexam.composition

import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

class Compositions {

    companion object {

        @Preview
        @Composable
        fun startScreen() {
            Card {
                Text(text = "1. Schritt lernen")
            }
        }
    }
}