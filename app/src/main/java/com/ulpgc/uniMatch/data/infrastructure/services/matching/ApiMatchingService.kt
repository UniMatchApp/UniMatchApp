package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.MatchingEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ApiMatchingService(
    private val matchingController: MatchingController,
    private val profileService: ProfileService,
    private val profileDao: ProfileDao
) : MatchingService {

    override suspend fun getMatchingUsers(limit: Int): Flow<Profile> = flow {
        val response = safeRequest { matchingController.getMatchingUsers(limit) }
        val userIds = response.mapCatching { it.value }.getOrNull() ?: emptyList()

        for (userId in userIds) {
            val profileResult = profileService.getProfile(userId)
            val profile = profileResult.getOrNull()
            if (profile != null) {
                emit(profile)
                val matchingEntity = MatchingEntity.fromDomain(profile)
                profileDao.insertMatching(matchingEntity)
            }
        }
    }.catch { e ->
        profileDao.getAllMatching().forEach { matchingEntity ->
            emit(MatchingEntity.toDomain(matchingEntity))
        }
    }



    override suspend fun dislikeUser(dislikedUserId: String): Result<Unit> {
        return safeRequest {
            val response = matchingController.dislikeUser(dislikedUserId)

            // Si la respuesta no es exitosa, lanzar una excepción que será manejada por safeRequest
            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            // Si la respuesta es exitosa, eliminar el perfil del DAO
            profileDao.deleteProfile(dislikedUserId)
        }
    }

    override suspend fun likeUser(likedUserId: String): Result<Unit> {
        return safeRequest {
            val response = matchingController.likeUser(likedUserId)

            // Si la respuesta no es exitosa, lanzar una excepción que será manejada por safeRequest
            if (!response.success) {
                throw Exception(response.errorMessage ?: "Unknown error occurred")
            }

            // Si la respuesta es exitosa, eliminar el perfil del DAO
            profileDao.deleteProfile(likedUserId)
        }
    }

}
