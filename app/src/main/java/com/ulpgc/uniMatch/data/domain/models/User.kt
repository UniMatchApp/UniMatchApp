package com.ulpgc.uniMatch.data.domain.models

import com.ulpgc.uniMatch.data.infrastructure.mocks.ProfileMock
import java.util.Date


data class User(
    val id: String,
    val registrationDate: Date,
    var email: String,
    var blockedUsers: List<String> = emptyList(),
) {

}





