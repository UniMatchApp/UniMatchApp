package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.ApiResponse
import com.ulpgc.uniMatch.data.application.services.*
import com.ulpgc.uniMatch.data.domain.enum.Habits
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.Religion
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.Job
import retrofit2.http.*

interface ProfileController {

    // Crear perfil
    @POST("users/createProfile")
    suspend fun createProfile(
        @Query("userId") userId: String,
        @Query("fullName") fullName: String,
        @Query("age") age: Int,
        @Query("aboutMe") aboutMe: String,
        @Query("gender") gender: String,
        @Query("sexualOrientation") sexualOrientation: String,
        @Query("relationshipType") relationshipType: String,
        @Query("birthday") birthday: String,
        @Query("location") location: Pair<Double, Double>?,
        @Query("profileImageUri") profileImageUri: String?
    ): ApiResponse<Profile>

    @PUT("profile/{id}/about")
    suspend fun updateAbout(@Path("id") userId: String, @Body about: String): ApiResponse<Unit>

    @PUT("profile/{id}/degree")
    suspend fun updateDegree(@Path("id") userId: String, @Body degree: String): ApiResponse<Unit>

    @PUT("profile/{id}/drinks")
    suspend fun updateDrinks(@Path("id") userId: String, @Body drinks: Habits): ApiResponse<Unit>

    @PUT("profile/{id}/email")
    suspend fun updateEmail(@Path("id") userId: String, @Body email: String): ApiResponse<Unit>

    @PUT("profile/{id}/height")
    suspend fun updateHeight(@Path("id") userId: String, @Body height: Int): ApiResponse<Unit>

    @PUT("profile/{id}/weight")
    suspend fun updateWeight(@Path("id") userId: String, @Body weight: Int): ApiResponse<Unit>

    @PUT("profile/{id}/horoscope")
    suspend fun updateHoroscope(@Path("id") userId: String, @Body horoscope: Horoscope): ApiResponse<Unit>

    @PUT("profile/{id}/interests")
    suspend fun updateInterests(@Path("id") userId: String, @Body interests: String): ApiResponse<Unit>

    @PUT("profile/{id}/job")
    suspend fun updateJob(@Path("id") userId: String, @Body job: Job): ApiResponse<Unit>

    @PUT("profile/{id}/password")
    suspend fun updatePassword(@Path("id") userId: String, @Body password: String): ApiResponse<Unit>

    @PUT("profile/{id}/personality")
    suspend fun updatePersonality(@Path("id") userId: String, @Body personality: String): ApiResponse<Unit>

    @PUT("profile/{id}/pets")
    suspend fun updatePets(@Path("id") userId: String, @Body pets: String): ApiResponse<Unit>

    @PUT("profile/{id}/relationship-type")
    suspend fun updateRelationshipType(@Path("id") userId: String, @Body relationshipType: RelationshipType): ApiResponse<Unit>

    @PUT("profile/{id}/sexual-orientation")
    suspend fun updateSexualOrientation(@Path("id") userId: String, @Body sexualOrientation: SexualOrientation): ApiResponse<Unit>

    @PUT("profile/{id}/smokes")
    suspend fun updateSmokes(@Path("id") userId: String, @Body smokes: Habits): ApiResponse<Unit>

    @PUT("profile/{id}/sports")
    suspend fun updateSports(@Path("id") userId: String, @Body sports: Habits): ApiResponse<Unit>

    @PUT("profile/{id}/values-and-beliefs")
    suspend fun updateValuesAndBeliefs(@Path("id") userId: String, @Body valuesAndBeliefs: Religion): ApiResponse<Unit>

    @DELETE("profile/{id}/deletePhoto/{photoUrl}")
    suspend fun deletePhoto(@Path("id") userId: String, @Path("photoUrl") photoUrl: String): ApiResponse<Unit>

    @POST("profile/{id}/photo")
    suspend fun uploadPhoto(@Path("id") userId: String, @Body photoRequest: String): ApiResponse<Unit>
}
