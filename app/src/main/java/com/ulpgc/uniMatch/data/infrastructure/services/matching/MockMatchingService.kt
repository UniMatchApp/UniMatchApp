package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock

class MockMatchingService: MatchingService {
    override suspend fun getMatchingUsers(limit: Int): Result<List<Profile>> {
        return Result.success(
            ProfileMock.createNamedProfiles().take(limit)
        )
    }

    override suspend fun dislikeUser(dislikedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun likeUser(likedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }
}