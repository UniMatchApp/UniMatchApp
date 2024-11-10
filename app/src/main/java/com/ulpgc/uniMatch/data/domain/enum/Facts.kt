package com.ulpgc.uniMatch.data.domain.enum

enum class Facts {
    THE_EARTH_IS_NOT_A_PERFECT_SPHERE,
    LIGHT_TRAVELS_FASTER_THAN_SOUND,
    OCTOPUSES_HAVE_THREE_HEARTS,
    WATER_CAN_BOIL_AND_FREEZE_AT_THE_SAME_TIME,
    SHARKS_HAVE_BEEN_AROUND_LONGER_THAN_TREES,
    A_DAY_ON_VENUS_IS_LONGER_THAN_A_YEAR_ON_VENUS
}

fun factFromStringToEnum(fact: String?): Facts? {
    if (fact.isNullOrBlank()) return null
    return try {
        // Convierte los espacios a guiones bajos para que coincida con el nombre en el enum
        Facts.valueOf(fact.uppercase().replace(" ", "_"))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun fromEnumToString(fact: Facts?): String? {
    // Convierte los guiones bajos a espacios al transformar a String
    return fact?.name?.lowercase()?.replace("_", " ")
}