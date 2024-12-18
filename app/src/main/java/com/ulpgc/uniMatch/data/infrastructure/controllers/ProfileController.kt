package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.AgeRangeRequest
import com.ulpgc.uniMatch.data.application.services.IntRequest
import com.ulpgc.uniMatch.data.application.services.ListRequest
import com.ulpgc.uniMatch.data.application.services.LocationRequest
import com.ulpgc.uniMatch.data.application.services.StringRequest
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
    @POST("users/profile")
    suspend fun createProfile(
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("aboutMe") aboutMe: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("sexualOrientation") sexualOrientation: RequestBody,
        @Part("relationshipType") relationshipType: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part("latitude") latitude: Double?,
        @Part("longitude") longitude: Double?,
        @Part thumbnail: MultipartBody.Part
    ): ApiResponse<Profile>

    @PUT("users/about")
    suspend fun updateAbout(@Body about: StringRequest): ApiResponse<String>

    @PUT("users/fact")
    suspend fun updateFact(@Body fact: StringRequest): ApiResponse<String?>

    @PUT("users/degree")
    suspend fun updateDegree(@Body degree: StringRequest): ApiResponse<String?>

    @PUT("users/drinks")
    suspend fun updateDrinks(@Body drinks: StringRequest): ApiResponse<String?>

    @PUT("users/height")
    suspend fun updateHeight(@Body height: IntRequest): ApiResponse<Int?>

    @PUT("users/weight")
    suspend fun updateWeight(@Body weight: IntRequest): ApiResponse<Int?>

    @PUT("users/gender")
    suspend fun updateGender(@Body gender: StringRequest): ApiResponse<String>

    @PUT("users/horoscope")
    suspend fun updateHoroscope(
        @Body horoscope: StringRequest
    ): ApiResponse<String?>

    @PUT("users/interests")
    suspend fun updateInterests(
        @Body interests: ListRequest
    ): ApiResponse<List<String>>

    @PUT("users/job")
    suspend fun updateJob(@Body job: StringRequest): ApiResponse<String?>

    @PUT("users/personality")
    suspend fun updatePersonality(
        @Body personality: StringRequest
    ): ApiResponse<String?>

    @PUT("users/pets")
    suspend fun updatePets(@Body pets: StringRequest): ApiResponse<String?>

    @PUT("users/relationship-type")
    suspend fun updateRelationshipType(
        @Body relationshipType: StringRequest
    ): ApiResponse<String>

    @PUT("users/sexual-orientation")
    suspend fun updateSexualOrientation(
        @Body sexualOrientation: StringRequest
    ): ApiResponse<String>

    @PUT("users/smokes")
    suspend fun updateSmokes(@Body smokes: StringRequest): ApiResponse<String?>

    @PUT("users/sports")
    suspend fun updateSports(@Body sports: StringRequest): ApiResponse<String?>

    @PUT("users/values-and-beliefs")
    suspend fun updateValuesAndBeliefs(@Body valuesAndBeliefs: StringRequest): ApiResponse<String?>

    @PUT("users/gender-priority")
    suspend fun updateGenderPriority(@Body gender: StringRequest?): ApiResponse<String?>

    @PUT("users/max-distance")
    suspend fun updateMaxDistance(
        @Body distance: IntRequest
    ): ApiResponse<Int>

    @PUT("users/age-range")
    suspend fun updateAgeRange(
        @Body ageRange: AgeRangeRequest
    ): ApiResponse<Unit>

    @PUT("users/wall")
    suspend fun updateWall(@Body wall: ListRequest): ApiResponse<List<String>>

    @PUT("users/location")
    suspend fun updateLocation(@Body location: LocationRequest): ApiResponse<Profile.Location>

    @Multipart
    @POST("users/photo")
    suspend fun uploadPhoto(
        @Part photoURL: MultipartBody.Part
    ): ApiResponse<String>

    @DELETE("users/delete-photo/{photoUrl}")
    suspend fun deletePhoto(
        @Path("photoUrl") photoUrl : String
    ): ApiResponse<Unit>
}
