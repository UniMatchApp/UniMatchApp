package com.ulpgc.uniMatch

import NotificationSocket
import UserStatusSocket
import android.app.Application
import android.util.Log
import com.ulpgc.uniMatch.data.application.api.ApiClient
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.controllers.NotificationController
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.controllers.UserController
import com.ulpgc.uniMatch.data.infrastructure.database.AppDatabase
import com.ulpgc.uniMatch.data.infrastructure.events.WebSocketEventBus
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage
import com.ulpgc.uniMatch.data.infrastructure.services.chat.ApiChatService
import com.ulpgc.uniMatch.data.infrastructure.services.chat.MockChatService
import com.ulpgc.uniMatch.data.infrastructure.services.matching.ApiMatchingService
import com.ulpgc.uniMatch.data.infrastructure.services.matching.MockMatchingService
import com.ulpgc.uniMatch.data.infrastructure.services.notification.ApiNotificationService
import com.ulpgc.uniMatch.data.infrastructure.services.notification.MockNotificationService
import com.ulpgc.uniMatch.data.infrastructure.services.profile.ApiProfileService
import com.ulpgc.uniMatch.data.infrastructure.services.profile.MockProfileService
import com.ulpgc.uniMatch.data.infrastructure.services.user.ApiUserService
import com.ulpgc.uniMatch.data.infrastructure.services.user.MockUserService
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.NotificationsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.PermissionsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel

class UniMatchApplication : Application() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val secureStorage: SecureStorage by lazy {
        SecureStorage(this)
    }

    // ----------------------------------- Services -----------------------------------
    private val userService by lazy { apiUserService }
    private val profileService by lazy { apiProfileService }
    private val matchingService by lazy { apiMatchingService }
    private val notificationService by lazy { mockNotificationService }
    private val chatService by lazy { apiChatService }


    // ----------------------------------- ViewModels -----------------------------------
    val errorViewModel: ErrorViewModel by lazy {
        ErrorViewModel()
    }

    val userViewModel: UserViewModel by lazy {
        UserViewModel(userService, profileService, errorViewModel)
    }

    val profileViewModel: ProfileViewModel by lazy {
        ProfileViewModel(profileService, errorViewModel, userViewModel)
    }

    val homeViewModel: HomeViewModel by lazy {
        val matchingService = matchingService ?: throw IllegalStateException("Matching service is not available")
        HomeViewModel(
            profileService,
            errorViewModel,
            userViewModel,
            matchingService,
            userService
        )
    }


    val eventbus by lazy {
        WebSocketEventBus()
    }

    val permissionsViewModel by lazy {
        PermissionsViewModel()
    }

    val notificationsViewModel: NotificationsViewModel by lazy {
        NotificationsViewModel(notificationService, errorViewModel, eventbus, userViewModel)
    }

    val chatViewModel: ChatViewModel by lazy {
        ChatViewModel(chatService, profileService, errorViewModel, userViewModel, eventbus)
    }

    // ----------------------------------- Mock services -----------------------------------
    private val mockMatchingService = MockMatchingService()
    private val mockNotificationService = MockNotificationService()
    private val mockProfileService = MockProfileService()
    private val mockUserService = MockUserService()
    private val mockChatService = MockChatService()


    // ----------------------------------- API services -----------------------------------
    private val apiChatService by lazy {
        ApiChatService(
            messageController = ApiClient.retrofit.create(MessageController::class.java),
            chatMessageDao = database.chatMessageDao(),
            profileService = MockProfileService()
        )
    }

    private val apiUserService by lazy {
        ApiUserService(
            userController = ApiClient.retrofit.create(UserController::class.java),
            secureStorage = secureStorage
        )
    }

    private val apiProfileService by lazy {
        ApiProfileService(
            profileController = ApiClient.retrofit.create(ProfileController::class.java),
            profileDao = database.profileDao(),
            contentResolver = contentResolver
        )
    }

    private val apiMatchingService: ApiMatchingService? by lazy {
        try {
            val matchingController = ApiClient.retrofit.create(MatchingController::class.java)
            val profileService = apiProfileService

            ApiMatchingService(
                matchingController = matchingController,
                profileService = profileService,
                profileDao = database.profileDao()
            )
        } catch (e: Exception) {
            Log.e("UniMatchApplication", "Error initializing ApiMatchingService: ${e.message}")
            null
        }
    }

    private val apiNotificationService = ApiNotificationService(
        notificationController = ApiClient.retrofit.create(NotificationController::class.java)
    )

    public fun initializeWebSocket(userId: String, eventbus: WebSocketEventBus) {
        val userStatusSocket = UserStatusSocket("localhost", 8081, userId, eventbus)
        val notificationsSocket = NotificationSocket("localhost", 8080, userId, eventbus)
        userStatusSocket.connect()
        notificationsSocket.connect()
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("UniMatchApplication", "Application initialized")

        // Eliminar manualmente el archivo de la base de datos
//        val db= applicationContext.getDatabasePath("uniMatch_database")
//        if (db.exists()) {
//            db.delete()
//        }

    }
}
