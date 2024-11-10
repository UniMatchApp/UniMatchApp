package com.ulpgc.uniMatch.data.domain.enum

enum class Interests {
    SPORTS,
    MUSIC,
    MOVIES,
    TRAVEL,
    READING,
    CUISINE,
    TECHNOLOGY,
    ART,
    PHOTOGRAPHY,
    FASHION,
    BOARD_GAMES,
    YOGA,
    HIKING,
    VOLUNTEERING,
    ENTREPRENEURSHIP
}

fun interestFromStringToEnum(interest: String?): Interests? {
    if (interest.isNullOrBlank()) return null

    return try {
        Interests.valueOf(interest.uppercase().replace(" ", "_"))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun fromEnumToString(interest: Interests?): String {
    return interest?.name?.lowercase()?.replace("_", " ")!!
}