package com.ulpgc.uniMatch.data.infrastructure.entities

import android.location.Location
import java.util.Date


data class User(
    val id: String,
    val code: String,
    val registrationDate: Date,
    var email: String,
    var blockedUsers: List<String> = emptyList(),
    var profile: Profile
) {
    companion object {
        fun createMockLoggedUser(): User {
            return User(
                id = "mock_user_id",
                code = "mockCode",
                registrationDate = Date(),
                email = "loggeduser@email.com",
                blockedUsers = listOf("blockedUser1", "blockedUser2"),
                profile = Profile.createMockProfile()
            )
        }

        fun createMockUser(): User {
            return User(
                id = "user_id" + (0..100).random(),
                code = "mockCode",
                registrationDate = Date(),
                email = "mock@example.com",
                blockedUsers = listOf("blockedUser1", "blockedUser2"),
                profile = Profile.createMockProfile()
            )
        }
    }


}


data class Profile(
    val name: String,
    val age: Int,
    val aboutMe: String,
    val location: Location,
    val gender: Gender,
    val sexualOrientation: SexualOrientation,
    val relationshipType: RelationshipType,
    val birthday: Date,
    val interests: List<String> = emptyList(),
    val wall: List<String> = emptyList(),
    var preferredImage: String = wall.firstOrNull() ?: "",
    var maxDistance: Int = 50, // Distancia máxima por defecto
    var ageRange: Pair<Int, Int> = 18 to 100,
    var horoscope: Horoscope? = null,
    var height: Int? = null,
    var weight: Int? = null,
    var job: String? = null,
    var education: String? = null,
    var personalityType: String? = null,
    var pets: String? = null,
    var drinks: String? = null,
    var smokes: String? = null,
    var doesSports: String? = null,
    var valuesAndBeliefs: String? = null,
    var genderPriority: Gender? = null,
    var fact: String? = null
) {
    companion object {
        fun createMockProfile(): Profile {
            return Profile(
                name = "Mock Name",
                age = 25,
                aboutMe = "This is a mock profile.",
                location = Location("mockProvider").apply {
                    latitude = 0.0
                    longitude = 0.0
                },
                gender = Gender.OTHER,
                sexualOrientation = SexualOrientation.OTHER,
                relationshipType = RelationshipType.OTHER,
                birthday = Date(),
                interests = listOf("mockInterest1", "mockInterest2"),
                wall = listOf("mockImage1", "mockImage2"),
                preferredImage = "mockImage1",
                maxDistance = 50,
                ageRange = 18 to 100,
                horoscope = Horoscope.ARIES,
                height = 170,
                weight = 70,
                job = "Mock Job",
                education = "Mock Education",
                personalityType = "Mock Personality",
                pets = "Mock Pets",
                drinks = "Mock Drinks",
                smokes = "Mock Smokes",
                doesSports = "Mock Sports",
                valuesAndBeliefs = "Mock Values",
                genderPriority = Gender.OTHER,
                fact = "Mock Fact"
            )
        }
    }
}


enum class Gender {
    MALE, FEMALE, OTHER
}

enum class SexualOrientation {
    HETEROSEXUAL, HOMOSEXUAL, BISEXUAL, OTHER
}

enum class RelationshipType {
    FRIENDSHIP, DATING, LONG_TERM, OTHER
}

enum class Horoscope {
    ARIES, TAURUS, GEMINI, CANCER, LEO, VIRGO, LIBRA, SCORPIO, SAGITTARIUS, CAPRICORN, AQUARIUS, PISCES
}

