package com.ulpgc.uniMatch.data.domain.enums


enum class ReceptionStatus(val status: String) {
    FAILED("FAILED"),
    SENDING("SENDING"),
    SENT("SENT"),
    RECEIVED("RECEIVED"),
    READ("READ"),
}

enum class ContentStatus(val status: String) {
    NOT_EDITED("NOT_EDITED"),
    EDITED("EDITED"),
}

enum class DeletedMessageStatus(val status: String) {
    DELETED_FOR_BOTH("DELETED_FOR_BOTH"),
    DELETED_BY_RECIPIENT("DELETED_BY_RECIPIENT"),
    DELETED_BY_SENDER("DELETED_BY_SENDER"),
    NOT_DELETED("NOT_DELETED"),
}