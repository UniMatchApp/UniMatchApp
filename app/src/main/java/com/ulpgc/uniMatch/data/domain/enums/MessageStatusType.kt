package com.ulpgc.uniMatch.data.domain.enums

enum class MessageStatusType {
    FAILED, SENDING, SENT, RECEIVED, READ,
}

enum class DeletedMessageStatusType {
    DELETED_FOR_BOTH, DELETED_BY_RECIPIENT, DELETED_BY_SENDER, NOT_DELETED
}