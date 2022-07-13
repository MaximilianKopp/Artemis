package com.ataraxia.artemis.helper

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataraxia.artemis.model.Dictionary
import com.ataraxia.artemis.ui.theme.Artemis_Yellow

@Composable
fun DictionaryLink(
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    fullText: String,
    dictionaryEntry: Dictionary,
    linkText: List<String>,
    linkTextColor: Color = Artemis_Yellow,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
) {
    val isDictionaryAlertDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val annotatedString = buildAnnotatedString {
        append(fullText)
        linkText.forEachIndexed { _, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize,
            ),
            start = 0,
            end = fullText.length
        )
    }

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            isDictionaryAlertDialogOpen.value = true
        }
    )
    if (isDictionaryAlertDialogOpen.value) {
        DictionaryAlertDialog(
            isDictionaryAlertDialogOpen = isDictionaryAlertDialogOpen,
            dictionaryEntry
        )
    }
}

@Composable
fun DictionaryAlertDialog(
    isDictionaryAlertDialogOpen: MutableState<Boolean>,
    dictionaryEntry: Dictionary
) {
    AlertDialog(
        onDismissRequest = { isDictionaryAlertDialogOpen.value = false },
        shape = RoundedCornerShape(CornerSize(25.dp)),
        modifier = Modifier.border(
            1.dp,
            Color.Black,
            shape = RoundedCornerShape(CornerSize(25.dp))
        ),
        backgroundColor = Artemis_Yellow,
        text = {
            Column {
                Text(
                    text = "${dictionaryEntry.item}:",
                    style = MaterialTheme.typography.body2,
                    color = Color.Black
                )
                Text(
                    text = dictionaryEntry.definition,
                    style = MaterialTheme.typography.body2,
                    color = Color.Black
                )
                Column(
                    modifier = Modifier.padding(top = 5.dp)
                ) {
                    HyperlinkText(
                        fullText = dictionaryEntry.url,
                        linkText = listOf(dictionaryEntry.url),
                        fontSize = 12.sp,
                        linkTextColor = Color.Black,
                        hyperlinks = listOf(dictionaryEntry.url)
                    )
                    Text(
                        text = "Das Anklicken dieses Links öffnet Ihren Web-Browser und leitet Sie zum Wikipedia-Eintrag weiter",
                        style = MaterialTheme.typography.overline
                    )
                }
            }
        },
        buttons = {}
    )
}