package com.ulpgc.uniMatch.ui.components.event.survey

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.components.event.EventSurveyCard
import androidx.compose.runtime.*

@Composable
fun SurveysList(
    userId: String = "",
    surveys: List<Survey> = emptyList(),
    isEditing: Boolean = true,
    onDeleteSurveyClick: ((Survey) -> Unit)? = null,
    onConfirmSurveyClick: ((Survey, String, List<String>) -> Unit)? = null,
    onVoteSurvey: ((Survey, String) -> Unit)? = null,
    OnQuitVoteSurvey: ((Survey, String) -> Unit)? = null
) {
    LazyColumn (
        modifier = Modifier
            .heightIn(max = Short.MAX_VALUE.toInt().dp)
    ) {
        items(surveys, key = { it.title }) { survey ->
            EventSurveyCard(
                userId = userId,
                survey = survey,
                isEditing = isEditing,
                onDeleteSurvey = { onDeleteSurveyClick?.invoke(survey) },
                onConfirmSurvey = { title, options -> onConfirmSurveyClick?.invoke(survey, title, options) },
                onVoteSurvey = { option ->

                    onVoteSurvey?.invoke(survey, option)
                },
                onQuitVoteSurvey = { option -> OnQuitVoteSurvey?.invoke(survey, option) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

