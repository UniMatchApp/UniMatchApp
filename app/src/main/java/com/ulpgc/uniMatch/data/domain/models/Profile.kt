package com.ulpgc.uniMatch.data.domain.models

import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
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
    var horoscope: Horoscope?,
    var height: Int?,
    var weight: Int?,
    var job: String?,
    var education: String?,
    var personalityType: String?,
    var pets: String?,
    var drinks: Habits?,
    var smokes: Habits?,
    var doesSports: Habits?,
    var valuesAndBeliefs: Religion?,
    var genderPriority: Gender?,
    var fact: String?
) {

}