package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.MatchingEntity

class ApiMatchingService(
    private val matchingController: MatchingController,
    private val profileService: ProfileService,
    private val profileDao: ProfileDao
) : MatchingService {

    override suspend fun getMatchingUsers(userId: String, limit: Int): Result<List<Profile>> {
        return try {
            val response = matchingController.getMatchingUsers(userId, limit)

            if (!response.success) {
               Result.success(profileDao.getAllMatching().map(MatchingEntity::toDomain))
            } else {
                val userIds = response.value ?: emptyList()
                val profiles = userIds.mapNotNull { profileService.getProfile(it).getOrNull() }
                profiles.forEach {
                    val matchingEntity = MatchingEntity.fromDomain(it)
                    profileDao.insertMatching(matchingEntity)
                }
                Result.success(profiles)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun dislikeUser(userId: String, dislikedUserId: String): Result<Unit> {
        return try {
            val response = matchingController.dislikeUser(userId, dislikedUserId)

            if (!response.success) {
                Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
            } else {
                profileDao.deleteProfile(dislikedUserId)
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
                profileDao.deleteProfile(likedUserId)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
