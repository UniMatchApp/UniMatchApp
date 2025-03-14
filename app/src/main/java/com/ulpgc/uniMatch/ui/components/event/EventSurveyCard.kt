package com.ulpgc.uniMatch.ui.components.event

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyBottomButtons
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyOptions
import com.ulpgc.uniMatch.ui.components.event.survey.SurveyTitle

@Composable
fun EventSurveyCard(
    userId: String = "",
    survey: Survey,
    isEditing: Boolean = false,
    onDeleteSurvey: (() -> Unit)? = null,
    onVoteSurvey: ((String) -> Unit)? = null,
    onQuitVoteSurvey: ((String) -> Unit)? = null
) {
    Log.i("Survey", "IsEditing survey card $isEditing")

    var title = survey.title
    var options by remember { mutableStateOf(survey.options) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SurveyTitle(
            title = title,
            isEditing = false,
            onDeleteSurvey = onDeleteSurvey)
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
                if (isSelected) onQuitVoteSurvey?.invoke(selected)
                else onVoteSurvey?.invoke(selected)
            }
        )

    }
}
