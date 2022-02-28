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
import com.ataraxia.artemis.data.AppBarViewModel
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBarComponent {


    @Composable
    fun GeneralTopAppBar(
        scope: CoroutineScope,
        state: ScaffoldState,
    ) {
        val topBarViewModel: AppBarViewModel = viewModel()
        val title: String by topBarViewModel.title.observeAsState("")
        val questionFilter: Float by topBarViewModel.questionFilter.observeAsState(Constants.ALPHA_INVISIBLE)
        val closeTrainingScreen: Float by topBarViewModel.closeTrainingScreen.observeAsState(
            Constants.ALPHA_INVISIBLE
        )
        TopAppBar(
            title = { Text(text = title) },
            backgroundColor = YELLOW_ARTEMIS,
            navigationIcon = {
                IconButton(onClick = { scope.launch { state.drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(
                    onClick = { topBarViewModel.onOpenFilterDialog(true) },
                    Modifier.alpha(questionFilter)
                ) {
                    Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
                }
                IconButton(
                    onClick = { topBarViewModel.onOpenTrainingDialog(true) },
                    Modifier.alpha(closeTrainingScreen)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        )
    }

    @Composable
    fun DrawerContent(
        scope: CoroutineScope,
        state: ScaffoldState,
        navController: NavHostController
    ) {
        val topBarViewModel: AppBarViewModel = viewModel()
        Screen.GENERAL_SCREENS.forEach { screen ->
            TextButton(
                onClick = {
                    scope.launch { state.drawerState.close() }
                        .also {
                            topBarViewModel.onTopBarTitleChange(screen.title)
                            navController.navigate(screen.route)
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
