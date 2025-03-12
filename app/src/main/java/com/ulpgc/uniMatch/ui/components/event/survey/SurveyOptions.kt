package com.ulpgc.uniMatch.ui.components.event.survey

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
    val maxVotes = votes.values.maxOfOrNull { it.size } ?: 1

    options.forEachIndexed { index, option ->
        votes[option]?.contains(userId)?.let {
            SurveyOptionRow(
                option = option,
                votes = votes[option]?.size ?: 0,
                isSelected = it,
                maxVotes = maxVotes,
                onVote = {onVote(option)} ,
                isEditing = isEditing,
                onOptionChange = { newValue -> onOptionChange(index, newValue) },
                onOptionRemove = { onOptionRemove(index) }
            )
        }
    }
}
