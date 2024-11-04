package com.ulpgc.uniMatch.data.domain.enum

enum class MessageStatus {
    SENT, READ, EDITED
}

enum class DeletedMessageStatus {
    DELETED_FOR_BOTH, DELETED_BY_RECIPIENT, DELETED_BY_SENDER, NOT_DELETED
}