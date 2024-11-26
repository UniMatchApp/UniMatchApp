package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.MatchingEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest

class ApiMatchingService(
    private val matchingController: MatchingController,
    private val profileService: ProfileService,
    private val profileDao: ProfileDao
) : MatchingService {

    override suspend fun getMatchingUsers(limit: Int): Result<List<Profile>> {
        return safeRequest {
            val response = matchingController.getMatchingUsers(limit)

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
        }
    }

    override suspend fun dislikeUser(dislikedUserId: String): Result<Unit> {
        return safeRequest {
            val response = matchingController.dislikeUser(dislikedUserId)

            if (!response.success) {
                Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
            } else {
                profileDao.deleteProfile(dislikedUserId)
                Result.success(Unit)
            }
        }
    }

    override suspend fun likeUser(likedUserId: String): Result<Unit> {
        return safeRequest {
            val response = matchingController.likeUser(likedUserId)

            if (!response.success) {
                Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
            } else {
                profileDao.deleteProfile(likedUserId)
                Result.success(Unit)
            }
        }
    }
}
