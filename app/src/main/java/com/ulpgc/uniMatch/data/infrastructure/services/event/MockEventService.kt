package com.ulpgc.uniMatch.data.infrastructure.services.event

import android.net.Uri
import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.mocks.EventsMocks

class MockEventService : EventService {
    override suspend fun getAll(): Result<List<Event>> {
        return Result.success(
            EventsMocks.createMockEvents()
        )
    }

    override suspend fun getOne(id: String): Result<Event> {
        return Result.success(
            EventsMocks.createMockEvent()
        )
    }

    override suspend fun create(
        title: String,
        price: Double?,
        location: Location?,
        date: String,
        attachment: Uri,
        surveys: List<Survey>?
    ): Result<Event> {
        return Result.success(
            EventsMocks.createMockEvent()
        )
    }


    override suspend fun getEventsByName(filterNameEvent: String): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, event: Event): Result<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun participateEvent(id: String, userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeParticipation(id: String, userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun likeEvent(id: String, userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun dislikeEvent(id: String, userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun createSurvey(eventId: String, survey: Survey): Result<Survey> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSurvey(eventId: String, surveyTitle: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun editSurvey(eventId: String, survey: Survey): Result<Survey> {
        TODO("Not yet implemented")
    }

    override suspend fun selectSurvey(
        eventId: String,
        surveyTitle: String,
        option: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deselectSurvey(
        eventId: String,
        surveyTitle: String,
        option: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}