package com.ataraxia.artemis.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBarComponent {


    @Composable
    fun GeneralTopAppBar(
        scope: CoroutineScope,
        state: ScaffoldState,
    ) {
        val generalViewModel: GeneralViewModel = viewModel()
        val title: String by generalViewModel.title.observeAsState("")
        val questionFilter: Pair<Float, Boolean> by generalViewModel.questionFilter.observeAsState(
            Pair(Constants.ALPHA_VISIBLE, Constants.ENABLED)
        )
        val closeTrainingScreen: Pair<Float, Boolean> by generalViewModel.closeTrainingScreen.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        TopAppBar(
            title = { Text(text = title) },
            backgroundColor = Artemis_Yellow,
            navigationIcon = {
                IconButton(onClick = { scope.launch { state.drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(
                    onClick = { generalViewModel.onOpenFilterDialog(true) },
                    Modifier.alpha(questionFilter.first),
                    enabled = questionFilter.second,
                ) {
                    Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
                }
                IconButton(
                    onClick = { generalViewModel.onOpenTrainingDialog(true) },
                    Modifier.alpha(closeTrainingScreen.first),
                    enabled = closeTrainingScreen.second
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        )
    }

    @Composable
    fun DrawerContent(
        questionViewModel: QuestionViewModel,
        scope: CoroutineScope,
        state: ScaffoldState,
        navController: NavHostController
    ) {
        val topBarViewModel: GeneralViewModel = viewModel()
        Screen.GENERAL_SCREENS.forEach { screen ->
            TextButton(
                onClick = {
                    scope.launch { state.drawerState.close() }
                        .also {
                            topBarViewModel.onTopBarTitleChange(screen.title)
                            navController.apply {
                                navigate(screen.route) {
                                    popUpTo(0)
                                }
                            }
                            questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                        }
                }) {
                Row {
                    Spacer(Modifier.height(36.dp))
                    Image(
                        painter = painterResource(id = screen.drawable!!),
                        contentDescription = "DrawerItemIcon",
                        modifier = Modifier.padding(end = 24.dp)
                    )
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
