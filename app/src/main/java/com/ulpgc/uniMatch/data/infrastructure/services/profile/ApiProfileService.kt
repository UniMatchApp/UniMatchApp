package com.ulpgc.uniMatch.data.infrastructure.services.profile

import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
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

    override suspend fun updateAgeRange(userId: String, min: Int, max: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGenderPriority(userId: String, gender: Gender): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateRelationshipType(
        userId: String,
        relationshipType: RelationshipType
    ): Result<Unit> {
        TODO("Not yet implemented")
    }


}

