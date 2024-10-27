package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController

class ApiMatchingService(
    private val matchingController: MatchingController
) : MatchingService {

    override suspend fun getMatchingUsers(userId: String, limit: Int): Result<List<Profile>> {
        return try {
            val response = matchingController.getMatchingUsers(userId, limit)

            if (!response.success) {
               Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
            } else {
                Result.success(response.value ?: emptyList())
            }
        } catch (e: Exception) {
            // Manejo de errores
            Result.failure(e)
        }
    }

    override suspend fun dislikeUser(userId: String, dislikedUserId: String): Result<Unit> {
        return try {
            val response = matchingController.dislikeUser(userId, dislikedUserId)

            if (!response.success) {
                Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun likeUser(userId: String, likedUserId: String): Result<Unit> {
        return try {
            val response = matchingController.likeUser(userId, likedUserId)

            if (!response.success) {
                Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
