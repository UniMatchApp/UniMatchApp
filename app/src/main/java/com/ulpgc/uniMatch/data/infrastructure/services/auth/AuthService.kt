package com.ulpgc.uniMatch.data.infrastructure.services.auth

import com.ulpgc.uniMatch.data.application.LoginResponse
import com.ulpgc.uniMatch.data.application.RegisterResponse

interface AuthService {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun register(email: String, password: String): Result<RegisterResponse>
}