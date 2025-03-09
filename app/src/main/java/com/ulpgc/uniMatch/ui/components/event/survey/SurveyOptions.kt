package com.ulpgc.uniMatch.ui.components.event.survey

import androidx.compose.runtime.Composable


//@Composable
//fun SurveyOptions(
//    options: List<String>,
//    votes: Map<String, Int> = emptyMap(),
//    isEditing: Boolean,
//    selectedOption: String? = null,
//    onOptionChange: (Int, String) -> Unit = { _, _ -> },
//    onOptionRemove: (Int) -> Unit = {},
//    onVote: (String) -> Unit = {}
//) {
//    val maxVotes = votes.values.maxOrNull() ?: 1
//
//    options.forEachIndexed { index, option ->
//        SurveyOptionRow(
//            option = option,
//            votes = votes[option] ?: 0,
//            isSelected = option == selectedOption,
//            maxVotes = maxVotes,
//            onVote = onVote,
//            isEditing = isEditing,
//            onOptionChange = { newValue -> onOptionChange(index, newValue) },
//            onOptionRemove = { onOptionRemove(index) }
//        )
//    }
//}
@Composable
fun SurveyOptions(
    options: List<String>,
    votes: Map<String, Set<String>> = emptyMap(),
    isEditing: Boolean,
    selectedOption: String? = null,
    onOptionChange: (Int, String) -> Unit = { _, _ -> },
    onOptionRemove: (Int) -> Unit = {},
    onVote: (String) -> Unit = {}
) {
    val maxVotes = votes.values.maxOfOrNull { it.size } ?: 1

    options.forEachIndexed { index, option ->
        SurveyOptionRow(
            option = option,
            votes = votes[option]?.size ?: 0,
            isSelected = option == selectedOption,
            maxVotes = maxVotes,
            onVote = onVote,
            isEditing = isEditing,
            onOptionChange = { newValue -> onOptionChange(index, newValue) },
            onOptionRemove = { onOptionRemove(index) }
        )
    }
}
