package com.ataraxia.artemis.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ImprintComponent {

    @Composable
    fun ImprintScreen() {
        val scrollableState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(15.dp)
                .verticalScroll(state = scrollableState, true)
        ) {
            Text(
                text = "Angaben gemäß § 5 TMG",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = "Maximilian Kopp \nEinzelunternehmer \nGänsmarkt 4A 55128 Mainz \nTel: +49 1747349384 \nEmail: m.kopp89@web.de",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Divider(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                thickness = 2.dp,
                color = Color.White
            )
            Text(
                text = "Hinweis auf EU-Streitschlichtung",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = "Die Europäische Kommission stellt eine Plattform zur Online-Streitbeilegung (OS) bereit:\n" +
                        "http://ec.europa.eu/consumers/odr\n" +
                        "Die E-Mail-Adresse des Verantwortlichen finden sie oben im Impressum.",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Divider(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                thickness = 2.dp,
                color = Color.White
            )
            Text(
                text = "Informationspflicht nach dem Verbraucherstreitbeilegungsgesetz",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = "Der Herausgeber und Eigentümer dieser Anwendung nimmt nicht an einem Streitbeilegungsverfahren vor einer Verbraucherschlichtungsstelle teil.",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Divider(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                thickness = 2.dp,
                color = Color.White
            )
            Text(
                text = "Copyright und Quellenangabe",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = "Der Artemis-Prüfungstrainer ist geistiges Eigentum von Maximilian Kopp. \nEs wird hierbei zwischen der Applikation und den zugrunde liegenden Daten unterschieden. \nDer Quellcode sowie alle damit verbundenen Komponenten unterliegen dem Urheberrecht und anderen Gesetzen zum Schutz geistigen Eigentum" +
                        ". \n" +
                        "Sie dürfen nicht dekompiliert sowie ohne Kenntnis des Eigentümers weder für Handelszwecke oder zur Weitergabe kopiert, noch verändert und in anderen Produkten weiterverwendet werden.",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Text(
                text = "Die verwendeten Fragen basieren auf dem offiziellen Fragenkatalog des Landesjagdverband Rheinland-Pfalz e.V \n https://ljv-rlp.de/ausbildung-und-beratung/pruefungsfragen/",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
            Divider(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                thickness = 2.dp,
                color = Color.White
            )
            Text(
                text = "Haftungsausschluss",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            Text(
                text = "Der Artemis-Prüfungstrainer versteht sich als unverbindliches Hilfsmittel zur Vorbereitung auf die staatliche Jägerprüfung in Rheinland-Pfalz. \n" +
                        "Der Herausgeber und Eigentümer dieser Applikation leistet keine Gewähr auf Vollständigkeit und Richtigkeit des vom Landesjagdverband Rheinland-Pfalz e.V. bereitgestelltem Fragenkatalogs. \n" +
                        "Ferner wird keine Haftung für das Nichtbestehen einer Prüfung übernommen.",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }
    }
}