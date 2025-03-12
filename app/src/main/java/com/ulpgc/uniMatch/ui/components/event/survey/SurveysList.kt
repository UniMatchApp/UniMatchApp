package com.ulpgc.uniMatch.ui.components.event.survey

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.ui.components.event.EventSurveyCard
import androidx.compose.runtime.*

@Composable
fun SurveysList(
    surveys: List<Survey>,
    isEditing: Boolean = true,
    onDeleteSurveyClick: ((Survey) -> Unit)? = null,
    onConfirmSurveyClick: ((Survey, String, List<String>) -> Unit)? = null,
    onVoteSurvey: ((Survey, String) -> Unit)? = null
) {
    var surveyList by remember { mutableStateOf(surveys) }

    LaunchedEffect(surveys) {
        surveyList = surveys
    }

    surveyList.forEach { survey ->
        key(survey.title) {
            EventSurveyCard(
                survey = survey,
                isEditing = isEditing,
                onDeleteSurvey = {
                    // Solo llamamos a la función si no es nula
                    onDeleteSurveyClick?.invoke(survey)
                },
                onConfirmSurvey = { title, options ->
                    // Solo llamamos a la función si no es nula
                    onConfirmSurveyClick?.invoke(survey, title, options)
                },
                onVoteSurvey = { option ->
                    Log.i("Survey", "SurveyList ${survey.title} with option $option")
                    onVoteSurvey?.invoke(survey, option)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
