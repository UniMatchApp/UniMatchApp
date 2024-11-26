package com.ulpgc.uniMatch.data.infrastructure.services.profile

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
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
import com.ulpgc.uniMatch.ui.screens.shared.safeApiCall
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
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
        return safeRequest {
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
            return@safeRequest ProfileEntity.toDomain(profileEntity)
        }
    }

    override suspend fun updateAgeRange(min: Int, max: Int): Result<Unit> =
        safeApiCall { profileController.updateAgeRange(AgeRangeRequest(min, max)) }


    override suspend fun updateMaxDistance(distance: Int): Result<Int> =
        safeApiCall { profileController.updateMaxDistance(IntRequest(distance)) }


    override suspend fun updateGenderPriority(gender: Gender?): Result<String?> =
        safeApiCall { profileController.updateGenderPriority(StringRequest(enumToString(gender))) }


    override suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<String?> =
        safeApiCall {
            profileController.updateRelationshipType(StringRequest(relationshipType.toString()))
        }

    override suspend fun updateAboutMe(aboutMe: String): Result<String> =
        safeApiCall { profileController.updateAbout(StringRequest(aboutMe)) }

    override suspend fun updateFact(fact: String?): Result<String> =
        safeApiCall { profileController.updateFact(StringRequest(fact)) }

    override suspend fun updateInterests(interests: List<String>): Result<List<String>> =
        safeApiCall { profileController.updateInterests(ListRequest(interests)) }

    override suspend fun updateHeight(height: Int?): Result<Int> =
        safeApiCall { profileController.updateHeight(IntRequest(height)) }

    override suspend fun updateWeight(weight: Int?): Result<Int> =
        safeApiCall { profileController.updateWeight(IntRequest(weight)) }

    override suspend fun updateGender(gender: Gender): Result<String> =
        safeApiCall { profileController.updateGender(StringRequest(enumToString(gender))) }

    override suspend fun updateSexualOrientation(orientation: SexualOrientation): Result<String> =
        safeApiCall { profileController.updateSexualOrientation(StringRequest(orientation.toString())) }

    override suspend fun updateJob(position: String?): Result<String?> =
        safeApiCall { profileController.updateJob(StringRequest(position)) }

    override suspend fun updateHoroscope(horoscope: Horoscope?): Result<String?> =
        safeApiCall { profileController.updateHoroscope(StringRequest(enumToString(horoscope))) }

    override suspend fun updateEducation(education: String?): Result<String> =
        safeApiCall { profileController.updateDegree(StringRequest(education)) }

    override suspend fun updatePersonalityType(personalityType: String?): Result<String?> =
        safeApiCall { profileController.updatePersonality(StringRequest(personalityType)) }

    override suspend fun updatePets(pets: String?): Result<String?> =
        safeApiCall { profileController.updatePets(StringRequest(pets)) }

    override suspend fun updateDrinks(drinks: Habits?): Result<String> =
        safeApiCall { profileController.updateDrinks(StringRequest(enumToString(drinks))) }

    override suspend fun updateSmokes(smokes: Habits?): Result<String?> =
        safeApiCall { profileController.updateSmokes(StringRequest(enumToString(smokes))) }

    override suspend fun updateDoesSports(doesSports: Habits?): Result<String?> =
        safeApiCall { profileController.updateSports(StringRequest(enumToString(doesSports))) }

    override suspend fun updateValuesAndBeliefs(valuesAndBeliefs: Religion?): Result<String?> =
        safeApiCall {
            profileController.updateValuesAndBeliefs(StringRequest(enumToString(valuesAndBeliefs)))
        }

    override suspend fun updateLocation(location: Profile.Location?): Result<Profile.Location> {
        return safeApiCall {
            profileController.updateLocation(
                LocationRequest(
                    location?.longitude,
                    location?.latitude,
                    location?.altitude
                )
            )
        }
    }

    override suspend fun addImage(imageURI: Uri): Result<String> =
        safeApiCall { profileController.uploadPhoto(createImagePart(imageURI)) }


    override suspend fun removeImage(imageURL: String): Result<Unit> =
        safeApiCall { profileController.deletePhoto(imageURL) }

    override suspend fun updateWall(wall: List<String>): Result<List<String>> =
        safeApiCall { profileController.updateWall(ListRequest(wall)) }

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
        safeRequest {
            val fullNameRequest = createRequestBody(fullName)
            val ageRequest = createRequestBody(age.toString())
            val aboutMeRequest = createRequestBody(aboutMe)
            val genderRequest = createRequestBody(gender.toString())
            val sexualOrientationRequest = createRequestBody(sexualOrientation.toString())
            val relationshipTypeRequest = createRequestBody(relationshipType.toString())
            val birthdayRequest = createRequestBody(birthday)
            val locationRequest = location?.let { createRequestBody("${it.first},${it.second}") }

            val imageRequest = createImagePart(profileImage)

            return@withContext safeApiCall {
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
        }
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
}
