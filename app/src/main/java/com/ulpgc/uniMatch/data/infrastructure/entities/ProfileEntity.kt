package com.ulpgc.uniMatch.data.infrastructure.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.valueObjects.Location
import java.util.Date

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val profileId: String,
    val userId: String,
    val name: String,
    val age: Int,
    val aboutMe: String,
    val latitude: Double,
    val longitude: Double,
    val gender: Gender,
    val sexualOrientation: SexualOrientation,
    val relationshipType: RelationshipType,
    val birthday: Long,
    val interests: String,
    val wall: String,
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
        // Método para mapear un ProfileEntity a un Profile de dominio
        fun toDomain(profileEntity: ProfileEntity): Profile {
            return Profile(
                userId = profileEntity.userId,
                name = profileEntity.name,
                age = profileEntity.age,
                aboutMe = profileEntity.aboutMe,
                location = Location(profileEntity.latitude, profileEntity.longitude),
                gender = profileEntity.gender,
                sexualOrientation = profileEntity.sexualOrientation,
                relationshipType = profileEntity.relationshipType,
                birthday = Date(profileEntity.birthday),
                interests = profileEntity.interests.split(","),
                wall = profileEntity.wall.split(","),
                preferredImage = profileEntity.preferredImage,
                maxDistance = profileEntity.maxDistance,
                ageRange = profileEntity.ageRangeMin to profileEntity.ageRangeMax,
                horoscope = profileEntity.horoscope,
                height = profileEntity.height,
                weight = profileEntity.weight,
                job = profileEntity.job,
                education = profileEntity.education,
                personalityType = profileEntity.personalityType,
                pets = profileEntity.pets,
                drinks = profileEntity.drinks,
                smokes = profileEntity.smokes,
                doesSports = profileEntity.doesSports,
                valuesAndBeliefs = profileEntity.valuesAndBeliefs,
                genderPriority = profileEntity.genderPriority,
                fact = profileEntity.fact
            )
        }

        // Método para mapear un Profile de dominio a un ProfileEntity
        fun fromDomain(profile: Profile): ProfileEntity {
            return ProfileEntity(
                profileId = profile.profileId,
                userId = profile.userId,
                name = profile.name,
                age = profile.age,
                aboutMe = profile.aboutMe,
                latitude = profile.location.latitude,
                longitude = profile.location.longitude,
                gender = profile.gender,
                sexualOrientation = profile.sexualOrientation,
                relationshipType = profile.relationshipType,
                birthday = profile.birthday.time,
                interests = profile.interests.joinToString(","),
                wall = profile.wall.joinToString(","),
                preferredImage = profile.preferredImage,
                maxDistance = profile.maxDistance,
                ageRangeMin = profile.ageRange.first,
                ageRangeMax = profile.ageRange.second,
                horoscope = profile.horoscope,
                height = profile.height,
                weight = profile.weight,
                job = profile.job,
                education = profile.education,
                personalityType = profile.personalityType,
                pets = profile.pets,
                drinks = profile.drinks,
                smokes = profile.smokes,
                doesSports = profile.doesSports,
                valuesAndBeliefs = profile.valuesAndBeliefs,
                genderPriority = profile.genderPriority,
                fact = profile.fact
            )
        }
    }
}