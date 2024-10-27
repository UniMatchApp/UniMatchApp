package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.Profile


interface MatchingService {
    suspend fun getMatchingUsers(userId: String, limit: Int): Result<List<Profile>>
    suspend fun dislikeUser(userId: String, dislikedUserId: String): Result<Unit>
    suspend fun likeUser(userId: String, likedUserId: String): Result<Unit>
}