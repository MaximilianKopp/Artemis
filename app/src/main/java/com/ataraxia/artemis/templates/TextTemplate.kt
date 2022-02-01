package com.ataraxia.artemis.templates

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

abstract class TextTemplate {
    companion object {

        @Composable
        fun menuStyle() = MaterialTheme.typography.body1
        val MENU_COLOR = Color.Black
    }
}