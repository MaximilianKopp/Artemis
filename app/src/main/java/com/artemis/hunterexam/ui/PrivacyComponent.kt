package com.artemis.hunterexam.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.artemis.hunterexam.helper.Constants

class PrivacyComponent {

    @Composable
    fun PrivacyScreen() {

        val scrollableState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(15.dp)
                .verticalScroll(state = scrollableState, true)
        ) {
            Text(
                text = Constants.PRIVACY_HEADER,
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = Constants.PRIVACY_TEXT,
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Text(
                text = Constants.PRIVACY_PERMISSIONS,
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }
    }
}