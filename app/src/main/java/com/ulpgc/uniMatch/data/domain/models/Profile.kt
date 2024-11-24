package com.ulpgc.uniMatch.data.domain.models

import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Profile(
    val profileId: String = "",
    val userId: String,
    val name: String,
    val age: Int,
    var aboutMe: String,
    var location: Location?,
    var gender: String,
    var sexualOrientation: String,
    var relationshipType: String,
    val birthday: String,
    var interests: List<String>,
    var wall: List<String>,
    var preferredImage: String,
    var maxDistance: Int,
    var ageRange: AgeRange,
    var horoscope: String?,
    var height: Int?,
    var weight: Int?,
    var job: String?,
    var education: String?,
    var personalityType: String?,
    var pets: String?,
    var drinks: String?,
    var smokes: String?,
    var doesSports: String?,
    var valuesAndBeliefs: String?,
    var genderPriority: String?,
    var fact: String?
) {

    val sexualOrientationEnum: SexualOrientation
        get() {
            return SexualOrientation.valueOf(sexualOrientation.uppercase())
        }

    val relationshipTypeEnum: RelationshipType
        get() {
            return RelationshipType.valueOf(relationshipType.uppercase())
        }

    val drinksEnum: Habits?
        get() {
            return drinks?.let { Habits.valueOf(it.uppercase()) }
        }

    val smokesEnum: Habits?
        get() {
            return smokes?.let { Habits.valueOf(it.uppercase()) }
        }

    val doesSportsEnum: Habits?
        get() {
            return doesSports?.let { Habits.valueOf(it.uppercase()) }
        }

    val valuesAndBeliefsEnum: Religion?
        get() {
            return valuesAndBeliefs?.let { Religion.valueOf(it.uppercase()) }
        }


    val genderEnum: Gender
        get() {
            return Gender.valueOf(gender.uppercase(Locale.getDefault()))
        }


    val genderPriorityEnum: Gender?
        get() {
            return genderPriority?.let { Gender.valueOf(it.uppercase()) }
        }

    val horoscopeEnum: Horoscope?
        get() {
            return horoscope?.let { Horoscope.valueOf(it.uppercase()) }
        }

    // Getter for date as Date object
    val birthdayDate: Date
        get() {
            val dateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
            return dateFormat.parse(birthday) ?: Date()
        }

    data class Location(
        val latitude: Double,
        val longitude: Double,
        val altitude: Double?
    )

    data class AgeRange(
        val min: Int,
        val max: Int
    )
}

//
//data class ProfileOld(
//    val profileId: String = "",
//    val userId: String,
//    var name: String,
//    var age: Int,
//    var aboutMe: String,
//    var location: Location,
//    var gender: Gender,
//    var sexualOrientation: SexualOrientation,
//    var relationshipType: RelationshipType,
//    var birthday: Date,
//    var interests: List<String> = emptyList(),
//    var wall: List<String> = emptyList(),
//    var preferredImage: String = wall.firstOrNull() ?: "",
//    var maxDistance: Int = 50,
//    var ageRange: Pair<Int, Int> = 18 to 100,
//    var horoscope: Horoscope?,
//    var height: Int?,
//    var weight: Int?,
//    var job: String?,
//    var education: String?,
//    var personalityType: String?,
//    var pets: String?,
//    var drinks: Habits?,
//    var smokes: Habits?,
//    var doesSports: Habits?,
//    var valuesAndBeliefs: Religion?,
//    var genderPriority: Gender?,
//    var fact: String?
//) {
//    companion object {
//        fun fromDTO(profileDTO: ProfileDTO): ProfileOld {
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
//                timeZone = TimeZone.getTimeZone("UTC")
//            }
//            return ProfileOld(
//                profileId = "",
//                userId = profileDTO.userId,
//                name = profileDTO.name,
//                age = profileDTO.age,
//                aboutMe = profileDTO.aboutMe,
//                location = Location(
//                    _latitude = profileDTO.location.latitude,
//                    _longitude = profileDTO.location.longitude,
//                    _altitude = profileDTO.location.altitude
//                ),
//                gender = Gender.valueOf(profileDTO.gender),
//                sexualOrientation = SexualOrientation.valueOf(profileDTO.sexualOrientation),
//                relationshipType = RelationshipType.valueOf(profileDTO.relationshipType),
//                birthday = dateFormat.parse(profileDTO.birthday)?: Date(),
//                interests = profileDTO.interests.split(", "),
//                wall = profileDTO.wall.split(", "),
//                preferredImage = profileDTO.preferredImage,
//                maxDistance = profileDTO.maxDistance,
//                ageRange = profileDTO.ageRange.min to profileDTO.ageRange.max,
//                horoscope = profileDTO.horoscope?.let { Horoscope.valueOf(it) },
//                height = profileDTO.height,
//                weight = profileDTO.weight,
//                job = profileDTO.job,
//                education = profileDTO.education,
//                personalityType = profileDTO.personalityType,
//                pets = profileDTO.pets,
//                drinks = profileDTO.drinks?.let { Habits.valueOf(it) },
//                smokes = profileDTO.smokes?.let { Habits.valueOf(it) },
//                doesSports = profileDTO.doesSports?.let { Habits.valueOf(it) },
//                valuesAndBeliefs = profileDTO.valuesAndBeliefs?.let { Religion.valueOf(it) },
//                genderPriority = profileDTO.genderPriority?.let { Gender.valueOf(it) },
//                fact = profileDTO.fact
//            )
//        }
//    }
//}
//
//
//

