package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.Profile


interface MatchingService {
    suspend fun getMatchingUsers(limit: Int): Result<List<Profile>>
    suspend fun dislikeUser(dislikedUserId: String): Result<Unit>
    suspend fun likeUser(likedUserId: String): Result<Unit>
}