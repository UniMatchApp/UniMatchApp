package com.ulpgc.uniMatch.data.infrastructure.services.profile

import android.net.http.HttpException
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        return handleApiCall { profileController.updateAgeRange(userId, min, max) }
    }

    override suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit> {
        return handleApiCall { profileController.updateMaxDistance(userId, distance) }
    }

    override suspend fun updateGenderPriority(userId: String, gender: Gender?): Result<Unit> {
        return handleApiCall { profileController.updateGenderPriority(userId, gender) }
    }

    override suspend fun updateRelationshipType(
        userId: String,
        relationshipType: RelationshipType
    ): Result<Unit> {
        return handleApiCall { profileController.updateRelationshipType(userId, relationshipType) }
    }

    override suspend fun updateAboutMe(userId: String, aboutMe: String): Result<Unit> {
        return handleApiCall { profileController.updateAbout(userId, aboutMe) }
    }

    override suspend fun updateFact(userId: String, fact: String): Result<Unit> {
        return handleApiCall { profileController.updateFact(userId, fact) }
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
        return handleApiCall { profileController.updateHeight(userId, height) }
    }

    override suspend fun updateWeight(userId: String, weight: Int): Result<Unit> {
        return handleApiCall { profileController.updateWeight(userId, weight) }
    }

    override suspend fun updateGender(userId: String, gender: Gender): Result<Unit> {
        return handleApiCall { profileController.updateGender(userId, gender) }
    }

    override suspend fun updateSexualOrientation(
        userId: String,
        orientation: SexualOrientation
    ): Result<Unit> {
        return handleApiCall { profileController.updateSexualOrientation(userId, orientation) }
    }

    override suspend fun updateJob(userId: String, position: String): Result<Unit> {
        return handleApiCall { profileController.updateJob(userId, position) }
    }

    override suspend fun updateHoroscope(userId: String, horoscope: Horoscope): Result<Unit> {
        return handleApiCall { profileController.updateHoroscope(userId, horoscope) }
    }

    override suspend fun updateEducation(userId: String, education: String): Result<Unit> {
        return handleApiCall { profileController.updateDegree(userId, education) }
    }

    override suspend fun updatePersonalityType(
        userId: String,
        personalityType: String
    ): Result<Unit> {
        return handleApiCall { profileController.updatePersonality(userId, personalityType) }
    }

    override suspend fun updatePets(userId: String, pets: String): Result<Unit> {
        return handleApiCall { profileController.updatePets(userId, pets) }
    }

    override suspend fun updateDrinks(userId: String, drinks: Habits): Result<Unit> {
        return handleApiCall { profileController.updateDrinks(userId, drinks) }
    }

    override suspend fun updateSmokes(userId: String, smokes: Habits): Result<Unit> {
        return handleApiCall { profileController.updateSmokes(userId, smokes) }
    }

    override suspend fun updateDoesSports(userId: String, doesSports: Habits): Result<Unit> {
        return handleApiCall { profileController.updateSports(userId, doesSports) }
    }

    override suspend fun updateValuesAndBeliefs(
        userId: String,
        valuesAndBeliefs: Religion
    ): Result<Unit> {
        return handleApiCall { profileController.updateValuesAndBeliefs(userId, valuesAndBeliefs) }
    }

    override suspend fun addInterest(userId: String, interest: String): Result<Unit> {
       return handleApiCall { profileController.addInterest(userId, interest) }
    }

    override suspend fun removeInterest(userId: String, interest: String): Result<Unit> {
        return handleApiCall { profileController.removeInterest(userId, interest) }
    }

    override suspend fun addImage(userId: String, image: String): Result<Unit> {
        return handleApiCall { profileController.uploadPhoto(userId, image) }
    }

    override suspend fun removeImage(userId: String, image: String): Result<Unit> {
        return handleApiCall { profileController.deletePhoto(userId, image) }
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
        profileImageUri: String?
    ): Result<Profile> {
        return withContext(Dispatchers.IO) {
            try {
                val response = profileController.createProfile(
                    userId,
                    fullName,
                    age,
                    aboutMe,
                    gender.toString(),
                    sexualOrientation.toString(),
                    relationshipType.toString(),
                    birthday,
                    location,
                    profileImageUri
                )
                if (response.success) {
                    Result.success(response.value!!)
                } else {
                    Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
                }
            } catch (e: Exception) {
                Result.failure(Throwable("Failed to create profile: ${e.message}"))
            }
        }
    }

    private suspend fun <T> handleApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            val response = apiCall()
            Result.success(response)
        } catch (e: Exception) {
            when (e) {
                is java.net.UnknownHostException -> Result.failure(Throwable("No se pudo conectar con el servidor"))
                is java.net.SocketTimeoutException -> Result.failure(Throwable("La solicitud ha tardado demasiado"))
                is retrofit2.HttpException -> Result.failure(Throwable("Error en la respuesta de la API: ${e.message}"))
                else -> Result.failure(Throwable("Error desconocido: ${e.localizedMessage}"))
            }
        }
    }

}

