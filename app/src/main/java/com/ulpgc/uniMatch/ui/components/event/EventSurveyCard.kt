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
    title: String,
    initialOptions: Map<String, Set<String>>,
    isEditing: Boolean = false,
    onDeleteSurvey: (() -> Unit)? = null,
    onConfirmSurvey: ((String, List<String>) -> Unit)? = null,
    onVoteSurvey: ((String) -> Unit)? = null
) {

    val finalTitle = if (title.isBlank()) stringResource(R.string.event_title) else title
    val finalOptions = if (initialOptions.isEmpty()) {
        mapOf(
            stringResource(R.string.option_one) to setOf(),
            stringResource(R.string.option_two) to setOf()
        )
    } else initialOptions

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var options by remember { mutableStateOf(finalOptions) }
    var editTitle by remember { mutableStateOf(finalTitle) }
    var editOptions by remember { mutableStateOf(finalOptions.keys.toList()) }
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
                    onAddOption = { editOptions =
                        editOptions + "New Option ${editOptions.size + 1}"
                    },
                    onConfirmSurvey = {
                        isConfirmed = true
                        if (onConfirmSurvey != null) {
                            onConfirmSurvey(editTitle, editOptions)
                        }
                        options = editOptions.associateWith { setOf<String>() }
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
                            if (key == selectedOption) {
                                value - (selectedOption ?: "")
                            } else if (key == selected) {
                                value + selected
                            } else {
                                value
                            }
                        }
                        selectedOption = if (selectedOption == selected) null else selected
                    }

                )
            }
        }
    }
}


