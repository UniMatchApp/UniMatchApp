package com.ulpgc.uniMatch.data.infrastructure.services.matching

import com.ulpgc.uniMatch.data.application.services.MatchingService
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.database.dao.ProfileDao
import com.ulpgc.uniMatch.data.infrastructure.entities.MatchingEntity
import com.ulpgc.uniMatch.ui.screens.shared.safeRequest

class ApiMatchingService(
    private val matchingController: MatchingController,
    private val profileService: ProfileService,
    private val profileDao: ProfileDao
) : MatchingService {

    override suspend fun getMatchingUsers(limit: Int): Result<List<Profile>> {
        return safeRequest {
            // Realizar la llamada al controlador
            val response = matchingController.getMatchingUsers(limit)

            // Si la respuesta no es exitosa, retornar datos desde el DAO local
            if (!response.success) {
                return@safeRequest profileDao.getAllMatching().map(MatchingEntity::toDomain)
            }

            // Si la respuesta es exitosa, obtener los IDs de usuario
            val userIds = response.value.orEmpty()

            // Mapear los IDs a perfiles, ignorando cualquier fallo en la obtención de perfiles
            val profiles = userIds.mapNotNull { userId ->
                profileService.getProfile(userId).getOrNull()
            }

            // Guardar los perfiles en la base de datos local
            profiles.forEach { profile ->
                val matchingEntity = MatchingEntity.fromDomain(profile)
                profileDao.insertMatching(matchingEntity)
            }

            // Retornar la lista de perfiles
            return@safeRequest profiles
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
