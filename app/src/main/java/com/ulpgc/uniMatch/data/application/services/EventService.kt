package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Survey

interface EventService {
    suspend fun getAll(): Result<List<Event>>
    suspend fun getOne(id: String): Result<Event>
    suspend fun create(event: Event): Result<Event>
    suspend fun update(id: String, event: Event): Result<Event>
    suspend fun delete(id: String): Result<Unit>
    suspend fun participateEvent(id: String, userId: String): Result<Unit>
    suspend fun removeParticipation(id: String, userId: String): Result<Unit>
    suspend fun likeEvent(id: String, userId: String): Result<Unit>
    suspend fun dislikeEvent(id: String, userId: String): Result<Unit>
    suspend fun createSurvey(eventId: String, survey: Survey): Result<Survey>
    suspend fun deleteSurvey(eventId: String, surveyTitle: String): Result<Unit>
    suspend fun editSurvey(eventId: String, survey: Survey): Result<Survey>
    suspend fun selectSurvey(eventId: String, surveyTitle: String, option: String): Result<Unit>
    suspend fun deselectSurvey(eventId: String, surveyTitle: String, option: String): Result<Unit>
}