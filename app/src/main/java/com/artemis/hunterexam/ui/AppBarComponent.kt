package com.artemis.hunterexam.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.artemis.hunterexam.R
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.CriteriaFilter
import com.artemis.hunterexam.model.Screen
import com.artemis.hunterexam.ui.theme.Artemis_Green
import com.artemis.hunterexam.ui.theme.Artemis_Yellow
import com.artemis.hunterexam.viewModel.AssignmentViewModel
import com.artemis.hunterexam.viewModel.GeneralViewModel
import com.artemis.hunterexam.viewModel.QuestionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
class AppBarComponent {

    @Composable
    fun GeneralTopAppBar(
        currentScreen: Screen.DrawerScreen,
        scope: CoroutineScope,
        state: ScaffoldState,
        generalViewModel: GeneralViewModel,
        assignmentViewModel: AssignmentViewModel,
        questionViewModel: QuestionViewModel
    ) {
        val title: String by generalViewModel.title.observeAsState(Constants.EMPTY_STRING)
        val questionFilter: Pair<Float, Boolean> by generalViewModel.questionFilter.observeAsState(
            Pair(Constants.ALPHA_VISIBLE, Constants.ENABLED)
        )
        val closeAssignmentScreen: Pair<Float, Boolean> by generalViewModel.assignmentCloseButton.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        val closeTrainingScreen: Pair<Float, Boolean> by generalViewModel.trainingCloseButton.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        val searchWidget: Pair<Float, Boolean> by generalViewModel.searchWidget.observeAsState(
            Pair(Constants.ALPHA_INVISIBLE, Constants.DISABLED)
        )
        val searchWidgetState by generalViewModel.searchWidgetState
        val searchTextState by generalViewModel.searchTextState

        val showAppBar: Boolean by assignmentViewModel.showAppBar.observeAsState(true)

        if (searchWidgetState) {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { generalViewModel.onChangeSearchTextState(it) },
                onCloseClicked = {
                    generalViewModel.onChangeSearchWidgetState(false)
                    questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                },
            )
        } else if (showAppBar) {
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
                if (currentScreen != Screen.DrawerScreen.Assignment) {
                    IconButton(onClick = { scope.launch { state.drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                } else {
                    Box(modifier = Modifier.padding(start = 10.dp)) {
                        Icon(Icons.Default.Assignment, contentDescription = "Info")
                    }
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
                        questionViewModel.onChangeFilter(CriteriaFilter.CUSTOM_SEARCH)
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
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

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
                        onClick = {
                            keyboardController?.hide()
                        }
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
                                onTextChange(Constants.EMPTY_STRING)
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
    fun ExpandTopicList(
        scope: CoroutineScope,
        state: ScaffoldState,
        navController: NavHostController,
        questionViewModel: QuestionViewModel,
        isTopicListExpanded: MutableState<Boolean>
    ) {
        ///Display Topic Screens
        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            for (topic in Screen.TOPIC_SCREENS) {
                TextButton(onClick = {
                    scope.launch {
                        state.drawerState.close()
                        navController.navigate(topic.route)
                        questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                        isTopicListExpanded.value = false
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
                            style = MaterialTheme.typography.subtitle2,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DrawerContent(
        questionViewModel: QuestionViewModel,
        scope: CoroutineScope,
        state: ScaffoldState,
        navController: NavHostController
    ) {
        val isTopicListExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()
        Column(
            Modifier.verticalScroll(state = scrollState, true)
        ) {
            Card(
                backgroundColor = Artemis_Yellow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
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
                        style = MaterialTheme.typography.h6
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
            TextButton(onClick = {
                scope.launch { state.drawerState.close() }
                    .also {
                        navController.navigate(Screen.DrawerScreen.Home.route)
                        questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                    }
            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_home_24),
                        contentDescription = "Startmenü"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = Screen.DrawerScreen.Home.title,
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White
                    )
                }
            }
            TextButton(onClick = {
                isTopicListExpanded.value = !isTopicListExpanded.value
            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var arrowIconOfTopic = R.drawable.ic_baseline_keyboard_arrow_right_24
                    if (isTopicListExpanded.value) {
                        arrowIconOfTopic = R.drawable.ic_baseline_keyboard_arrow_left_24
                    }
                    Image(
                        painter = painterResource(id = arrowIconOfTopic),
                        contentDescription = "Sachgebiete"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = Screen.DrawerScreen.QuestionCatalogue.title,
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White
                    )
                }
            }
            if (isTopicListExpanded.value) {
                ExpandTopicList(
                    scope = scope,
                    state = state,
                    navController = navController,
                    questionViewModel = questionViewModel,
                    isTopicListExpanded = isTopicListExpanded
                )
            }
            TextButton(onClick = {
                scope.launch { state.drawerState.close() }
                    .also {
                        navController.navigate(Screen.DrawerScreen.Dictionary.route)
                        questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                    }
            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                        contentDescription = "Dictionary"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Glossar",
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White
                    )
                }
            }
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
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White
                    )
                }
            }
            Divider(
                color = Color.Gray, thickness = 2.dp
            )
            TextButton(onClick = {
                scope.launch { state.drawerState.close() }
                    .also {
                        navController.navigate(Screen.DrawerScreen.QuestionCatalogue.route)
                        questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                    }
            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                        contentDescription = "Themenkatalog"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Themenkatalog",
                        style = MaterialTheme.typography.subtitle2,
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
                        contentDescription = "Privacy"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = Screen.DrawerScreen.Imprint.title,
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White
                    )
                }
            }
            TextButton(onClick = {
                scope.launch {
                    state.drawerState.close()
                    navController.navigate(Screen.DrawerScreen.Privacy.route)
                    questionViewModel.onChangeFilter(CriteriaFilter.ALL_QUESTIONS_SHUFFLED)
                }
            }) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                        contentDescription = "Privacy"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = Screen.DrawerScreen.Privacy.title,
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White
                    )
                }
            }
        }
    }
}
