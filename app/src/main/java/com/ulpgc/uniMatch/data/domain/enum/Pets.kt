package com.ulpgc.uniMatch.data.domain.enum

enum class Pets {
    NONE,
    DOG,
    CAT,
    RODENT,
    FISH,
    OTHER
}

fun fromStringToEnum(pet: String?): Pets? {
    if (pet.isNullOrBlank()) return null

    return try {
        Pets.valueOf(pet.uppercase())
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun fromEnumToString(pet: Pets?): String? {
    return pet?.name?.lowercase()
}