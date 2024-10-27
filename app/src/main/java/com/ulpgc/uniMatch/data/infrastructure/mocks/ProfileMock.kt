package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.valueObjects.Location
import java.util.Date

object ProfileMock {
    fun createMockProfile(): Profile {
        return Profile(
            userId = "Mock Id",
            name = "Mock Name",
            age = 25,
            aboutMe = "This is a mock profile.",
            location = Location(28.0, 15.0),
            gender = Gender.OTHER,
            sexualOrientation = SexualOrientation.OTHER,
            relationshipType = RelationshipType.OTHER,
            birthday = Date(),
            interests = listOf("mockInterest1", "mockInterest2"),
            wall = listOf("mockImage1", "mockImage2"),
            preferredImage = "https://i.pinimg.com/736x/83/ca/48/83ca48f723206997ccc852915f3af4d2.jpg",
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

    fun getUserProfileByChatId(chatId: String): Profile {
        return createMockProfile()
    }
}