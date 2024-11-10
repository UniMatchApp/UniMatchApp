package com.ulpgc.uniMatch.data.domain.enum

enum class Personality {
    INTROVERTED,
    EXTROVERTED,
    AMBIVERTED
}

fun personalitFromStringToEnum(personality: String?): Personality? {
    if (personality.isNullOrBlank()) return null

    return try {
        Personality.valueOf(personality.uppercase())
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun fromEnumToString(personality: Personality?): String? {
    return personality?.name?.lowercase()
}