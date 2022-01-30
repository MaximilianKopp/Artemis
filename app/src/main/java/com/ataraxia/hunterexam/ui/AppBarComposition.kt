package com.ataraxia.hunterexam.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ataraxia.hunterexam.model.Screen
import com.ataraxia.hunterexam.ui.theme.YELLOW_ARTEMIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppBarComposition {

    class AppBarViewModel : ViewModel() {
        private val _title = MutableLiveData("Artemis-Jägerprüfung")
        val title: LiveData<String> = _title

        fun onTopBarTitleChange(newTitle: String) {
            viewModelScope.launch {
                onTopBarTitleChangeCoroutine(newTitle)
            }
        }

        private suspend fun onTopBarTitleChangeCoroutine(newTitle: String) = withContext(Dispatchers.Default) {
            _title.postValue(newTitle)
        }

    }

    @Composable
    fun GeneralTopAppBar(
        scope: CoroutineScope,
        state: ScaffoldState,
        title: String
    ) {
        TopAppBar(
            title = { Text(text = title) },
            backgroundColor = YELLOW_ARTEMIS,
            navigationIcon = {
                IconButton(onClick = { scope.launch { state.drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "")
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
                        painter = painterResource(id = screen.drawable),
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
