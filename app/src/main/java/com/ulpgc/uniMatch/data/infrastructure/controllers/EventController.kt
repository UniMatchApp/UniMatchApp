package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.Event
import com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers.ListRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface EventController {

    @GET("events")
    suspend fun getEvents(): ApiResponse<List<Event>>

    @GET("events/{eventId}")
    suspend fun getEventById(@Path("eventId") eventId: String): ApiResponse<Event>

    @POST("events")
    suspend fun createEvent(
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("latitude") latitude: Double?,
        @Part("longitude") longitude: Double?,
        @Part("date") date: RequestBody,
        @Part attachment: MultipartBody.Part,
        @Part("surveys") surveys: ListRequest
    ): ApiResponse<Event>

    @POST("events/{eventId}/like")
    suspend fun likeEvent(@Path("eventId") eventId: String): ApiResponse<Event>

    @POST("events/{eventId}/participate")
    suspend fun participateEvent(@Path("eventId") eventId: String): ApiResponse<Event>

    @DELETE("events/{eventId}/like")
    suspend fun unlikeEvent(@Path("eventId") eventId: String): ApiResponse<Event>

    @DELETE("events/{eventId}/participate")
    suspend fun unparticipateEvent(@Path("eventId") eventId: String): ApiResponse<Event>
}