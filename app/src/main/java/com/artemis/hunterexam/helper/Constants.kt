package com.artemis.hunterexam.helper

class Constants {
    companion object {
        const val TRAINING = "Training"
        const val SIZE_PER_TRAINING_UNIT_MIN = 10
        const val SIZE_PER_TRAINING_UNIT_MAX = 100
        const val APP_NAME = "Artemis"
        const val DESCRIPTION = "Prüfungstrainer zur staatlichen Jagdprüfung in \nRheinland-Pfalz"

        const val ALPHA_VISIBLE = 1f
        const val ALPHA_INVISIBLE = 0f

        const val ENABLED = true
        const val DISABLED = false

        const val EMPTY_STRING = ""
        const val LAST_SEEN_DEFAULT = "01.03.22, 00:00"

        const val TRAINING_SELECTION_A = "a"
        const val TRAINING_SELECTION_B = "b"
        const val TRAINING_SELECTION_C = "c"
        const val TRAINING_SELECTION_D = "d"

        const val SIZE_OF_EACH_ASSIGNMENT_TOPIC = 20

        const val TABLE_NAME_CONFIGURATION = "configuration"
        const val TABLE_NAME_DICTIONARY = "dictionary"
        const val TABLE_NAME_QUESTIONS = "questions"

        const val PRIVACY_HEADER = "Datenschutz"
        const val PRIVACY_TEXT =
            "Die Applikation erhebt bzw. speichert weder personenbezogenen Daten wie bspw. Namen, Anschriften oder E-Mail-Adressen noch betreibt sie die Weiterverarbeitung oder gewerbliche Veräußerung von Informationen, die aus dem Nutzerverhalten abgeleitet werden könnten. \nDie Applikation ist vollständig offline nutzbar und setzt nur im Falle eine Aktualisierung durch Google-Play eine aktive Internverbindung voraus. \nDer Herausgeber und Eigentümer ist nicht für Weiterverarbeitungen von Informationen verantwortlich, die im Rahmen der Google-Play Nutzungsbedingungen vorausgesetzt und durch den Endnutzer akzeptiert worden sind.\n"

        const val SHUFFLED = "Zufällige Auswahl"
        const val CHRONOLOGICAL = "Alle Fragen (chronologisch)"
        const val NOT_LEARNED = "Noch nicht gelernt"
        const val ONCE_LEARNED = "Mind. 1x richtig beantwortet"
        const val FAILED = "Falsch beantwortet"
        const val FAVOURITES = "Favouriten"
        const val LAST_VIEWED = "Seit 1 Woche nicht angesehen"
        const val CUSTOM_SEARCH = "Benutzerdefinierte Suche"
        const val NO_SELECTION = "Keine Auswahl"

        const val STATISTICS_ONCE_LEARNED_TOTAL = "OnceLearnedTotal"
        const val STATISTICS_FAILED_TOTAL = "FailedTotal"
        const val STATISTICS_TWICE_LEARNED_TOTAL = "TwiceLearnedTotal"
        const val STATISTICS_TOTAL_PERCENTAGE = "TotalPercentage"

        const val BTN_ANSWER = "Antworten"

        const val PRIVACY_PERMISSIONS =
            "Die Applikation benötigt die Freigabe der Vibrations-Funktionalität"
        const val DICTIONARY_HINT =
            "Das Anklicken dieses Links öffnet Ihren Web-Browser und leitet Sie zum Wikipedia-Eintrag weiter"
    }
}