package com.ulpgc.uniMatch.ui.components.event.survey

import android.util.Log
import androidx.compose.runtime.Composable

@Composable
fun SurveyOptions(
    userId: String = "",
    options: List<String>,
    votes: Map<String, Set<String>> = emptyMap(),
    isEditing: Boolean,
    onOptionChange: (Int, String) -> Unit = { _, _ -> },
    onOptionRemove: (Int) -> Unit = {},
    onVote: (String) -> Unit = {}
) {
    val maxVotes = votes.values.maxOfOrNull { it.size } ?: 0

    Log.i("Survey", "SurveyOptions $votes")
    options.forEachIndexed { index, option ->
        SurveyOptionRow(
            option = option,
            votes = votes[option]?.size ?: 0,
            isSelected = votes[option]?.contains(userId) ?: false,
            maxVotes = maxVotes,
            onVote = { onVote(option) },
            isEditing = isEditing,
            onOptionChange = { newValue -> onOptionChange(index, newValue) },
            onOptionRemove = { onOptionRemove(index) },
            placeholder = "Option ${index + 1}"
        )
    }
}
