package com.ulpgc.uniMatch.data.domain.enum

enum class MessageStatus(val status: String) {
    SENT("SENT"),
    READ("READ"),
    EDITED("EDITED"),
}

enum class DeletedMessageStatus(val status: String) {
    DELETED_FOR_BOTH("DELETED_FOR_BOTH"),
    DELETED_BY_RECIPIENT("DELETED_BY_RECIPIENT"),
    DELETED_BY_SENDER("DELETED_BY_SENDER"),
    NOT_DELETED("NOT_DELETED")
}