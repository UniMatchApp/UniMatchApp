package com.ulpgc.uniMatch.ui.components.event

import android.util.Log
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
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyBottomButtons
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyOptionRow
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyOptions
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyTitle


@Composable
fun EventSurveyCard(
    userId: String = "",
    survey: Survey,
    isEditing: Boolean = false,
    onDeleteSurvey: (() -> Unit)? = null,
    onConfirmSurvey: ((String, List<String>) -> Unit)? = null,
    onVoteSurvey: ((String) -> Unit)? = null,
    onQuitVoteSurvey: ((String) -> Unit)? = null
) {

    val finalTitle = if (survey.title.isBlank()) stringResource(R.string.event_title) else survey.title
    val finalOptions = if (survey.options.isEmpty()) {
        mapOf(
            stringResource(R.string.option_one) to setOf(),
            stringResource(R.string.option_two) to setOf()
        )
    } else survey.options

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
                    isEditing = false,
                    onDeleteSurvey = onDeleteSurvey,
                )

                SurveyOptions(
                    userId = userId,
                    options = options.keys.toList(),
                    isEditing = false,
                    votes = options,
                    onVote = { selected ->
                        val isSelected = options[selected]?.contains(userId) == true

                        options = options.mapValues { (key, value) ->
                            when {
                                key == selected && isSelected -> value - userId
                                key == selected -> value + userId
                                value.contains(userId) -> value - userId
                                else -> value
                            }
                        }

                        Log.i("Survey", "EventSurveyCard $options")

                        if (isSelected) {
                            onQuitVoteSurvey?.invoke(selected)
                        } else {
                            onVoteSurvey?.invoke(selected)
                        }
                    }

                )
            }
        }
    }
}


