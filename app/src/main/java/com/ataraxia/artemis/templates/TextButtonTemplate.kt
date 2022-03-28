package com.ataraxia.artemis.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ataraxia.artemis.ui.theme.Artemis_Yellow

abstract class TextButtonTemplate : Modifier {
    companion object {

        val MENU_SHAPE = RoundedCornerShape(15.dp)
        val MENU_MODIFIER = Modifier
            .padding(15.dp)
            .size(150.dp, 75.dp)
            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))

        @Composable
        fun menuBackgroundColor() = textButtonColors(
            backgroundColor = Artemis_Yellow
        )
    }
}
