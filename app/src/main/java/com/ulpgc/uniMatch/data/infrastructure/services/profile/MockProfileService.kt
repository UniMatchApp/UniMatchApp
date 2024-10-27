package com.ulpgc.uniMatch.data.infrastructure.services.profile

import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock

class MockProfileService: ProfileService {
    override suspend fun getProfile(userId: String): Result<Profile> {
        return Result.success(
            ProfileMock.createMockProfile()
        )
    }
}