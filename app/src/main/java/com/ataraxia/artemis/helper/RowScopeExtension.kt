package com.ataraxia.artemis.helper

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class RowScopeExtension {

    companion object {
        @Composable
        fun RowScope.TableCell(
            text: String,
            weight: Float,
        ) {
            Text(
                text = text,
                Modifier
                    .weight(weight),
                color = Color.White,
                style = MaterialTheme.typography.caption
            )
        }

        @Composable
        fun RowScope.TableCell(
            text: String,
            imageVector: ImageVector,
            tint: Color,
            weight: Float,
        ) {
            Text(
                text = text,
                Modifier
                    .weight(weight),
                color = Color.White,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End,
            )
            Icon(
                imageVector,
                contentDescription = "ImageVector for table cell",
                Modifier
                    .weight(weight)
                    .size(16.dp),
                tint = tint
            )
        }
    }
}