package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.models.User
import java.util.Date

object UserMock {
    fun createMockLoggedUser(): User {
        return User(
            id = "1",
            registrationDate = Date(),
            email = "loggeduser@email.com",
            blockedUsers = listOf("blockedUser1", "blockedUser2")
        )
    }

    fun createMockUser(): User {
        return User(
            id = "user_id" + (0..100).random(),
            registrationDate = Date(),
            email = "mock@example.com",
            blockedUsers = listOf("blockedUser1", "blockedUser2")
        )
    }
}