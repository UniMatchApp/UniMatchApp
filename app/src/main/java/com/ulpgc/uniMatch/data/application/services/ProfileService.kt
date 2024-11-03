package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.models.Profile

interface ProfileService {
    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun updateAgeRange(userId: String, min: Int, max: Int): Result<Unit>
    suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit>
    suspend fun updateGenderPriority(userId: String, gender: Gender): Result<Unit>
    suspend fun updateRelationshipType(userId: String, relationshipType: RelationshipType): Result<Unit>
}