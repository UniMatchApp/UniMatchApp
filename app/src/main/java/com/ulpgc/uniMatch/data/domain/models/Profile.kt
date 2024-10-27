package com.ulpgc.uniMatch.data.domain.models

import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.Horoscope
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.enum.SexualOrientation
import com.ulpgc.uniMatch.data.domain.valueObjects.Location
import java.util.Date

data class Profile(
    val profileId: String = "",
    val userId: String,
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
    var maxDistance: Int = 50,
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

}