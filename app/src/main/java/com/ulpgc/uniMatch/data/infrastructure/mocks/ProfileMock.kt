package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.domain.valueObjects.Location
import java.util.Date
import kotlin.random.Random

object ProfileMock {

    private val names = listOf("Alice", "Bob", "Charlie", "Diana", "Edward")
    private val jobs = listOf("Engineer", "Teacher", "Artist", "Doctor", "Lawyer")
    private val educationLevels = listOf("High School", "Bachelor's", "Master's", "PhD")
    private val personalityTypes = listOf("INTJ", "ENFP", "ISTJ", "ESFP")
    private val petPreferences = listOf("Dogs", "Cats", "None")
    private val interests = listOf("Reading", "Traveling", "Cooking", "Gaming")
    private val imageUrls = listOf(
        "https://i.pinimg.com/736x/83/ca/48/83ca48f723206997ccc852915f3af4d2.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6dy7MLouvsr5o6Iur7puBkIXDy-Vue7KkqQ&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ7TUuvtYtiRUGDqgGLyI2ztgsKFvOpqB-Jbw&s"
    )

    fun createMockProfile(): Profile {
        return Profile(
            userId = "Mock Id ${Random.nextInt(1, 1000)}",
            name = names.random(),
            age = Random.nextInt(18, 60),
            aboutMe = "I am ${names.random()}, a ${jobs.random()} who loves ${interests.random()}.",
            location = Location(
                Random.nextDouble(27.0, 29.0),
                Random.nextDouble(14.0, 16.0)
            ),
            gender = Gender.entries.toTypedArray().random(),
            sexualOrientation = SexualOrientation.entries.toTypedArray().random(),
            relationshipType = RelationshipType.entries.toTypedArray().random(),
            birthday = Date(),
            interests = List(Random.nextInt(1, 4)) { interests.random() },
            wall = imageUrls,
            preferredImage = imageUrls.random(),
            maxDistance = Random.nextInt(10, 100),
            ageRange = Random.nextInt(18, 40) to Random.nextInt(41, 100),
            horoscope = Horoscope.entries.toTypedArray().random(),
            height = Random.nextInt(150, 200),
            weight = Random.nextInt(50, 100),
            job = jobs.random(),
            education = educationLevels.random(),
            personalityType = personalityTypes.random(),
            pets = petPreferences.random(),
            drinks = listOf("Yes", "No").random(),
            smokes = listOf("Yes", "No").random(),
            doesSports = listOf("Yes", "No").random(),
            valuesAndBeliefs = "I believe in ${interests.random()} and respect.",
            genderPriority = Gender.entries.toTypedArray().random(),
            fact = "I once ${interests.random()} for a whole day!"
        )
    }

    fun createMockProfiles(count: Int): List<Profile> {
        return List(count) { createMockProfile() }
    }

    fun getUserProfileByChatId(chatId: String): Profile {
        return createMockProfile()
    }
}
