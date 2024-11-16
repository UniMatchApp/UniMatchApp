package com.ulpgc.uniMatch

import android.app.Application
import android.util.Log
import com.ulpgc.uniMatch.data.application.api.ApiClient
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.database.AppDatabase
import com.ulpgc.uniMatch.data.infrastructure.services.chat.ApiChatService
import com.ulpgc.uniMatch.data.infrastructure.services.profile.MockProfileService

class UniMatchApplication : Application() {

    // Instancia de AppDatabase que se usará en toda la aplicación
    private val database by lazy { AppDatabase.getDatabase(this) }

    // Servicio de chat que se usará en la aplicación
    val apiChatService by lazy {
        ApiChatService(
            messageController = ApiClient.retrofit.create(MessageController::class.java),
            chatMessageDao = database.chatMessageDao(),
            profileService = MockProfileService()
        )
    }

    override fun onCreate() {
        super.onCreate()
        // Print the apiChatService instance
        Log.i("UniMatchApplication", "apiChatService: $apiChatService")
    }
}
