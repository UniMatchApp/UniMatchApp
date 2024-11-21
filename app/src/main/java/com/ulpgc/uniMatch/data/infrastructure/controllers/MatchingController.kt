package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MatchingController {

    @GET("matching/potential-matches/{userId}/{limit}")
    suspend fun getMatchingUsers(
        @Path("userId") userId: String,
        @Path("limit") limit: Int
    ): ApiResponse<List<String>>

    @POST("matching/like/{userId}/{likedUserId}")
    suspend fun likeUser(
        @Path("userId") userId: String,
        @Path("likedUserId") likedUserId: String
    ): ApiResponse<Unit>

    @POST("matching/dislike/{userId}/{dislikedUserId}")
    suspend fun dislikeUser(
        @Path("userId") userId: String,
        @Path("dislikedUserId") dislikedUserId: String
    ): ApiResponse<Unit>
}