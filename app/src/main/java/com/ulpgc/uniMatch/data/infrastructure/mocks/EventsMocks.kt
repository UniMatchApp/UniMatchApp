package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.domain.models.User
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
            location = Profile.Location(
                latitude = 37.7749,
                longitude = -122.4194,
                altitude = 0.0
            ),
            date = Date(2025 - 1900, 1, 25), // February 25, 2025 (adjust for Date constructor)
            ownerId = "user567",
            participants = listOf("user123", "user234", "user345"),
            likes = listOf("user567", "user123"),
            attachment = "https://lasterrazasoutlet.com/wp-content/uploads/2025/01/56o-Imagen-2-580x390.jpg",
            surveys = listOf(createMockSurvey())
        )
    }

    fun createMockSurvey(): Survey {
        return Survey(
            title = "Event Feedback",
            options = mapOf(
                "How did you hear about the event?" to setOf("Social Media", "Friend", "Email"),
                "Was the event useful?" to setOf("Yes", "No", "Not sure")
            )
        )
    }
}