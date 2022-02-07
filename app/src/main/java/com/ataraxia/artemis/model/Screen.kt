package com.ataraxia.artemis.model

import com.ataraxia.artemis.R
import com.ataraxia.artemis.helper.Constants


class Screen {

    open class DrawerScreen(
        val drawable: Int?,
        var title: String,
        var route: String,
        var chapter: String?
    ) {
        constructor(drawable: Int?, title: String, route: String) : this(
            drawable,
            title,
            route,
            null
        )

        constructor(title: String, route: String, chapter: String?) : this(
            null,
            title,
            route,
            chapter
        )

        object Home : DrawerScreen(
            R.drawable.ic_baseline_language_24,
            "Startmenü",
            "startmenu",
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
            "topicWildlife",
            Constants.CHAPTER_1
        )

        object TopicHuntingOperations : DrawerScreen(
            "Jagdbetrieb",
            "topicHuntingOperations",
            Constants.CHAPTER_2
        )

        object TopicWeaponsLawAndTechnology : DrawerScreen(
            "Waffenrecht & Technik",
            "topicWeaponsLawAndTechnology",
            Constants.CHAPTER_3
        )

        object TopicWildLifeTreatment : DrawerScreen(
            "Behandlung des Wildes",
            "topicWildLifeTreatment",
            Constants.CHAPTER_4
        )

        object TopicHuntingLaw : DrawerScreen(
            "Jagdrecht",
            "topicHuntingLaw",
            Constants.CHAPTER_5
        )

        object TopicPreservationOfWildLifeAndNature : DrawerScreen(
            "Tier- und Naturschutz",
            "topicPreservationOfWildLifeAndNature",
            Constants.CHAPTER_6
        )
    }

    companion object {
        val GENERAL_SCREENS = listOf(
            DrawerScreen.Home,
            DrawerScreen.Questions,
            DrawerScreen.Exam,
            DrawerScreen.Statistics,
            DrawerScreen.Configuration
        )

        val CHAPTER_SCREENS = listOf(
            DrawerScreen.TopicWildLife,
            DrawerScreen.TopicHuntingOperations,
            DrawerScreen.TopicWeaponsLawAndTechnology,
            DrawerScreen.TopicWildLifeTreatment,
            DrawerScreen.TopicHuntingLaw,
            DrawerScreen.TopicPreservationOfWildLifeAndNature
        )
    }
}