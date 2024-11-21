package com.ulpgc.uniMatch.data.infrastructure.services.profile

import android.net.Uri
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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

    override suspend fun updateFact(userId: String, fact: String?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateInterests(userId: String, interests: List<String>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateHeight(userId: String, height: Int?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateWeight(userId: String, weight: Int?): Result<Unit> {
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

    override suspend fun updateJob(userId: String, position: String?): Result<Unit> {
        return Result.success(Unit)
    }


    override suspend fun updateHoroscope(userId: String, horoscope: Horoscope?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateEducation(userId: String, education: String?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePersonalityType(
        userId: String,
        personalityType: String?
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePets(userId: String, pets: String?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateDrinks(userId: String, drinks: Habits?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateSmokes(userId: String, smokes: Habits?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateDoesSports(userId: String, doesSports: Habits?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateValuesAndBeliefs(
        userId: String,
        valuesAndBeliefs: Religion?
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun addImage(userId: String, image: Uri): Result<String> {
        return Result.success("")
    }

    override suspend fun removeImage(userId: String, image: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun createProfile(
        userId: String,
        fullName: String,
        age: Int,
        aboutMe: String,
        gender: Gender,
        sexualOrientation: SexualOrientation,
        relationshipType: RelationshipType,
        birthday: String,
        location: Pair<Double, Double>?,
        profileImage: Uri
    ): Result<Profile> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    ProfileMock.createMockProfile()
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}