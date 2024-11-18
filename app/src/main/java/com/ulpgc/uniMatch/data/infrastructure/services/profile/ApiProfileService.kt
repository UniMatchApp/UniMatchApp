package com.ulpgc.uniMatch.data.infrastructure.services.profile

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.*
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class ApiProfileService (
    private val profileController: ProfileController,
    private val profileDao: ProfileDao,
    private val contentResolver: ContentResolver
) : ProfileService {

    override suspend fun getProfile(userId: String): Result<Profile> {
        return try {
            var profileEntity = profileDao.getProfileById(userId)
            val updatedProfileEntity = profileController.getProfile(userId)

            Log.i("ApiProfileService", "Profile: $updatedProfileEntity.value)")
            Log.i("ApiProfileService", "Profile updated: $profileEntity")
            if (updatedProfileEntity.success && updatedProfileEntity.value != null) {
                profileEntity = ProfileEntity.fromDomain(Profile.fromDTO(updatedProfileEntity.value))
                profileDao.insertProfile(profileEntity)
            }

            if (profileEntity == null) {
                return Result.failure(Throwable("Profile for user $userId not found"))
            }

            Result.success(ProfileEntity.toDomain(profileEntity))
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun updateAgeRange(userId: String, min: Int, max: Int): Result<Unit> =
        handleApiCall { profileController.updateAgeRange(userId, min, max) }

    override suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit> =
        handleApiCall { profileController.updateMaxDistance(userId, distance) }

    override suspend fun updateGenderPriority(userId: String, gender: Gender?): Result<Unit> =
        handleApiCall { profileController.updateGenderPriority(userId, gender) }

    override suspend fun updateRelationshipType(userId: String, relationshipType: RelationshipType): Result<Unit> =
        handleApiCall { profileController.updateRelationshipType(userId, relationshipType) }

    override suspend fun updateAboutMe(userId: String, aboutMe: String): Result<Unit> =
        handleApiCall { profileController.updateAbout(userId, aboutMe) }

    override suspend fun updateFact(userId: String, fact: String): Result<Unit> =
        handleApiCall { profileController.updateFact(userId, fact) }

    override suspend fun updateInterests(userId: String, interests: List<String>): Result<Unit> =
        handleApiCall { profileController.updateInterests(userId, interests) }

    override suspend fun updateHeight(userId: String, height: Int): Result<Unit> =
        handleApiCall { profileController.updateHeight(userId, height) }

    override suspend fun updateWeight(userId: String, weight: Int): Result<Unit> =
        handleApiCall { profileController.updateWeight(userId, weight) }

    override suspend fun updateGender(userId: String, gender: Gender): Result<Unit> =
        handleApiCall { profileController.updateGender(userId, gender) }

    override suspend fun updateSexualOrientation(userId: String, orientation: SexualOrientation): Result<Unit> =
        handleApiCall { profileController.updateSexualOrientation(userId, orientation) }

    override suspend fun updateJob(userId: String, position: String): Result<Unit> =
        handleApiCall { profileController.updateJob(userId, position) }

    override suspend fun updateHoroscope(userId: String, horoscope: Horoscope): Result<Unit> =
        handleApiCall { profileController.updateHoroscope(userId, horoscope) }

    override suspend fun updateEducation(userId: String, education: String): Result<Unit> =
        handleApiCall { profileController.updateDegree(userId, education) }

    override suspend fun updatePersonalityType(userId: String, personalityType: String): Result<Unit> =
        handleApiCall { profileController.updatePersonality(userId, personalityType) }

    override suspend fun updatePets(userId: String, pets: String): Result<Unit> =
        handleApiCall { profileController.updatePets(userId, pets) }

    override suspend fun updateDrinks(userId: String, drinks: Habits): Result<Unit> =
        handleApiCall { profileController.updateDrinks(userId, drinks) }

    override suspend fun updateSmokes(userId: String, smokes: Habits): Result<Unit> =
        handleApiCall { profileController.updateSmokes(userId, smokes) }

    override suspend fun updateDoesSports(userId: String, doesSports: Habits): Result<Unit> =
        handleApiCall { profileController.updateSports(userId, doesSports) }

    override suspend fun updateValuesAndBeliefs(userId: String, valuesAndBeliefs: Religion): Result<Unit> =
        handleApiCall { profileController.updateValuesAndBeliefs(userId, valuesAndBeliefs) }

    override suspend fun removeInterest(userId: String, interest: String): Result<Unit> =
        handleApiCall { profileController.removeInterest(userId, interest) }

    override suspend fun addImage(userId: String, image: String): Result<Unit> =
        handleApiCall { profileController.uploadPhoto(userId, image) }

    override suspend fun removeImage(userId: String, image: String): Result<Unit> =
        handleApiCall { profileController.deletePhoto(userId, image) }

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
    ): Result<Profile> = withContext(Dispatchers.IO) {
        try {
            val fullNameRequest = createRequestBody(fullName)
            val ageRequest = createRequestBody(age.toString())
            val aboutMeRequest = createRequestBody(aboutMe)
            val genderRequest = createRequestBody(gender.toString())
            val sexualOrientationRequest = createRequestBody(sexualOrientation.toString())
            val relationshipTypeRequest = createRequestBody(relationshipType.toString())
            val birthdayRequest = createRequestBody(birthday)
            val locationRequest = location?.let { createRequestBody("${it.first},${it.second}") }

            val imageRequest = createImagePart(profileImage)

            val request = handleApiCall {
                profileController.createProfile(
                    userId,
                    fullNameRequest,
                    ageRequest,
                    aboutMeRequest,
                    genderRequest,
                    sexualOrientationRequest,
                    relationshipTypeRequest,
                    birthdayRequest,
                    locationRequest,
                    imageRequest,
                )
            }

            return@withContext handleProfileCreationResult(request)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create profile: ${e.message}", e))
        }
    }

    private suspend fun <T> handleApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            val response = apiCall()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create profile: ${e.message}", e))
        }
    }

    private fun handleProfileCreationResult(request: Result<ApiResponse<Profile>>): Result<Profile> {
        return request.fold(
            onSuccess = { apiResponse ->
                apiResponse.value?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Profile creation failed: null response"))
            },
            onFailure = { throwable ->
                Result.failure(Exception("Profile creation failed: ${throwable.message}", throwable))
            }
        )
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open image input stream")
        val requestBody = inputStream.use { it.readBytes().toRequestBody("image/*".toMediaTypeOrNull()) }
        return MultipartBody.Part.createFormData("thumbnail", uri.lastPathSegment, requestBody)
    }

    private fun createRequestBody(value: String): RequestBody =
        value.toRequestBody("text/plain".toMediaTypeOrNull())


    private suspend fun <T> handleDatabaseCall(databaseCall: suspend () -> T): Result<T> = try {
        Result.success(databaseCall())
    } catch (e: Throwable) {
        Result.failure(e)
    }

    private fun mapException(e: Exception): Throwable = when (e) {
        is java.net.UnknownHostException -> Throwable("No connection to server")
        is java.net.SocketTimeoutException -> Throwable("Request timed out")
        is retrofit2.HttpException -> Throwable("API error: ${e.message()}")
        else -> Throwable("Unknown error: ${e.message}")
    }
}
