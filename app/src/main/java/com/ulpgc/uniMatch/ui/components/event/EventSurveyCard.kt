package com.ulpgc.uniMatch.ui.components.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyBottomButtons
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyOptionRow
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyOptions
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyTitle

@Composable
fun EventSurveyCard(
    title: String = "",
    initialOptions: Map<String, Int> = mapOf(
        stringResource(R.string.option_one) to 0,
        stringResource(R.string.option_two) to 0
    ),
    isEditing: Boolean = false,
    onDeleteSurvey: (() -> Unit)? = null,
    onConfirmSurvey: ((String, List<String>) -> Unit)? = null
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var options by remember { mutableStateOf(initialOptions) }
    var editTitle by remember { mutableStateOf(title) }
    var editOptions by remember { mutableStateOf(initialOptions.keys.toList()) }
    var isConfirmed by remember { mutableStateOf(!isEditing) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!isConfirmed) {
            Column {
                SurveyTitle(
                    title = editTitle,
                    onDeleteSurvey = onDeleteSurvey,
                    onTitleChange = { editTitle = it },
                    isEditing = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                SurveyOptions(
                    options = editOptions,
                    isEditing = true,
                    onOptionChange = { index, newValue ->
                        editOptions = editOptions.toMutableList().also { it[index] = newValue }
                    },
                    onOptionRemove = { index ->
                        editOptions = editOptions.toMutableList().also { it.removeAt(index) }
                    },
                    onVote = {}
                )
                Spacer(modifier = Modifier.height(8.dp))

                SurveyBottomButtons(
                    onAddOption = { editOptions = editOptions + "New Option ${editOptions.size + 1}" },
                    onConfirmSurvey = {
                        isConfirmed = true
                        onConfirmSurvey?.invoke(editTitle, editOptions)
                        options = editOptions.associateWith { 0 }
                    },
                    optionsSize = editOptions.size
                )
            }
        } else {
            Column {
                SurveyTitle(
                    title = editTitle,
                    isEditing = true,
                    onDeleteSurvey = onDeleteSurvey,
                )

                SurveyOptions(
                    options = options.keys.toList(),
                    isEditing = false,
                    votes = options,
                    selectedOption = selectedOption,
                    onVote = { selected ->
                        options = options.mapValues { (key, value) ->
                            when {
                                key == selectedOption -> value - 1
                                key == selected -> value + 1
                                else -> value
                            }
                        }
                        selectedOption = if (selectedOption == selected) null else selected
                    }
                )

            }
        }
    }
}
