package com.artemis.hunterexam.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemis.hunterexam.helper.Constants
import com.artemis.hunterexam.helper.HyperlinkText
import com.artemis.hunterexam.ui.theme.Artemis_Blue
import com.artemis.hunterexam.viewModel.DictionaryViewModel

@OptIn(ExperimentalFoundationApi::class)
class DictionaryComponent {

    @Composable
    fun DictionaryScreen(dictionaryViewModel: DictionaryViewModel) {
        DictionaryContent(dictionaryViewModel)
    }

    @Composable
    fun DictionaryContent(dictionaryViewModel: DictionaryViewModel) {
        val dictionaryItems = dictionaryViewModel.dictionaryItems
        val dictionaryLiveData = remember { mutableStateOf(dictionaryItems) }
        val query = remember { mutableStateOf(Constants.EMPTY_STRING) }
        val focusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current

        LazyColumn()
        {
            stickyHeader {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(top = 10.dp)
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = query.value,
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        Color.Black,
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Artemis_Blue
                    ),
                    onValueChange = { searchWord ->
                        query.value = searchWord
                        dictionaryLiveData.value =
                            dictionaryItems.filter {
                                it.item.contains(
                                    searchWord,
                                    ignoreCase = true
                                )
                            }
                        Log.v("Current query", query.value)
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Black
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            query.value = Constants.EMPTY_STRING
                            dictionaryLiveData.value = dictionaryItems
                            focusManager.clearFocus(true)
                        }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close icon of the TextField"
                            )
                        }
                    })
            }
            items(dictionaryLiveData.value) { dictionaryItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Column(
                        Modifier
                            .padding(5.dp)
                    ) {
                        Text(
                            text = dictionaryItem.item,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dictionaryItem.definition,
                            style = MaterialTheme.typography.body2,
                        )
                        HyperlinkText(
                            fullText = dictionaryItem.url,
                            linkText = listOf(dictionaryItem.url),
                            linkTextColor = Color.Black,
                            fontSize = 12.sp,
                            hyperlinks = listOf(dictionaryItem.url),
                        )
                        Text(
                            text = "Das Anklicken dieses Links öffnet Ihren Web-Browser und leitet Sie zum Wikipedia-Eintrag weiter",
                            style = MaterialTheme.typography.overline
                        )
                    }
                }
            }
        }
    }
}