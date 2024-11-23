package com.ulpgc.uniMatch.data.application.services

import android.net.Uri
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile

data class StringRequest(val newContent: String?)
data class IntRequest(val newContent: Int?)
data class ListRequest(val newContent: List<String>)

interface ProfileService {
    suspend fun createProfile(
        fullName: String,
        age: Int,
        aboutMe: String,
        gender: Gender,
        sexualOrientation: SexualOrientation,
        relationshipType: RelationshipType,
        birthday: String,
        location: Pair<Double, Double>?,
        profileImage: Uri
    ): Result<Profile>

    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun updateAgeRange(min: Int, max: Int): Result<Unit>
    suspend fun updateMaxDistance(distance: Int): Result<Unit>
    suspend fun updateGenderPriority(gender: Gender?): Result<Unit>
    suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<Unit>

    suspend fun updateAboutMe(aboutMe: String): Result<Unit>
    suspend fun updateFact(fact: String?): Result<Unit>
    suspend fun updateInterests(interests: List<String>): Result<Unit>
    suspend fun updateHeight(height: Int?): Result<Unit>
    suspend fun updateWeight(weight: Int?): Result<Unit>
    suspend fun updateGender(gender: Gender): Result<Unit>
    suspend fun updateSexualOrientation(
        orientation: SexualOrientation
    ): Result<Unit>

    suspend fun updateJob(position: String?): Result<Unit>
    suspend fun updateHoroscope(horoscope: Horoscope?): Result<Unit>
    suspend fun updateEducation(education: String?): Result<Unit>
    suspend fun updatePersonalityType(personalityType: String?): Result<Unit>
    suspend fun updatePets(pets: String?): Result<Unit>
    suspend fun updateDrinks(drinks: Habits?): Result<Unit>
    suspend fun updateSmokes(smokes: Habits?): Result<Unit>
    suspend fun updateDoesSports(doesSports: Habits?): Result<Unit>
    suspend fun updateValuesAndBeliefs(valuesAndBeliefs: Religion?): Result<Unit>

    suspend fun addImage(imageURI: Uri): Result<String>
    suspend fun removeImage(imageURL: String): Result<Unit>
    suspend fun updateWall(wall: List<String>): Result<Unit>
}