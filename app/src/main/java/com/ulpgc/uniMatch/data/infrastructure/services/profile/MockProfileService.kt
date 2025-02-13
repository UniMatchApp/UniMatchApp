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
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MockProfileService : ProfileService {
    override suspend fun getProfile(userId: String): Result<Profile> {
        return Result.success(
            ProfileMock.createMockProfile()
        )
    }

    override suspend fun updateAgeRange(min: Int, max: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateMaxDistance(distance: Int): Result<Int> {
        return Result.success(
            20000
        )
    }

    override suspend fun updateGenderPriority(gender: Gender?): Result<Gender?> {
        return Result.success(gender)
    }

    override suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<RelationshipType> {
        return Result.success(relationshipType)
    }

    override suspend fun updateAboutMe(aboutMe: String): Result<String> {
        return Result.success(aboutMe)
    }

    override suspend fun updateFact(fact: String?): Result<String> {
        return Result.success("mock_fact")
    }

    override suspend fun updateInterests(interests: List<String>): Result<List<String>> {
        return Result.success(listOf("mock_interest1", "mock_interest2"))
    }

    override suspend fun updateHeight(height: Int?): Result<Int> {
        return Result.success(20000)
    }

    override suspend fun updateWeight(weight: Int?): Result<Int> {
        return Result.success(20000)
    }

    override suspend fun updateGender(gender: Gender): Result<Gender> {
        return Result.success(gender)
    }

    override suspend fun updateSexualOrientation(
        orientation: SexualOrientation
    ): Result<SexualOrientation> {
        return Result.success(orientation)
    }

    override suspend fun updateJob(position: String?): Result<String?> {
        return Result.success("mock_job")
    }


    override suspend fun updateHoroscope(horoscope: Horoscope?): Result<Horoscope?> {
        return Result.success(horoscope)
    }

    override suspend fun updateEducation(education: String?): Result<String> {
        return Result.success("mock_education")
    }

    override suspend fun updatePersonalityType(
        personalityType: String?
    ): Result<String?> {
        return Result.success("mock_personality")
    }

    override suspend fun updatePets(pets: String?): Result<String?> {
        return Result.success("mock_pets")
    }

    override suspend fun updateDrinks(drinks: Habits?): Result<Habits?> {
        return Result.success(drinks)
    }

    override suspend fun updateSmokes(smokes: Habits?): Result<Habits?> {
        return Result.success(smokes)
    }

    override suspend fun updateDoesSports(doesSports: Habits?): Result<Habits?> {
        return Result.success(doesSports)
    }

    override suspend fun updateValuesAndBeliefs(valuesAndBeliefs: Religion?): Result<Religion?> {
        return Result.success(valuesAndBeliefs)
    }

    override suspend fun updateLocation(location: Profile.Location?): Result<Profile.Location> {
        return Result.success(Profile.Location(0.0, 0.0, 0.0))
    }

    override suspend fun addImage(imageURI: Uri): Result<String> {
        return Result.success(imageURI.toString())
    }

    override suspend fun removeImage(imageURL: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateWall(wall: List<String>): Result<List<String>> {
        return Result.success(listOf("mock_wall1", "mock_wall2"))
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
            safeRequest {
                return@safeRequest ProfileMock.createMockProfile()
            }
        }
    }
}