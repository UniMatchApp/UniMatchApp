package com.ulpgc.uniMatch.data.domain.models

import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Habits
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.Jobs
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.Religion
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.valueObjects.Location
import java.util.Date

data class Profile(
    val profileId: String = "",
    val userId: String,
    var name: String,
    var age: Int,
    var aboutMe: String,
    var location: Location,
    var gender: Gender,
    var sexualOrientation: SexualOrientation,
    var relationshipType: RelationshipType,
    var birthday: Date,
    var interests: List<String> = emptyList(),
    var wall: List<String> = emptyList(),
    var preferredImage: String = wall.firstOrNull() ?: "",
    var maxDistance: Int = 50,
    var ageRange: Pair<Int, Int> = 18 to 100,
    var horoscope: Horoscope? = null,
    var height: Int? = null,
    var weight: Int? = null,
    var job: Jobs? = null,
    var education: String? = null,
    var personalityType: String? = null,
    var pets: String? = null,
    var drinks: Habits? = null,
    var smokes: Habits? = null,
    var doesSports: Habits? = null,
    var valuesAndBeliefs: Religion? = null,
    var genderPriority: Gender? = null,
    var fact: String? = null
) {

}