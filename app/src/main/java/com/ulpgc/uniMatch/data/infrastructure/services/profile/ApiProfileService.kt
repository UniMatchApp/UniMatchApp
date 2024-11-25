package com.ulpgc.uniMatch.data.infrastructure.services.profile

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.AgeRangeRequest
import com.ulpgc.uniMatch.data.application.services.IntRequest
import com.ulpgc.uniMatch.data.application.services.ListRequest
import com.ulpgc.uniMatch.data.application.services.LocationRequest
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.application.services.StringRequest
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
import com.ulpgc.uniMatch.ui.screens.utils.enumToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class ApiProfileService(
    private val profileController: ProfileController,
    private val profileDao: ProfileDao,
    private val contentResolver: ContentResolver
) : ProfileService {

    override suspend fun getProfile(userId: String): Result<Profile> {
        return try {
            var profileEntity = profileDao.getProfileById(userId)
            val updatedProfileEntity = profileController.getProfile(userId)

            if (updatedProfileEntity.success && updatedProfileEntity.value != null) {
                profileEntity = ProfileEntity.fromDomain(updatedProfileEntity.value)
                profileDao.insertProfile(profileEntity)
            }

            if (profileEntity == null) {
                return Result.failure(Throwable("Profile for user $userId not found"))
            }

            Log.i("ApiProfileService", "ProfileEntity: $profileEntity")
            Result.success(ProfileEntity.toDomain(profileEntity))
        } catch (e: Throwable) {
            Log.e("ApiProfileService", "Error getting profile", e)
            Result.failure(e)
        }
    }

    override suspend fun updateAgeRange(min: Int, max: Int): Result<Unit> =
        handleApiCall { profileController.updateAgeRange(AgeRangeRequest(min, max)) }

    override suspend fun updateMaxDistance(distance: Int): Result<Int> {
        val handle = handleApiCall { profileController.updateMaxDistance(IntRequest(distance)) }
        return handle.map { distance }
    }

    override suspend fun updateGenderPriority(gender: Gender?): Result<Unit> =
        handleApiCall { profileController.updateGenderPriority(StringRequest(enumToString(gender))) }

    override suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<Unit> =
        handleApiCall { profileController.updateRelationshipType(StringRequest(relationshipType.toString())) }

    override suspend fun updateAboutMe(aboutMe: String): Result<Unit> =
        handleApiCall { profileController.updateAbout(StringRequest(aboutMe)) }

    override suspend fun updateFact(fact: String?): Result<Unit> =
        handleApiCall { profileController.updateFact(StringRequest(fact)) }

    override suspend fun updateInterests(interests: List<String>): Result<Unit> =
        handleApiCall { profileController.updateInterests(ListRequest(interests)) }

    override suspend fun updateHeight(height: Int?): Result<Unit> =
        handleApiCall { profileController.updateHeight(IntRequest(height)) }

    override suspend fun updateWeight(weight: Int?): Result<Unit> =
        handleApiCall { profileController.updateWeight(IntRequest(weight)) }

    override suspend fun updateGender(gender: Gender): Result<Unit> =
        handleApiCall { profileController.updateGender(StringRequest(enumToString(gender))) }

    override suspend fun updateSexualOrientation(
        orientation: SexualOrientation
    ): Result<Unit> =
        handleApiCall { profileController.updateSexualOrientation(StringRequest(orientation.toString())) }

    override suspend fun updateJob(position: String?): Result<Unit> =
        handleApiCall {
            profileController.updateJob(StringRequest(position))
        }

    override suspend fun updateHoroscope(horoscope: Horoscope?): Result<Unit> =
        handleApiCall { profileController.updateHoroscope(StringRequest(enumToString(horoscope))) }

    override suspend fun updateEducation(education: String?): Result<Unit> =
        handleApiCall {
            profileController.updateDegree(StringRequest(education))
        }

    override suspend fun updatePersonalityType(
        personalityType: String?
    ): Result<Unit> =
        handleApiCall { profileController.updatePersonality(StringRequest(personalityType)) }

    override suspend fun updatePets(pets: String?): Result<Unit> =
        handleApiCall {
            profileController.updatePets(StringRequest(pets)) }

    override suspend fun updateDrinks(drinks: Habits?): Result<Unit> =
        handleApiCall { profileController.updateDrinks(StringRequest(enumToString(drinks))) }

    override suspend fun updateSmokes(smokes: Habits?): Result<Unit> =
        handleApiCall {
            profileController.updateSmokes(StringRequest(enumToString(smokes))) }

    override suspend fun updateDoesSports(doesSports: Habits?): Result<Unit> =
        handleApiCall { profileController.updateSports(StringRequest(enumToString(doesSports))) }

    override suspend fun updateValuesAndBeliefs(
        valuesAndBeliefs: Religion?
    ): Result<Unit> =
        handleApiCall { profileController.updateValuesAndBeliefs(StringRequest(enumToString(valuesAndBeliefs))) }

    override suspend fun updateLocation(location: Profile.Location?): Result<Unit> {
        val longitude = location?.longitude
        val latitude = location?.latitude
        val altitude = location?.altitude
        Log.i("ApiProfileService", "Updating location: $location")
        return handleApiCall { profileController.updateLocation(LocationRequest(longitude, latitude, altitude)) }
    }

    override suspend fun addImage(imageURI: Uri): Result<String> {
        return try {
            val response = profileController.uploadPhoto(createImagePart(imageURI))
            Result.success(response.value ?: "")
        } catch (e: Exception) {
            Result.failure(mapException(e))
        }
    }

    override suspend fun removeImage(imageURL: String): Result<Unit> =
        handleApiCall { profileController.deletePhoto(imageURL) }

    override suspend fun updateWall(wall: List<String>): Result<Unit> =
        handleApiCall { profileController.updateWall(ListRequest(wall)) }

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
            Result.failure(Exception("${e.message}", e))
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
                Result.failure(
                    Exception(
                        "Profile creation failed: ${throwable.message}",
                        throwable
                    )
                )
            }
        )
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open image input stream")
        val requestBody =
            inputStream.use { it.readBytes().toRequestBody("image/*".toMediaTypeOrNull()) }
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
