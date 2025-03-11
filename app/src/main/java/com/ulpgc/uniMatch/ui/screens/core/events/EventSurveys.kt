package com.ulpgc.uniMatch.ui.screens.core.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.EventViewModel
import com.ulpgc.uniMatch.ui.components.event.survey.SurveysList

@Composable
fun EventSurveys(
    eventViewModel: EventViewModel
) {

    val event = eventViewModel.eventData.collectAsState().value

    val surveys = event?.surveys

    if (surveys != null) {
        SurveysList(
            surveys = surveys,
            isEditing = false)
    }

}