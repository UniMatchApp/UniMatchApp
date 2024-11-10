package com.ulpgc.uniMatch.data.domain.enum

enum class Education {
    NO_EDUCATION,
    PRIMARY_EDUCATION,
    SECONDARY_EDUCATION,
    HIGH_SCHOOL_DIPLOMA,
    VOCATIONAL_TRAINING,
    UNDERGRADUATE_DEGREE,
    MASTER_DEGREE,
    DOCTORATE
}

fun educationFromStringToEnum(education: String?): Education? {
    if (education.isNullOrBlank()) return null

    return try {
        // Convierte los espacios a guiones bajos para que coincida con el nombre en el enum
        Education.valueOf(education.uppercase().replace(" ", "_"))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun fromEnumToString(education: Education?): String? {
    // Convierte los guiones bajos a espacios al transformar a String
    return education?.name?.lowercase()?.replace("_", " ")
}