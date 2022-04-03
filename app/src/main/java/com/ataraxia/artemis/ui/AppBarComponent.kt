package com.ataraxia.artemis.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ataraxia.artemis.R
import com.ataraxia.artemis.helper.Constants
import com.ataraxia.artemis.helper.CriteriaFilter
import com.ataraxia.artemis.model.Screen
import com.ataraxia.artemis.ui.theme.Artemis_Green
import com.ataraxia.artemis.ui.theme.Artemis_Yellow
import com.ataraxia.artemis.viewModel.GeneralViewModel
import com.ataraxia.artemis.viewModel.QuestionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppBarComponent {


    @Composable
    fun GeneralTopAppBar(
        currentScreen: Screen.DrawerScreen,
        scope: CoroutineScope,
        state: ScaffoldState,
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel
    ) {
        val title: String by generalViewModel.title.observeAsState("")
        val questionFilter: Pair<Float, Boolean> by generalViewModel.questionFilter.observeAsState(
            Pair(Constants.ALPHA_VISIBLE, Constants.ENABLED)
        )
        val closeAssignmentScreen: Pair<Float, Boolean> by generalViewModel.closeAssignmentScreen.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        val closeTrainingScreen: Pair<Float, Boolean> by generalViewModel.closeTrainingScreen.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        val searchWidget: Pair<Float, Boolean> by generalViewModel.searchWidget.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        val searchWidgetState by generalViewModel.searchWidgetState
        val searchTextState by generalViewModel.searchTextState

        if (searchWidgetState) {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { generalViewModel.onChangeSearchTextState(it) },
                onCloseClicked = {
                    generalViewModel.onChangeSearchWidgetState(false)
                    questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                },
                onSearchClicked = {
                    Log.v("Clicked", "Auf search geklickt")
                }
            )
        } else {
            DefaultAppBar(
                title = title,
                currentScreen = currentScreen,
                scope = scope,
                state = state,
                generalViewModel = generalViewModel,
                questionFilter = questionFilter,
                searchWidget = searchWidget,
                onSearchTriggered = { generalViewModel.onChangeSearchWidgetState(true) },
                questionViewModel = questionViewModel,
                closeAssignmentScreen = closeAssignmentScreen,
                closeTrainingScreen = closeTrainingScreen
            )
        }
    }

    @Composable
    fun DefaultAppBar(
        title: String,
        currentScreen: Screen.DrawerScreen,
        scope: CoroutineScope,
        state: ScaffoldState,
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        questionFilter: Pair<Float, Boolean>,
        searchWidget: Pair<Float, Boolean>,
        onSearchTriggered: () -> Unit,
        closeAssignmentScreen: Pair<Float, Boolean>,
        closeTrainingScreen: Pair<Float, Boolean>
    ) {
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
                    onClick = {
                        onSearchTriggered()
                        questionViewModel.onChangeFilter(CriteriaFilter.SEARCH)
                    },
                    Modifier.alpha(searchWidget.first),
                    enabled = searchWidget.second,
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                Box {
                    if (currentScreen == Screen.DrawerScreen.Training) {
                        IconButton(
                            onClick = { generalViewModel.onOpenTrainingDialog(true) },
                            Modifier.alpha(closeTrainingScreen.first),
                            enabled = closeTrainingScreen.second
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    } else if (currentScreen == Screen.DrawerScreen.Assignment) {
                        IconButton(
                            onClick = { generalViewModel.onOpenAssignmentDialog(true) },
                            Modifier.alpha(closeAssignmentScreen.first),
                            enabled = closeAssignmentScreen.second
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun SearchAppBar(
        text: String,
        onTextChange: (String) -> Unit,
        onCloseClicked: () -> Unit,
        onSearchClicked: (String) -> Unit
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = AppBarDefaults.TopAppBarElevation,
            color = Artemis_Yellow
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { onTextChange(it) },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(ContentAlpha.medium),
                        text = "Nach Schlagwörter suchen...",
                        color = Color.Black
                    )
                },
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (text.isNotEmpty()) {
                                onTextChange("")
                            } else {
                                onCloseClicked()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = Color.Black
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(text)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Artemis_Yellow,
                    focusedIndicatorColor = Artemis_Yellow,
                    unfocusedIndicatorColor = Artemis_Yellow,
                    focusedLabelColor = Artemis_Green,
                    cursorColor = Artemis_Green.copy(alpha = ContentAlpha.medium)
                ).apply {
                    TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Artemis_Green,
                        focusedLabelColor = Artemis_Green,
                        focusedBorderColor = Artemis_Green,
                    )
                }
            )
        }
    }

    @Composable
    fun DrawerContent(
        questionViewModel: QuestionViewModel,
        scope: CoroutineScope,
        state: ScaffoldState,
        navController: NavHostController
    ) {
        Card(
            backgroundColor = Artemis_Yellow,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    scope.launch {
                        state.drawerState.close()
                        navController.navigate(Screen.DrawerScreen.Home.route)
                    }
                },
            shape = RectangleShape
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.75f)
                        .padding(start = 15.dp),
                    text = "Artemis - Prüfungstrainer",
                    color = Color.Black,
                    style = MaterialTheme.typography.h5
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_coat_of_arms_of_rhineland_palatinate),
                    contentDescription = "Coat of arms of RLP",
                    modifier = Modifier
                        .padding(bottom = 5.dp, end = 10.dp)
                        .weight(0.25f)
                )
            }
        }
        //Display Topic Screens
        for (topic in Screen.TOPIC_SCREENS) {
            TextButton(onClick = {
                scope.launch {
                    state.drawerState.close()
                    navController.navigate(topic.route)
                    questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                }

            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                        contentDescription = "Topics"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = topic.title,
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
        }
        Divider(
            color = Color.Gray, thickness = 2.dp
        )
        TextButton(onClick = {
            scope.launch { state.drawerState.close() }
                .also {
                    navController.navigate(Screen.DrawerScreen.Statistics.route)
                    questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                }
        }) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = "Statistik"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = Screen.DrawerScreen.Statistics.title,
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
        TextButton(onClick = {
            scope.launch {
                state.drawerState.close()
                navController.navigate(Screen.DrawerScreen.Configuration.route)
                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
            }
        }) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = "Einstellungen"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = Screen.DrawerScreen.Configuration.title,
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
        TextButton(onClick = {
            scope.launch {
                state.drawerState.close()
                navController.navigate(Screen.DrawerScreen.Imprint.route)
                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
            }
        }) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = "Impressum"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = Screen.DrawerScreen.Imprint.title,
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
        TextButton(onClick = {
            scope.launch {
                state.drawerState.close()
                navController.navigate(Screen.DrawerScreen.Policy.route)
                questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
            }
        }) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = "Datenschutz"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = Screen.DrawerScreen.Policy.title,
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
    }
}
