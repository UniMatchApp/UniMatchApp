package com.ulpgc.uniMatch.data.domain.enum

enum class Jobs {
    STUDENT,
    ENGINEER,
    DOCTOR,
    TEACHER,
    ARTIST,
    PHOTOGRAPHER,
    MUSICIAN,
    WRITER,
    ENTREPRENEUR,
    SCIENTIST,
    LAWYER,
    ACCOUNTANT,
    PSYCHOLOGIST,
    THERAPIST,
    CONSULTANT,
    MANAGER,
    DEVELOPER,
    ANALYST,
    MARKETER,
    TRADER,
    OTHER
}

fun jobFromStringToEnum(job: String?): Jobs? {
    if (job.isNullOrBlank()) return null

    return try {
        Jobs.valueOf(job.uppercase())
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun fromEnumToString(job: Jobs?): String? {
    return job?.name
}
