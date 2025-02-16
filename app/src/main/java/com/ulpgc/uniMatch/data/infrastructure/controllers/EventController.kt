package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.models.Event
import retrofit2.http.GET
import retrofit2.http.POST

interface EventController {

    @GET("events")
    suspend fun getEvents(): ApiResponse<List<Event>>

    @GET("events/{eventId}")
    suspend fun getEventById(eventId: String): ApiResponse<Event>

    @POST("events")
    suspend fun createEvent(event: Event): ApiResponse<Event>

}