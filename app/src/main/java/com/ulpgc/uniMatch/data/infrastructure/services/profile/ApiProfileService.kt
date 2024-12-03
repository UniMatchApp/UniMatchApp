package com.ulpgc.uniMatch.data.infrastructure.services.profile

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.protobuf.Internal
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
import okhttp3.MediaType
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
        safeApiCall { profileController.updateAgeRange(AgeRangeRequest(min, max)) }.mapCatching {
            Unit
        }


    override suspend fun updateMaxDistance(distance: Int): Result<Int> =
        safeApiCall { profileController.updateMaxDistance(IntRequest(distance)) }.mapCatching {
            distance
        }


    override suspend fun updateGenderPriority(gender: Gender?): Result<Gender?> =
       try {
           safeApiCall {
               profileController.updateGenderPriority(StringRequest(enumToString(gender)))
           }.mapCatching { response ->
               response?.let { Gender.valueOf(it) }
           }
       } catch (e: NullPointerException) {
           Result.success(null)
       }

    override suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<RelationshipType> =
        safeApiCall {
            profileController.updateRelationshipType(StringRequest(relationshipType.toString()))
        }.mapCatching { response ->
            response?.let { RelationshipType.valueOf(it) } ?: throw NullPointerException()
        }

    override suspend fun updateAboutMe(aboutMe: String): Result<String> =
        safeApiCall { profileController.updateAbout(StringRequest(aboutMe)) }.mapCatching {
            aboutMe
        }

    override suspend fun updateFact(fact: String?): Result<String?> =
        try {
            safeApiCall { profileController.updateFact(StringRequest(fact)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateInterests(interests: List<String>): Result<List<String>> =
        safeApiCall {
            profileController.updateInterests(ListRequest(interests))
        }.mapCatching {
            interests
        }

    override suspend fun updateHeight(height: Int?): Result<Int?> =
        try{
            safeApiCall { profileController.updateHeight(IntRequest(height)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateWeight(weight: Int?): Result<Int?> =
        try {
            safeApiCall { profileController.updateWeight(IntRequest(weight)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateGender(gender: Gender): Result<Gender> =
        safeApiCall {
            profileController.updateGender(StringRequest(gender.toString()))
        }.mapCatching { response ->
            response?.let { Gender.valueOf(it) } ?: throw NullPointerException()
        }

    override suspend fun updateSexualOrientation(orientation: SexualOrientation): Result<SexualOrientation> =
        safeApiCall {
            profileController.updateSexualOrientation(StringRequest(orientation.toString()))
        }.mapCatching { response ->
            response?.let { SexualOrientation.valueOf(it) } ?: throw NullPointerException()
        }

    override suspend fun updateJob(position: String?): Result<String?> =
        try {
            safeApiCall { profileController.updateJob(StringRequest(position)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateHoroscope(horoscope: Horoscope?): Result<Horoscope?> =
        try {
            safeApiCall {
                profileController.updateHoroscope(StringRequest(enumToString(horoscope)))
            }.mapCatching { response ->
                response?.let { Horoscope.valueOf(it) }
            }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateEducation(education: String?): Result<String?> =
        try {
            safeApiCall { profileController.updateDegree(StringRequest(education)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updatePersonalityType(personalityType: String?): Result<String?> =
        try {
            safeApiCall { profileController.updatePersonality(StringRequest(personalityType)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updatePets(pets: String?): Result<String?> =
        try {
            safeApiCall { profileController.updatePets(StringRequest(pets)) }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateDrinks(drinks: Habits?): Result<Habits?> =
        try {
            safeApiCall {
                profileController.updateDrinks(StringRequest(enumToString(drinks)))
            }.mapCatching { response ->
                response?.let { Habits.valueOf(it) }
            }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateSmokes(smokes: Habits?): Result<Habits?> =
        try {
            safeApiCall {
                profileController.updateSmokes(StringRequest(enumToString(smokes)))
            }.mapCatching { response ->
                response?.let { Habits.valueOf(it) }
            }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateDoesSports(doesSports: Habits?): Result<Habits?> =
        try {
            safeApiCall {
                profileController.updateSports(StringRequest(enumToString(doesSports)))
            }.mapCatching { response ->
                response?.let { Habits.valueOf(it) }
            }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateValuesAndBeliefs(valuesAndBeliefs: Religion?): Result<Religion?> =
        try {
            safeApiCall {
                profileController.updateValuesAndBeliefs(StringRequest(enumToString(valuesAndBeliefs)))
            }.mapCatching { response ->
                response?.let { Religion.valueOf(it) }
            }
        } catch (e: NullPointerException) {
            Result.success(null)
        }

    override suspend fun updateLocation(location: Profile.Location?): Result<Profile.Location?> {
        return safeApiCall {
            Log.i("UpdateLocation", "Updating location in api ${location?.longitude},${location?.latitude}")
            profileController.updateLocation(
                LocationRequest(
                    location?.longitude,
                    location?.latitude,
                    location?.altitude
                )
            )
        }.mapCatching {
            location
        }
    }

    override suspend fun addImage(imageURI: Uri): Result<String> =
        safeApiCall { profileController.uploadPhoto(createImagePart(imageURI)) }.mapCatching { image ->
            image ?: throw NullPointerException()
        }

    override suspend fun removeImage(imageURL: String): Result<Unit> =
        safeApiCall { profileController.deletePhoto(imageURL) }.mapCatching {
            Unit
        }

    override suspend fun updateWall(wall: List<String>): Result<List<String>> =
        safeApiCall { profileController.updateWall(ListRequest(wall)) }.mapCatching {
            wall
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
    ): Result<Profile> = withContext(Dispatchers.IO) {
        safeRequest {
            val gson = Gson()

            val fullNameRequest = createRequestBody(fullName)
            val ageRequest = createRequestBody(age.toString())
            val aboutMeRequest = createRequestBody(aboutMe)
            val genderRequest = createRequestBody(gender.toString())
            val sexualOrientationRequest = createRequestBody(sexualOrientation.toString())
            val relationshipTypeRequest = createRequestBody(relationshipType.toString())
            val birthdayRequest = createRequestBody(birthday)

            val longitude = location?.first
            val latitude = location?.second

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
                    longitude,
                    latitude,
                    imageRequest,
                )
            }.mapCatching { it ?: throw NullPointerException() }
        }
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open image input stream")
        val requestBody =
            inputStream.use { it.readBytes().toRequestBody("image/png".toMediaTypeOrNull()) }
        return MultipartBody.Part.createFormData("thumbnail", uri.lastPathSegment, requestBody)
    }

    private fun createRequestBody(value: String): RequestBody =
        value.toRequestBody("text/plain".toMediaTypeOrNull())
}

