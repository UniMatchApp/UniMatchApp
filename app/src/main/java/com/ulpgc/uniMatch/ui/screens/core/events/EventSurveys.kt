package com.ulpgc.uniMatch.ui.screens.core.events

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.event.survey.SurveysList

@Composable
fun EventSurveys(
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel
) {
    val userId = userViewModel.userId
    val event = eventViewModel.eventData.collectAsState().value

    val surveyMap = mutableMapOf<Survey, Boolean>()
    event?.surveys?.forEach {
        surveyMap[it] = false
    }


    Log.i("EventSurveys", "Id ${userId}")

    if (userId != null && event != null) {
        SurveysList(
            userId = userId,
            surveys = surveyMap,
            onVoteSurvey = { survey, option ->
                Log.i("Survey", "EventSurveysScreen ${survey.title} with option $option")
                eventViewModel.voteSurvey(event.eventId, survey.title, option)
            },
            OnQuitVoteSurvey = { survey, option ->
                eventViewModel.quitVoteSurvey(event.eventId, survey.title, option)
            }
        )

    }

}