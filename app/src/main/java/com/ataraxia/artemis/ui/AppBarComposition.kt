package com.ataraxia.artemis.ui

import AppBarViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.YELLOW_ARTEMIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBarComposition {

    @Composable
    fun GeneralTopAppBar(
        scope: CoroutineScope,
        state: ScaffoldState,
        title: String,
        filter: Float,
        topBarViewModel: AppBarViewModel
    ) {
        TopAppBar(
            title = { Text(text = title) },
            backgroundColor = YELLOW_ARTEMIS,
            navigationIcon = {
                IconButton(onClick = { scope.launch { state.drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "")
                }
            },
            actions = {

                IconButton(
                    onClick = {  },
                    Modifier.alpha(filter)
                ) {
                    Icon(Icons.Default.FilterAlt, contentDescription = "")
                }
            }
        )
    }

    @Composable
    fun DrawerContent(
        scope: CoroutineScope,
        state: ScaffoldState,
        onTitleChange: (String) -> Unit,
        navController: NavHostController
    ) {
        Screen.SCREENS.forEach { screen ->
            TextButton(
                onClick = {
                    scope.launch { state.drawerState.close() }
                        .also {
                            onTitleChange(screen.title)
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
