package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.models.Profile

interface ProfileService {
    suspend fun getProfile(userId: String): Result<Profile>

}