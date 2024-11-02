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
    private val interests = listOf("Reading", "Traveling", "Cooking", "Gaming", "Hiking", "Photography", "Music")
    private val imageUrls = listOf(
        "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/marilola800x800.png?itok=zMv5SjYa",
        "https://imagenes2.fotos.europapress.es/preview/5309176.jpg?s=1000",
        "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/IMG-20230602-WA0007-B.jpg?itok=Xwradmzk",
        "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/daniel800-B.png?itok=JAJrobhA",
        "https://media.discordapp.net/attachments/885485159376384010/1300910611500367872/image.png?ex=67228eeb&is=67213d6b&hm=c050fef9dfd6a88ebd460a24630325ad0b739ee3aa8c7e86c1fe5ad1c7840ef7&=&format=webp&quality=lossless",
        "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/Foto%20oficial%20Jose%20Carlos%20Rodriguez%2005.jpeg?itok=eFOM2TXu",
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
            interests = List(Random.nextInt(1, 8)) { interests.random() },
            wall = imageUrls.shuffled().take(Random.nextInt(3, 6)),
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

    fun createNamedProfiles(): List<Profile> {
        val names = listOf("Marilola", "Adrián Pèñate", "Jose Carlos")
        val images = listOf(
            listOf(
                "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/marilola800x800.png?itok=zMv5SjYa",
                "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/Marilola_Afonso_Su%C3%A1rez_0.jpg?itok=jHco3IEU",
                "https://mt4sd.ulpgc.es/wp-content/uploads/2018/03/Marilola.png"
            ),
            listOf(
                "https://imagenes2.fotos.europapress.es/preview/5309176.jpg?s=1000",
                "https://i1.rgstatic.net/ii/profile.image/11431281127138961-1678964775589_Q512/Adrian-Penate-Sanchez.jpg"
            ),
            listOf(
                "https://www.eii.ulpgc.es/sites/default/files/styles/height/public/team/Foto%20oficial%20Jose%20Carlos%20Rodriguez%2005.jpeg?itok=eFOM2TXu",
                "https://estaticos-cdn.prensaiberica.es/clip/53ccde93-d373-422a-8426-4dbee8ffbcf3_16-9-aspect-ratio_default_0.jpg"
            ),
        )

        return names.mapIndexed { index, name ->
            Profile(
                userId = "Mock Id ${Random.nextInt(1, 1000)}",
                name = name,
                age = Random.nextInt(18, 60),
                aboutMe = "I am $name, a ${jobs.random()} who loves ${interests.random()}.",
                location = Location(
                    Random.nextDouble(27.0, 29.0),
                    Random.nextDouble(14.0, 16.0)
                ),
                gender = Gender.entries.toTypedArray().random(),
                sexualOrientation = SexualOrientation.entries.toTypedArray().random(),
                relationshipType = RelationshipType.entries.toTypedArray().random(),
                birthday = Date(),
                interests = List(Random.nextInt(1, 8)) { interests.random() },
                wall = images[index],
                preferredImage = images[index].first(),
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
    }
}
