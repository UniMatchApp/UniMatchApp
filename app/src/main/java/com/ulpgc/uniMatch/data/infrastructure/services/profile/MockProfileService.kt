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

    override suspend fun updateAgeRange(min: Int, max: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateMaxDistance(distance: Int): Result<Int> {
        return Result.success(distance)
    }

    override suspend fun updateGenderPriority(gender: Gender?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateAboutMe(aboutMe: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateFact(fact: String?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateInterests(interests: List<String>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateHeight(height: Int?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateWeight(weight: Int?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateGender(gender: Gender): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateSexualOrientation(
        orientation: SexualOrientation
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateJob(position: String?): Result<Unit> {
        return Result.success(Unit)
    }


    override suspend fun updateHoroscope(horoscope: Horoscope?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateEducation(education: String?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePersonalityType(
        personalityType: String?
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePets(pets: String?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateDrinks(drinks: Habits?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateSmokes(smokes: Habits?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateDoesSports(doesSports: Habits?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateValuesAndBeliefs(
        valuesAndBeliefs: Religion?
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateLocation(location: Profile.Location?): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun addImage(imageURI: Uri): Result<String> {
        return Result.success("")
    }

    override suspend fun removeImage(imageURL: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateWall(wall: List<String>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun createProfile(
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