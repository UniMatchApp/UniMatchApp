package com.ulpgc.uniMatch.data.infrastructure.mocks

import androidx.compose.ui.res.stringResource
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import java.util.Date

object EventsMocks {


    fun createMockEvents(): List<Event> {
        return listOf(
            createMockEvent(),
            createMockEvent(),
            createMockEvent()
        )
    }

    fun createMockEvent(): Event {
        return Event(
            eventId = "event123",
            title = "Tech Conference 2025",
            price = 150.0,
            location = Location(
                latitude = 37.7749,
                longitude = -122.4194,
                altitude = 0.0
            ),
            date = Date(2025 - 1900, 1, 25), // February 25, 2025 (adjust for Date constructor)
            ownerId = "user567",
            participants = listOf("User123", "User234", "User345"),
            likes = listOf("user567", "user123"),
            attachment = "https://imagenes2.fotos.europapress.es/preview/5309176.jpg?s=1000",
            surveys = listOf(createMockSurvey())
        )
    }

    fun createMockSurvey(): Survey {
        return Survey(
            title = "Event Feedback",
            options = mapOf(
                "Opción 1" to setOf(),
                "Opción 2" to setOf())
        )
    }
}