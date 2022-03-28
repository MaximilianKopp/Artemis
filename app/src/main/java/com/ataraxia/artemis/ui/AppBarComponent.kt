package com.ataraxia.artemis.ui

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
        scope: CoroutineScope,
        state: ScaffoldState,
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel
    ) {
        val title: String by generalViewModel.title.observeAsState("")
        val questionFilter: Pair<Float, Boolean> by generalViewModel.questionFilter.observeAsState(
            Pair(Constants.ALPHA_VISIBLE, Constants.ENABLED)
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
                scope = scope,
                state = state,
                generalViewModel = generalViewModel,
                questionFilter = questionFilter,
                searchWidget = searchWidget,
                onSearchTriggered = { generalViewModel.onChangeSearchWidgetState(true) },
                questionViewModel = questionViewModel,
                closeTrainingScreen = closeTrainingScreen
            )
        }
    }

    @Composable
    fun DefaultAppBar(
        title: String,
        scope: CoroutineScope,
        state: ScaffoldState,
        generalViewModel: GeneralViewModel,
        questionViewModel: QuestionViewModel,
        questionFilter: Pair<Float, Boolean>,
        searchWidget: Pair<Float, Boolean>,
        onSearchTriggered: () -> Unit,
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
