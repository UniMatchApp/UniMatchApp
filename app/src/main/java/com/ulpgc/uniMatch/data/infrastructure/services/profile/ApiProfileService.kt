package com.ulpgc.uniMatch.data.infrastructure.services.profile

import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity

class ApiProfileService (
    private val profileController: ProfileController,
    private val profileDao: ProfileDao
) : ProfileService {

    override suspend fun getProfile(userId: String): Result<Profile> {
        return try {
            val profileEntity = profileDao.getProfileById(userId)
            val profile = ProfileEntity.toDomain(profileEntity)
            Result.success(profile)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun updateAgeRange(userId: String, min: Int, max: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGenderPriority(userId: String, gender: Gender?): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateRelationshipType(
        userId: String,
        relationshipType: RelationshipType
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAboutMe(userId: String, aboutMe: String): Result<Unit> {
        return try {
            // profileController.updateAboutMe(userId, aboutMe) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun updateFact(userId: String, fact: String): Result<Unit> {
        return try {
            // profileController.updateFact(userId, fact) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun updateInterests(userId: String, interests: List<String>): Result<Unit> {
        return try {
            // profileController.updateInterests(userId, interests) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun updateHeight(userId: String, height: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWeight(userId: String, weight: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGender(userId: String, gender: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSexualOrientation(
        userId: String,
        orientation: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateJob(userId: String, position: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateRelationshipType(
        userId: String,
        relationshipType: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHoroscope(userId: String, horoscope: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateEducation(userId: String, education: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePersonalityType(
        userId: String,
        personalityType: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePets(userId: String, pets: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDrinks(userId: String, drinks: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSmokes(userId: String, smokes: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDoesSports(userId: String, doesSports: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateValuesAndBeliefs(
        userId: String,
        valuesAndBeliefs: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addInterest(userId: String, interest: String): Result<Unit> {
        return try {
            // profileController.addInterest(userId, interest) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun removeInterest(userId: String, interest: String): Result<Unit> {
        return try {
            // profileController.removeInterest(userId, interest) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun addImage(userId: String, image: String): Result<Unit> {
        return try {
            // profileController.addImage(userId, image) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun removeImage(userId: String, image: String): Result<Unit> {
        return try {
            // profileController.removeImage(userId, image) TODO
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

}

