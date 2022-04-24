package com.ataraxia.artemis.ui

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
                text = "Datenschutz",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = "Die Applikation erhebt/speichert keine personenbezogenen Daten wie bspw. Name, Anschrift oder E-Mail-Adressen und betreibt keine Weiterverarbeitung bzw. gewerbliche Veräußerung von Informationen, die aus dem Nutzerverhalten abgeleitet werden könnten. \nDie Applikation ist vollständig offline nutzbar und setzt nur im Falle eine Aktualisierung durch Google-Play eine aktive Internverbindung voraus. \nDer Herausgeber und Eigentümer haftet nicht für eine eventuelle Weitergabe von Informationen, die im Rahmen der Google-Play Nutzungsbedingungen durchgeführt werden.",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }
    }
}