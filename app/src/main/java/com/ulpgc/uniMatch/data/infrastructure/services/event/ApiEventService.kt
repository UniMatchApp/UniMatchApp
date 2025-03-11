package com.ulpgc.uniMatch.data.infrastructure.services.event

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.ulpgc.uniMatch.data.application.services.EventService
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey
import com.ulpgc.uniMatch.data.infrastructure.controllers.EventController
import com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers.AgeRangeRequest
import com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers.CreateSurveyDTO
import com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers.ListRequest
import com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers.SurveyRequest
import com.ulpgc.uniMatch.data.infrastructure.database.dao.EventDao
import com.ulpgc.uniMatch.data.infrastructure.entities.EventEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeApiCall
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

            Log.i("ApiService", "${response}")
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
        // Obtener el tipo MIME del archivo
       Log.i("CreateImagePart", "URI: $uri ${contentResolver.getType(uri)}")
        val mimeType = contentResolver.getType(uri) ?: "image/*"

        // Determinar la extensión del archivo basado en el tipo MIME
        val fileExtension = when (mimeType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            "image/webp" -> "webp"
            else -> "txt" // Extensión por defecto si no se reconoce el tipo MIME
        }

        Log.i("CreateImagePart", "Fileextension: $fileExtension con mimetype: $mimeType")

        // Leer el archivo y convertirlo en un RequestBody
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open image input stream")
        val requestBody = inputStream.use {
            it.readBytes().toRequestBody(mimeType.toMediaTypeOrNull()) // Usar el tipo MIME correcto
        }
        Log.i("CreateImagePart", "RequestBody: $requestBody")

        // Crear la parte MultipartBody.Part
        return MultipartBody.Part.createFormData(
            "thumbnail", // Nombre del campo en la solicitud
            "thumbnail.$fileExtension", // Nombre del archivo con la extensión correcta
            requestBody
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
        return safeRequest {
            val surveyRequest = surveys?.map { survey ->
                CreateSurveyDTO(
                    title = survey.title,
                    options = survey.options.keys.toList()
                )
            }?.let { SurveyRequest(it) }

            val response = surveyRequest?.let {
                eventController.createEvent(
                    title = createRequestBody(title),
                    price = createRequestBody(price.toString()),
                    latitude = location?.latitude,
                    longitude = location?.longitude,
                    date = createRequestBody(date),
                    attachment = createImagePart(attachment),
                    surveys = it
                )
            }

            Log.i("ApiService", "${response}")

            if (response != null) {
                if (!response.success) {
                    throw Exception(response.errorMessage ?: "Unknown error occurred")
                }
            }

            val createdEvent = response?.value?.toDomainModel()

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
        return safeRequest {
            val response = eventController.delete(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
    }


    override suspend fun participateEvent(id: String): Result<Unit> {
        return safeRequest {
            val response = eventController.participateEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
    }

    override suspend fun removeParticipation(id: String): Result<Unit> {
        return safeRequest {
            val response = eventController.unparticipateEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            return@safeRequest
        }
    }

    override suspend fun likeEvent(id: String): Result<Unit> {
        return safeRequest {
            val response = eventController.likeEvent(id)

            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }
            return@safeRequest
        }
    }

    override suspend fun dislikeEvent(id: String): Result<Unit> {
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
    ): Result<Unit> =
        safeApiCall { eventController.selectSurvey(eventId, surveyTitle, option) }.mapCatching {
            Unit
        }


    override suspend fun deselectSurvey(
        eventId: String,
        surveyTitle: String,
        option: String
    ): Result<Unit> =
        safeApiCall { eventController.deselectSurvey(eventId, surveyTitle, option) }.mapCatching {
            Unit
        }
}