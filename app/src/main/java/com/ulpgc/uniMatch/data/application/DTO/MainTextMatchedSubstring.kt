package com.ulpgc.uniMatch.data.application.DTO


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainTextMatchedSubstring(
    @SerialName("length")
    val length: Int,
    @SerialName("offset")
    val offset: Int
)