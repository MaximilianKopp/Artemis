package com.ataraxia.hunterexam.model

import com.ataraxia.hunterexam.R

class Screen {

    open class DrawerScreen(val drawable: Int, val title: String, val route: String) {
        object Home : DrawerScreen(
            R.drawable.ic_baseline_language_24,
            "Startmenü", "startmenu"
        )

        object Questions : DrawerScreen(
            R.drawable.ic_baseline_menu_book_24,
            "Fragenkatalog",
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