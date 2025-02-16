package com.ulpgc.uniMatch.data.infrastructure.services.event

import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.controllers.EventController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.EventDao

class ApiEventService(
    private val eventController: EventController,
    private val eventDao: EventDao
) : EventService {
    override suspend fun getAll(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOne(id: String): Result<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun create(event: Event): Result<Event> {
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