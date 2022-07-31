package com.artemis.hunterexam.model


class Screen {

    open class DrawerScreen(
        var title: String,
        var route: String,
        var topic: Int
    ) {
        constructor(title: String, route: String) : this(
            title,
            route,
            0
        )

        object Home : DrawerScreen(
            "Startmenü",
            "startmenu",
        )

        object QuestionCatalogue : DrawerScreen(
            "Sachgebiete",
            "questioncatalogue"
        )

        object Assignment : DrawerScreen(
            "Prüfung",
            "assignment"
        )

        object Statistics : DrawerScreen(
            "Statistik",
            "statistics"
        )

        object Configuration : DrawerScreen(
            "Einstellungen",
            "configuration"
        )

        object Imprint : DrawerScreen(
            "Impressum",
            "imprint"
        )

        object Privacy : DrawerScreen(
            "Datenschutz",
            "policy"
        )

        object Dictionary : DrawerScreen(
            "Glossar",
            "dictionary"
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

        object AllQuestions : DrawerScreen(
            "Alle Fragen",
            "allQuestions",
            Topic.TOPIC_7.ordinal
        )

        object Training : DrawerScreen(
            "Training",
            "training"
        )
    }

    companion object {
        val GENERAL_SCREENS = listOf(
            DrawerScreen.Home,
            DrawerScreen.QuestionCatalogue,
            DrawerScreen.Assignment,
            DrawerScreen.Statistics,
            DrawerScreen.Configuration,
            DrawerScreen.Imprint,
            DrawerScreen.Privacy,
            DrawerScreen.Dictionary
        )

        val TOPIC_SCREENS = listOf(
            DrawerScreen.TopicWildLife,
            DrawerScreen.TopicHuntingOperations,
            DrawerScreen.TopicWeaponsLawAndTechnology,
            DrawerScreen.TopicWildLifeTreatment,
            DrawerScreen.TopicHuntingLaw,
            DrawerScreen.TopicPreservationOfWildLifeAndNature,
            DrawerScreen.AllQuestions
        )
    }
}