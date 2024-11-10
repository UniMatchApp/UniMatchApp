package com.ulpgc.uniMatch.data.application.services

import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Habits
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.Religion
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile

interface ProfileService {
    suspend fun createProfile(
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
    ): Result<Profile>
    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun updateAgeRange(userId: String, min: Int, max: Int): Result<Unit>
    suspend fun updateMaxDistance(userId: String, distance: Int): Result<Unit>
    suspend fun updateGenderPriority(userId: String, gender: Gender?): Result<Unit>
    suspend fun updateRelationshipType(userId: String, relationshipType: RelationshipType): Result<Unit>
    suspend fun updateAboutMe(userId: String, aboutMe: String): Result<Unit>
    suspend fun updateFact(userId: String, fact: String): Result<Unit>
    suspend fun updateInterests(userId: String, interests: List<String>): Result<Unit>
    suspend fun updateHeight(userId: String, height: Int): Result<Unit>
    suspend fun updateWeight(userId: String, weight: Int): Result<Unit>
    suspend fun updateGender(userId: String, gender: Gender): Result<Unit>
    suspend fun updateSexualOrientation(userId: String, orientation: SexualOrientation): Result<Unit>
    suspend fun updateJob(userId: String, position: String): Result<Unit>
    suspend fun updateHoroscope(userId: String, horoscope: Horoscope): Result<Unit>
    suspend fun updateEducation(userId: String, education: String): Result<Unit>
    suspend fun updatePersonalityType(userId: String, personalityType: String): Result<Unit>
    suspend fun updatePets(userId: String, pets: String): Result<Unit>
    suspend fun updateDrinks(userId: String, drinks: Habits): Result<Unit>
    suspend fun updateSmokes(userId: String, smokes: Habits): Result<Unit>
    suspend fun updateDoesSports(userId: String, doesSports: Habits): Result<Unit>
    suspend fun updateValuesAndBeliefs(userId: String, valuesAndBeliefs: Religion): Result<Unit>

    suspend fun addInterest(userId: String, interest: String): Result<Unit>
    suspend fun removeInterest(userId: String, interest: String): Result<Unit>

    suspend fun addImage(userId: String, image: String): Result<Unit>
    suspend fun removeImage(userId: String, image: String): Result<Unit>
}