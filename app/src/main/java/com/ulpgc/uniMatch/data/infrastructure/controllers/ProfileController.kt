package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileController {

    @GET("users/{id}")
    suspend fun getProfile(@Path("id") id: String): ApiResponse<Profile>

    @Multipart
    @POST("users/{id}")
    suspend fun createProfile(
        @Path("id") userId: String,
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("aboutMe") aboutMe: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("sexualOrientation") sexualOrientation: RequestBody,
        @Part("relationshipType") relationshipType: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part("location") location: RequestBody?,
        @Part thumbnail: MultipartBody.Part
    ): ApiResponse<Profile>

    @PUT("users/{id}/about")
    suspend fun updateAbout(@Path("id") userId: String, @Body about: String): ApiResponse<Unit>

    @PUT("users/{id}/fact")
    suspend fun updateFact(@Path("id") userId: String, @Body fact: String): ApiResponse<Unit>

    @PUT("users/{id}/degree")
    suspend fun updateDegree(@Path("id") userId: String, @Body degree: String): ApiResponse<Unit>

    @PUT("users/{id}/drinks")
    suspend fun updateDrinks(@Path("id") userId: String, @Body drinks: Habits): ApiResponse<Unit>

    @PUT("users/{id}/height")
    suspend fun updateHeight(@Path("id") userId: String, @Body height: Int): ApiResponse<Unit>

    @PUT("users/{id}/weight")
    suspend fun updateWeight(@Path("id") userId: String, @Body weight: Int): ApiResponse<Unit>

    @PUT("users/{id}/education")
    suspend fun updateEducation(
        @Path("id") userId: String,
        @Body education: String
    ): ApiResponse<Unit>

    @PUT("users/{id}/gender")
    suspend fun updateGender(@Path("id") userId: String, @Body gender: Gender): ApiResponse<Unit>

    @PUT("users/{id}/horoscope")
    suspend fun updateHoroscope(
        @Path("id") userId: String,
        @Body horoscope: Horoscope
    ): ApiResponse<Unit>

    @PUT("users/{id}/interests")
    suspend fun updateInterests(
        @Path("id") userId: String,
        @Body interests: List<String>
    ): ApiResponse<Unit>

    @PUT("users/{id}/job")
    suspend fun updateJob(@Path("id") userId: String, @Body job: String): ApiResponse<Unit>

    @PUT("users/{id}/personality")
    suspend fun updatePersonality(
        @Path("id") userId: String,
        @Body personality: String
    ): ApiResponse<Unit>

    @PUT("users/{id}/pets")
    suspend fun updatePets(@Path("id") userId: String, @Body pets: String): ApiResponse<Unit>

    @PUT("users/{id}/relationship-type")
    suspend fun updateRelationshipType(
        @Path("id") userId: String,
        @Body relationshipType: RelationshipType
    ): ApiResponse<Unit>

    @PUT("users/{id}/sexual-orientation")
    suspend fun updateSexualOrientation(
        @Path("id") userId: String,
        @Body sexualOrientation: SexualOrientation
    ): ApiResponse<Unit>

    @PUT("users/{id}/smokes")
    suspend fun updateSmokes(@Path("id") userId: String, @Body smokes: Habits): ApiResponse<Unit>

    @PUT("users/{id}/sports")
    suspend fun updateSports(@Path("id") userId: String, @Body sports: Habits): ApiResponse<Unit>

    @PUT("users/{id}/values-and-beliefs")
    suspend fun updateValuesAndBeliefs(
        @Path("id") userId: String,
        @Body valuesAndBeliefs: Religion
    ): ApiResponse<Unit>

    @PUT("users/{id}/gender-priority")
    suspend fun updateGenderPriority(
        @Path("id") userId: String,
        @Body gender: Gender?
    ): ApiResponse<Unit>

    @PUT("users/{id}/max-distance")
    suspend fun updateMaxDistance(
        @Path("id") userId: String,
        @Body distance: Int
    ): ApiResponse<Unit>

    @PUT("users/{id}/age-range")
    suspend fun updateAgeRange(
        @Path("id") userId: String,
        @Body min: Int,
        @Body max: Int
    ): ApiResponse<Unit>

    @DELETE("users/{id}/delete-photo/{photoUrl}")
    suspend fun deletePhoto(
        @Path("id") userId: String,
        @Path("photoUrl") photoUrl: String
    ): ApiResponse<Unit>

    @POST("users/{id}/photo")
    suspend fun uploadPhoto(
        @Path("id") userId: String,
        @Body photoRequest: String
    ): ApiResponse<Unit>

    @DELETE("users/{id}/interest")
    suspend fun removeInterest(
        @Path("id") userId: String,
        @Body interest: String
    ): ApiResponse<Unit>
}
