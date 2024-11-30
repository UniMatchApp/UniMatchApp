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
data class AgeRangeRequest(val min: Int, val max: Int)
data class LocationRequest(val latitude: Double?, val longitude: Double?, val altitude: Double?)

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
    suspend fun updateMaxDistance(distance: Int): Result<Int>
    suspend fun updateGenderPriority(gender: Gender?): Result<Gender?>
    suspend fun updateRelationshipType(
        relationshipType: RelationshipType
    ): Result<RelationshipType>

    suspend fun updateAboutMe(aboutMe: String): Result<String>
    suspend fun updateFact(fact: String?): Result<String?>
    suspend fun updateInterests(interests: List<String>): Result<List<String>>
    suspend fun updateHeight(height: Int?): Result<Int?>
    suspend fun updateWeight(weight: Int?): Result<Int?>
    suspend fun updateGender(gender: Gender): Result<Gender>
    suspend fun updateSexualOrientation(
        orientation: SexualOrientation
    ): Result<SexualOrientation>

    suspend fun updateJob(position: String?): Result<String?>
    suspend fun updateHoroscope(horoscope: Horoscope?): Result<Horoscope?>
    suspend fun updateEducation(education: String?): Result<String?>
    suspend fun updatePersonalityType(personalityType: String?): Result<String?>
    suspend fun updatePets(pets: String?): Result<String?>
    suspend fun updateDrinks(drinks: Habits?): Result<Habits?>
    suspend fun updateSmokes(smokes: Habits?): Result<Habits?>
    suspend fun updateDoesSports(doesSports: Habits?): Result<Habits?>
    suspend fun updateValuesAndBeliefs(valuesAndBeliefs: Religion?): Result<Religion?>
    suspend fun updateLocation(location: Profile.Location?): Result<Profile.Location>

    suspend fun addImage(imageURI: Uri): Result<String>
    suspend fun removeImage(imageURL: String): Result<Unit>
    suspend fun updateWall(wall: List<String>): Result<List<String>>
}