package com.ulpgc.uniMatch.data.infrastructure.controllers.requestHelpers

data class StringRequest(val newContent: String?)
data class IntRequest(val newContent: Int?)
data class ListRequest(val newContent: List<String>)
data class SurveyRequest(val surveys: List<CreateSurveyDTO>)
data class AgeRangeRequest(val min: Int, val max: Int)
data class LocationRequest(val latitude: Double?, val longitude: Double?, val altitude: Double?)

data class CreateSurveyDTO(
    val title: String,
    val options: List<String>
)