package com.ataraxia.artemis.ui

import AppBarViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBarComposition {


    @Composable
    fun GeneralTopAppBar(
        scope: CoroutineScope,
        state: ScaffoldState,
    ) {
        val topBarViewModel: AppBarViewModel = viewModel()
        val title: String by topBarViewModel.title.observeAsState("")
        val filter: Float by topBarViewModel.filter.observeAsState(Constants.FILTER_ALPHA_INVISIBLE)
        val filterDialog: Boolean by topBarViewModel.filterDialog.observeAsState(false)
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
                    Modifier.alpha(filter)
                ) {
                    Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
                }
            }
        )
        Box {
            if (filterDialog) {
                FilterDialog()
            }
        }
    }

    @Composable
    fun FilterDialog(
    ) {
        val topBarViewModel: AppBarViewModel = viewModel()
        AlertDialog(
            onDismissRequest = { topBarViewModel.onOpenFilterDialog(false) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Filterauswahl",
                        style = MaterialTheme.typography.h4
                    )
                    Divider(thickness = 2.dp)
                    Text(
                        text = "Der Filter legt fest, welche Fragen vorausgewählt werden:",
                        style = MaterialTheme.typography.body1
                    )
                }
            },
            buttons = {
                Column(
                    Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {

                        },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Alle Fragen",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Noch nicht gelernt",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Falsch beantwortet",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        Modifier
                            .width(300.dp)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Favouriten",
                            style = MaterialTheme.typography.body1
                        )
                    }
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
        Screen.SCREENS.forEach { screen ->
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
