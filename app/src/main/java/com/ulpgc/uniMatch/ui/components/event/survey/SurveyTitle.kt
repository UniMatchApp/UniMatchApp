package com.ulpgc.uniMatch.ui.components.event.survey

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R

@Composable
fun SurveyTitle(
    title: String,
    onDeleteSurvey: (() -> Unit)? = null,
    onTitleChange: ((String) -> Unit)? = null,
    isEditing: Boolean = false,
    placeholder: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = title,
            onValueChange = {
                if (onTitleChange != null) {
                    onTitleChange(it)
                }
            },
            enabled = isEditing,
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray)
                    )
                }
                innerTextField()
            }
        )

        if (isEditing) {
            IconButton(
                onClick = { onDeleteSurvey?.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_survey),
                    tint = Color.Gray
                )
            }
        }
    }
}
