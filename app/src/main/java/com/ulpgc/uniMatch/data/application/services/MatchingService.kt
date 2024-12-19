package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.flow.Flow


interface MatchingService {
    suspend fun getMatchingUsers(limit: Int): Flow<Profile>
    suspend fun dislikeUser(dislikedUserId: String): Result<Unit>
    suspend fun likeUser(likedUserId: String): Result<Unit>
}