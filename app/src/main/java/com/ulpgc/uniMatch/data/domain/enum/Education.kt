package com.ulpgc.uniMatch.data.domain.enum

enum class Education {
    ACCOUNTING,
    AGRICULTURE,
    ANIMAL_SCIENCE,
    ARCHITECTURE,
    ART,
    BIOCHEMISTRY,
    BIOLOGY,
    BUSINESS_ADMINISTRATION,
    CHEMICAL_ENGINEERING,
    CHEMISTRY,
    CIVIL_ENGINEERING,
    COMMUNICATION_SCIENCES,
    COMPUTER_SCIENCE,
    CRIMINOLOGY,
    ECONOMICS,
    EDUCATION,
    ELECTRICAL_ENGINEERING,
    ENGLISH,
    ENVIRONMENTAL_SCIENCE,
    FINANCE,
    FINE_ARTS,
    GEOGRAPHY,
    GEOLOGY,
    HISTORY,
    HOSPITALITY_MANAGEMENT,
    HUMAN_RESOURCES,
    INDUSTRIAL_ENGINEERING,
    INFORMATION_SYSTEMS,
    INTERIOR_DESIGN,
    INTERNATIONAL_RELATIONS,
    JOURNALISM,
    LAW,
    LINGUISTICS,
    MARKETING,
    MATHEMATICS,
    MEDICINE,
    MECHANICAL_ENGINEERING,
    NURSING,
    OPTOMETRY,
    PHARMACY,
    PHILOSOPHY,
    PHYSICS,
    POLITICAL_SCIENCE,
    PSYCHOLOGY,
    PUBLIC_RELATIONS,
    RADIOLOGY,
    SOCIAL_SCIENCES,
    SOCIOLOGY,
    TELECOMMUNICATIONS_ENGINEERING,
    VETERINARY,
    ZOOLOGY
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