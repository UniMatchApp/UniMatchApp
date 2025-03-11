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
    onDeleteSurveyClick: ((Survey) -> Unit)? = null,  // Hacemos la función opcional
    onConfirmSurveyClick: ((Survey, String, List<String>) -> Unit)? = null  // Hacemos la función opcional
) {
    var surveyList by remember { mutableStateOf(surveys) }

    LaunchedEffect(surveys) {
        surveyList = surveys
    }

    Log.i("SurveysList", "SurveysList $surveyList")

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
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
