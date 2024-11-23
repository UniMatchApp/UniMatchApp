package com.ulpgc.uniMatch.data.infrastructure.entities

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile

@Entity(tableName = "matching")
data class MatchingEntity(
    @PrimaryKey val profileId: String,
    val userId: String,
    val name: String,
    val age: Int,
    val aboutMe: String,
    val latitude: Double?,
    val longitude: Double?,
    val gender: Gender,
    val sexualOrientation: SexualOrientation,
    val relationshipType: RelationshipType,
    val birthday: String,
    val interests: List<String>,
    val wall: List<String>,
    val preferredImage: String,
    val maxDistance: Int,
    val ageRangeMin: Int,
    val ageRangeMax: Int,
    val horoscope: Horoscope?,
    val height: Int?,
    val weight: Int?,
    val job: String?,
    val education: String?,
    val personalityType: String?,
    val pets: String?,
    val drinks: Habits?,
    val smokes: Habits?,
    val doesSports: Habits?,
    val valuesAndBeliefs: Religion?,
    val genderPriority: Gender?,
    val fact: String?
) {
    companion object {
        fun toDomain(matchingEntity: MatchingEntity): Profile {
            Log.i("ApiProfileService", "toDomain: $matchingEntity")
            return Profile(
                userId = matchingEntity.userId,
                name = matchingEntity.name,
                age = matchingEntity.age,
                aboutMe = matchingEntity.aboutMe,
                location = Profile.Location(matchingEntity.latitude, matchingEntity.longitude, null),
                gender = matchingEntity.gender.toString(),
                sexualOrientation = matchingEntity.sexualOrientation.toString(),
                relationshipType = matchingEntity.relationshipType.toString(),
                birthday = matchingEntity.birthday,
                interests = matchingEntity.interests,
                wall = matchingEntity.wall,
                preferredImage = matchingEntity.preferredImage,
                maxDistance = matchingEntity.maxDistance,
                ageRange = Profile.AgeRange(matchingEntity.ageRangeMin, matchingEntity.ageRangeMax),
                horoscope = matchingEntity.horoscope?.toString(),
                height = matchingEntity.height,
                weight = matchingEntity.weight,
                job = matchingEntity.job,
                education = matchingEntity.education,
                personalityType = matchingEntity.personalityType,
                pets = matchingEntity.pets,
                drinks = matchingEntity.drinks?.toString(),
                smokes = matchingEntity.smokes?.toString(),
                doesSports = matchingEntity.doesSports?.toString(),
                valuesAndBeliefs = matchingEntity.valuesAndBeliefs?.toString(),
                genderPriority = matchingEntity.genderPriority?.toString(),
                fact = matchingEntity.fact
            )
        }

        // Método para mapear un Profile de dominio a un matchingEntity
        fun fromDomain(profile: Profile): MatchingEntity {
            return MatchingEntity(
                profileId = profile.profileId,
                userId = profile.userId,
                name = profile.name,
                age = profile.age,
                aboutMe = profile.aboutMe,
                latitude = profile.location?.latitude,
                longitude = profile.location?.longitude,
                gender = profile.genderEnum,
                sexualOrientation = profile.sexualOrientationEnum,
                relationshipType = profile.relationshipTypeEnum,
                birthday = profile.birthday,
                interests = profile.interests,
                wall = profile.wall,
                preferredImage = profile.preferredImage,
                maxDistance = profile.maxDistance,
                ageRangeMin = profile.ageRange.min,
                ageRangeMax = profile.ageRange.max,
                horoscope = profile.horoscopeEnum,
                height = profile.height,
                weight = profile.weight,
                job = profile.job,
                education = profile.education,
                personalityType = profile.personalityType,
                pets = profile.pets,
                drinks = profile.drinksEnum,
                smokes = profile.smokesEnum,
                doesSports = profile.doesSportsEnum,
                valuesAndBeliefs = profile.valuesAndBeliefsEnum,
                genderPriority = profile.genderPriorityEnum,
                fact = profile.fact
            )
        }
    }
}