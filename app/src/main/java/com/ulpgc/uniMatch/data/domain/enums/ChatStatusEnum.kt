package com.ulpgc.uniMatch.data.domain.enums

enum class ChatStatusEnum(val status: String) {
    ONLINE("ONLINE"),
    TYPING("TYPING"),
    OFFLINE("OFFLINE");

    companion object {
        fun fromString(status: String): ChatStatusEnum? {
            return entries.find { it.status == status }
        }
    }
}
