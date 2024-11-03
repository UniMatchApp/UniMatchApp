package com.ulpgc.uniMatch.data.infrastructure.services.profile

import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock

class MockProfileService: ProfileService {
    override suspend fun getProfile(userId: String): Result<Profile> {
        return Result.success(
            ProfileMock.createMockProfile()
        )
    }

    override suspend fun updateAgeRange(userId: String, min: Int, max: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateGenderPriority(userId: String, gender: Gender): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateRelationshipType(
        userId: String,
        relationshipType: RelationshipType
    ): Result<Unit> {
        return Result.success(Unit)
    }
}