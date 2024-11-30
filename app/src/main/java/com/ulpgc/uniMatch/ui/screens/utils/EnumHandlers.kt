package com.ulpgc.uniMatch.ui.screens.utils


inline fun <reified T : Enum<T>> stringToEnum(value: String?): T? {
    if (value.isNullOrBlank()) return null
    return try {
        enumValueOf<T>(value.uppercase().replace(" ", "_"))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun <T : Enum<T>> enumToStringReplace(enumValue: T?): String? {
    return enumValue?.name
        ?.lowercase()
        ?.replace("_", " ")
}

fun <T : Enum<T>> enumToString(enumValue: T?): String? {
    return enumValue?.name
}

