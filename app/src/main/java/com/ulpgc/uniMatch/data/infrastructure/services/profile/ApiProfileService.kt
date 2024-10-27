package com.ulpgc.uniMatch.data.infrastructure.services.profile

import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity

class ApiProfileService (
    private val profileController: ProfileController,
    private val profileDao: ProfileDao
) : ProfileService {

    override suspend fun getProfile(userId: String): Result<Profile> {
        return try {
            val profileEntity = profileDao.getProfileById(userId)
            val profile = ProfileEntity.toDomain(profileEntity)
            Result.success(profile)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }


}

