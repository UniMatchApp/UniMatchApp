package com.ulpgc.uniMatch.ui.components.event.survey

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.components.event.EventSurveyCard

@Composable
fun SurveysList(
    surveys: List<Survey>,
    onDeleteSurveyClick: (Survey) -> Unit,
    onConfirmSurveyClick: (Survey, String, List<String>) -> Unit
) {
    Log.i("SurveysList", "SurveysList $surveys")
    surveys.forEach { survey ->
        EventSurveyCard(
            survey = survey,
            isEditing = true,
            onDeleteSurvey = {
                onDeleteSurveyClick(survey)
            },
            onConfirmSurvey = { title, options ->
                onConfirmSurveyClick(survey, title, options)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}