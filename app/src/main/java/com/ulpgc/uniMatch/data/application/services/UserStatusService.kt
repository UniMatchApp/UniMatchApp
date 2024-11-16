package com.ulpgc.uniMatch.data.application.services

interface UserStatusService {
    suspend fun setUserTyping(userId: String, targetUserId: String): Result<Unit>
    suspend fun setUserStoppedTyping(userId: String): Result<Unit>
    suspend fun getUserStatus(userId: String, targetUserId: String): Result<Unit>
}