package com.ulpgc.uniMatch.data.infrastructure.services.event

import android.content.ContentResolver
import android.net.Uri
import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.controllers.EventController
import com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers.ListRequest
import com.ulpgc.uniMatch.data.infrastructure.database.dao.EventDao
import com.ulpgc.uniMatch.data.infrastructure.entities.EventEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ApiEventService(
    private val eventController: EventController,
    private val eventDao: EventDao,
    private val contentResolver: ContentResolver
) : EventService {
    override suspend fun getAll(): Result<List<Event>> {
        return safeRequest {
            val response = eventController.getEvents()

            if(!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            eventDao.deleteAllEvents()

            /// Mirar si hace bien el todomainmodel
            val events = response.value?.map { it.toDomainModel() }

            if (events != null) {
                eventDao.insertEvents(events.map(EventEntity::fromDomain))
            }

            return@safeRequest events ?: emptyList()
        }

    }

    override suspend fun getEventsByName(
        filterNameEvent : String
    ): Result<List<Event>?> {
        return safeRequest {
            val eventEntities = eventDao.getEventsByName(filterNameEvent)

            val filterEvents = eventEntities.map(EventEntity::toDomain)
            return@safeRequest filterEvents ?: emptyList()
        }
    }

    override suspend fun getOne(id: String): Result<Event> {
        return safeRequest {
            val response = eventController.getEventById(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            if (response.value == null) {
                throw Exception("Event not found")
            }

            val event = response.value.toDomainModel()

            return@safeRequest event
        }
    }

    private fun createRequestBody(value: String): RequestBody =
        value.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun createImagePart(uri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open image input stream")
        val requestBody =
            inputStream.use { it.readBytes().toRequestBody("image/png".toMediaTypeOrNull()) }
        return MultipartBody.Part.createFormData("thumbnail", uri.lastPathSegment, requestBody)
    }

    override suspend fun create(
        title: String,
        price: Double,
        location: Location,
        date: String,
        attachment: Uri,
        surveys: List<Survey>
    ): Result<Event> {
        return safeRequest {
            val response = eventController.createEvent(
                title = createRequestBody(title),
                price = createRequestBody(price.toString()),
                latitude = location.latitude,
                longitude = location.longitude,
                date = createRequestBody(date.toString()),
                attachment = createImagePart(attachment),
                surveys = ListRequest(surveys.map { it.title })
            )

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            val createdEvent = response.value?.toDomainModel()

            if (createdEvent != null) {
                eventDao.insertEvent(EventEntity.fromDomain(createdEvent))
            }

            return@safeRequest createdEvent ?: throw Exception("Event not created")
        }
    }

    override suspend fun update(id: String, event: Event): Result<Event> {
        TODO()
    }

    override suspend fun delete(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun participateEvent(id: String, userId: String): Result<Unit> {
        return safeRequest {
            val response = eventController.participateEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
    }

    override suspend fun removeParticipation(id: String, userId: String): Result<Unit> {
        return safeRequest {
            val response = eventController.unparticipateEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
    }

    override suspend fun likeEvent(id: String, userId: String): Result<Unit> {
        return safeRequest {
            val response = eventController.likeEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
    }

    override suspend fun dislikeEvent(id: String, userId: String): Result<Unit> {
        return safeRequest {
            val response = eventController.unlikeEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
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