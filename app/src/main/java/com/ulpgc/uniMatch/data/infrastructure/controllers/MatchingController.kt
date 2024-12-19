package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MatchingController {

    @GET("matching/mutual-likes")
    suspend fun getMatchingUserIds(): ApiResponse<List<String>>

    @GET("matching/potential-matches/{limit}")
    suspend fun getMatchingUsers(
        @Path("limit") limit: Int
    ): ApiResponse<List<String>>

    @POST("matching/like/{likedUserId}")
    suspend fun likeUser(
        @Path("likedUserId") likedUserId: String
    ): ApiResponse<Unit>

    @POST("matching/dislike/{dislikedUserId}")
    suspend fun dislikeUser(
        @Path("dislikedUserId") dislikedUserId: String
    ): ApiResponse<Unit>
}