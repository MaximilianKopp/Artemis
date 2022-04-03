package com.ataraxia.artemis.model

import com.ataraxia.artemis.R


class Screen {

    open class DrawerScreen(
        val drawable: Int?,
        var title: String,
        var route: String,
        var topic: Int
    ) {
        constructor(drawable: Int, title: String, route: String) : this(
            drawable,
            title,
            route,
            0
        )

        constructor(title: String, route: String, topic: Int) : this(
            null,
            title,
            route,
            topic
        )

        constructor(title: String, route: String) : this(
            null,
            title,
            route,
            0
        )

        object Home : DrawerScreen(
            R.drawable.ic_baseline_home_24,
            "Startmenü",
            "startmenu",
        )


        object Questions : DrawerScreen(
            R.drawable.ic_baseline_menu_book_24,
            "Sachgebiete",
            "questioncatalogue"
        )

        object Assignment : DrawerScreen(
            R.drawable.ic_baseline_assignment_24,
            "Prüfung",
            "assignment"
        )

        object Statistics : DrawerScreen(
            R.drawable.ic_baseline_insert_chart_24,
            "Statistik",
            "statistics"
        )

        object Configuration : DrawerScreen(
            R.drawable.ic_baseline_build_circle_24,
            "Einstellungen",
            "configuration"
        )

        object TopicWildLife : DrawerScreen(
            "Wildbiologie & Wildhege",
            "topicWildlife",
            Topic.TOPIC_1.ordinal
        )

        object TopicHuntingOperations : DrawerScreen(
            "Jagdbetrieb",
            "topicHuntingOperations",
            Topic.TOPIC_2.ordinal
        )

        object TopicWeaponsLawAndTechnology : DrawerScreen(
            "Waffenrecht & Technik",
            "topicWeaponsLawAndTechnology",
            Topic.TOPIC_3.ordinal
        )

        object TopicWildLifeTreatment : DrawerScreen(
            "Behandlung des Wildes",
            "topicWildLifeTreatment",
            Topic.TOPIC_4.ordinal
        )

        object TopicHuntingLaw : DrawerScreen(
            "Jagdrecht",
            "topicHuntingLaw",
            Topic.TOPIC_5.ordinal
        )

        object TopicPreservationOfWildLifeAndNature : DrawerScreen(
            "Tier- und Naturschutz",
            "topicPreservationOfWildLifeAndNature",
            Topic.TOPIC_6.ordinal
        )

        object Training : DrawerScreen(
            "Training",
            "training"
        )

        object Imprint : DrawerScreen(
            "Impressum",
            "imprint"
        )

        object Policy : DrawerScreen(
            "Datenschutzerklärung",
            "policy"
        )
    }

    companion object {
        val GENERAL_SCREENS = listOf(
            DrawerScreen.Home,
            DrawerScreen.Questions,
            DrawerScreen.Assignment,
            DrawerScreen.Statistics,
            DrawerScreen.Configuration,
        )

        val TOPIC_SCREENS = listOf(
            DrawerScreen.TopicWildLife,
            DrawerScreen.TopicHuntingOperations,
            DrawerScreen.TopicWeaponsLawAndTechnology,
            DrawerScreen.TopicWildLifeTreatment,
            DrawerScreen.TopicHuntingLaw,
            DrawerScreen.TopicPreservationOfWildLifeAndNature
        )
    }
}