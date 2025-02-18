package com.ulpgc.uniMatch.data.application.DTO


import com.ulpgc.uniMatch.data.domain.Place
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Prediction(
    val description: String = "",
    val matched_substrings: List<MatchedSubstring> = listOf(),
    val place_id: String = "",
    val reference: String = "",
    val structured_formatting: StructuredFormatting = StructuredFormatting(
        mainText = TODO(),
        mainTextMatchedSubstrings = TODO(),
        secondaryText = TODO()
    ),
    val terms: List<Term> = listOf(),
    val types: List<String> = listOf()
) {
    fun toPlace(lat: Double = 0.0, lng: Double = 0.0) = Place(
        id = place_id,
        name = description,
        longitud = lng,
        latitud = lat
    )
}