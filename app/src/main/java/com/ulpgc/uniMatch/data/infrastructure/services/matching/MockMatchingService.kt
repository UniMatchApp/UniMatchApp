package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock

class MockMatchingService: MatchingService {
    override suspend fun getMatchingUsers(userId: String, limit: Int): Result<List<Profile>> {
        return Result.success(
            ProfileMock.createNamedProfiles()
        )
    }

    override suspend fun dislikeUser(userId: String, dislikedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun likeUser(userId: String, likedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }
}