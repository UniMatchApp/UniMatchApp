package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockMatchingService: MatchingService {

    override suspend fun getMatchingUsers(limit: Int): Flow<Profile> {
        return flow {
            val profiles = ProfileMock.createNamedProfiles().take(limit)
            for (profile in profiles) {
                emit(profile)
            }
        }
    }


    override suspend fun dislikeUser(dislikedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun likeUser(likedUserId: String): Result<Unit> {
        return Result.success(Unit)
    }
}