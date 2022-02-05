package com.ataraxia.artemis.model

import com.ataraxia.artemis.R


class Screen {

    open class DrawerScreen(val drawable: Int?, var title: String, var route: String) {

        constructor(title: String, route: String) : this(null, title, route) {
            this.title = title
            this.route = route
        }

        object Home : DrawerScreen(
            R.drawable.ic_baseline_language_24,
            "Startmenü", "startmenu"
        )

        object Questions : DrawerScreen(
            R.drawable.ic_baseline_menu_book_24,
            "Sachgebiete",
            "questioncatalogue"
        )

        object Exam : DrawerScreen(
            R.drawable.ic_baseline_assignment_24,
            "Prüfung",
            "exam"
        )

        object Statistics : DrawerScreen(
            R.drawable.ic_baseline_insert_chart_24,
            "Statistik",
            "statistics"
        )

        object Configuration : DrawerScreen(
            R.drawable.ic_baseline_build_circle_24,
            "Konfiguration",
            "configuration"
        )

        object TopicWildLife : DrawerScreen(
            "Wildbiologie & Wildhege",
            "topicWildlife"
        )

        object TopicHuntingOperations : DrawerScreen(
            "Jagdbetrieb",
            "topicHuntingOperations"
        )

        object TopicWeaponsLawAndTechnology : DrawerScreen(
            "Waffenrecht & Technik",
            "topicWeaponsLawAndTechnology"
        )

        object TopicWildLifeTreatment : DrawerScreen(
            "Behandlung des Wildes",
            "topicWildLifeTreatment"
        )

        object TopicHuntingLaw : DrawerScreen(
            "Jagdrecht",
            "topicHuntingLaw"
        )

        object TopicPreservationOfWildLifeAndNature : DrawerScreen(
            "Tier- und Naturschutz",
            "topicPreservationOfWildLifeAndNature"
        )

    }

    companion object {
        val SCREENS = listOf(
            DrawerScreen.Home,
            DrawerScreen.Questions,
            DrawerScreen.Exam,
            DrawerScreen.Statistics,
            DrawerScreen.Configuration
        )
    }
}