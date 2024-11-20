package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.IntRequest
import com.ulpgc.uniMatch.data.application.services.ListRequest
import com.ulpgc.uniMatch.data.application.services.StringRequest
import com.ulpgc.uniMatch.data.domain.enums.Gender
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
    suspend fun updateAbout(@Path("id") userId: String, @Body about: StringRequest): ApiResponse<String>

    @PUT("users/{id}/fact")
    suspend fun updateFact(@Path("id") userId: String, @Body fact: StringRequest): ApiResponse<String>

    @PUT("users/{id}/degree")
    suspend fun updateDegree(@Path("id") userId: String, @Body degree: StringRequest): ApiResponse<String>

    @PUT("users/{id}/drinks")
    suspend fun updateDrinks(@Path("id") userId: String, @Body drinks: StringRequest): ApiResponse<String>

    @PUT("users/{id}/height")
    suspend fun updateHeight(@Path("id") userId: String, @Body height: IntRequest): ApiResponse<Int>

    @PUT("users/{id}/weight")
    suspend fun updateWeight(@Path("id") userId: String, @Body weight: IntRequest): ApiResponse<Int>

    @PUT("users/{id}/education")
    suspend fun updateEducation(
        @Path("id") userId: String,
        @Body education: StringRequest
    ): ApiResponse<String>

    @PUT("users/{id}/gender")
    suspend fun updateGender(@Path("id") id: String, @Body gender: StringRequest): ApiResponse<String>

    @PUT("users/{id}/horoscope")
    suspend fun updateHoroscope(
        @Path("id") userId: String,
        @Body horoscope: StringRequest
    ): ApiResponse<String>

    @PUT("users/{id}/interests")
    suspend fun updateInterests(
        @Path("id") userId: String,
        @Body interests: ListRequest
    ): ApiResponse<List<String>>

    @PUT("users/{id}/job")
    suspend fun updateJob(@Path("id") userId: String, @Body job: StringRequest): ApiResponse<String>

    @PUT("users/{id}/personality")
    suspend fun updatePersonality(
        @Path("id") userId: String,
        @Body personality: StringRequest
    ): ApiResponse<String>

    @PUT("users/{id}/pets")
    suspend fun updatePets(@Path("id") userId: String, @Body pets: StringRequest): ApiResponse<String>

    @PUT("users/{id}/relationship-type")
    suspend fun updateRelationshipType(
        @Path("id") userId: String,
        @Body relationshipType: StringRequest
    ): ApiResponse<String>

    @PUT("users/{id}/sexual-orientation")
    suspend fun updateSexualOrientation(
        @Path("id") userId: String,
        @Body sexualOrientation: StringRequest
    ): ApiResponse<String>

    @PUT("users/{id}/smokes")
    suspend fun updateSmokes(@Path("id") userId: String, @Body smokes: StringRequest): ApiResponse<String>

    @PUT("users/{id}/sports")
    suspend fun updateSports(@Path("id") userId: String, @Body sports: StringRequest): ApiResponse<String>

    @PUT("users/{id}/values-and-beliefs")
    suspend fun updateValuesAndBeliefs(@Path("id") userId: String,@Body valuesAndBeliefs: StringRequest): ApiResponse<String>

    @PUT("users/{id}/gender-priority")
    suspend fun updateGenderPriority(@Path("id") userId: String, @Body gender: Gender?): ApiResponse<String>

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

}
