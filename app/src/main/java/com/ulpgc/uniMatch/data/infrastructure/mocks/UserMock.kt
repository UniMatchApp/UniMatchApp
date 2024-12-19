package com.ulpgc.uniMatch.data.infrastructure.mocks

import com.ulpgc.uniMatch.data.domain.models.User
import java.util.Date

object UserMock {
    fun createMockLoggedUser(): User {
        return User(
            id = "d449400b-1716-4474-b8aa-2d058542270c",
//            id = "mock_user_id",
            registrationDate = Date(),
            email = "loggeduser@email.com",
            blockedUsers = listOf("blockedUser1", "blockedUser2"),
            registered = true
        )
    }

    fun createMockUser(): User {
        return User(
            id = "user_id" + (0..100).random(),
            registrationDate = Date(),
            email = "mock@example.com",
            blockedUsers = listOf("blockedUser1", "blockedUser2"),
            registered = true
        )
    }
}