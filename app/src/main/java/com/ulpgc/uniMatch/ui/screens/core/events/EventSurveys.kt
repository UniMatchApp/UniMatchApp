package com.ulpgc.uniMatch.ui.screens.core.events

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

    val surveys = event?.surveys

    Log.i("EventSurveys", "Id ${userId}")

    if (surveys != null) {
        if (userId != null) {
            SurveysList(
                userId = userId,
                surveys = surveys,
                isEditing = false,
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

}