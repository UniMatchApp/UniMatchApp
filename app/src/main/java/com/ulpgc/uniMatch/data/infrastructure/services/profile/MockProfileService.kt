package com.ulpgc.uniMatch.data.infrastructure.services.profile

import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Habits
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.Jobs
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.Religion
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
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

    override suspend fun updateGenderPriority(userId: String, gender: Gender?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateRelationshipType(
        userId: String,
        relationshipType: RelationshipType
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateAboutMe(userId: String, aboutMe: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateFact(userId: String, fact: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateInterests(userId: String, interests: List<String>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateHeight(userId: String, height: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateWeight(userId: String, weight: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateGender(userId: String, gender: Gender): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateSexualOrientation(
        userId: String,
        orientation: SexualOrientation
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateJob(userId: String, position: Jobs): Result<Unit> {
        return Result.success(Unit)
    }


    override suspend fun updateHoroscope(userId: String, horoscope: Horoscope): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateEducation(userId: String, education: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePersonalityType(
        userId: String,
        personalityType: String
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePets(userId: String, pets: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateDrinks(userId: String, drinks: Habits): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateSmokes(userId: String, smokes: Habits): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateDoesSports(userId: String, doesSports: Habits): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateValuesAndBeliefs(
        userId: String,
        valuesAndBeliefs: Religion
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun addInterest(userId: String, interest: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun removeInterest(userId: String, interest: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun addImage(userId: String, image: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun removeImage(userId: String, image: String): Result<Unit> {
        return Result.success(Unit)
    }
}