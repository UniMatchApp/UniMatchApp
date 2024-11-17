package com.ulpgc.uniMatch.data.domain.models

import java.util.Date


data class User(
    val id: String,
    val registrationDate: Date,
    var email: String,
    var blockedUsers: List<String> = emptyList(),
    var reportedUsers: List<String> = emptyList(),
    var registered: Boolean
)





