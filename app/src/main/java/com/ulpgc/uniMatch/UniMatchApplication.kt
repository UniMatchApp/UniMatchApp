package com.ulpgc.uniMatch

import NotificationSocket
import UserStatusSocket
import android.app.Application
import android.util.Log
import com.ulpgc.uniMatch.data.application.api.ApiClient
import com.ulpgc.uniMatch.data.application.api.TokenProvider
import com.ulpgc.uniMatch.data.infrastructure.controllers.MatchingController
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.controllers.NotificationController
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.controllers.UserController
import com.ulpgc.uniMatch.data.infrastructure.database.AppDatabase
import com.ulpgc.uniMatch.data.infrastructure.events.WebSocketEventBus
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureTokenProvider
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

    val secureStorageDelegate = lazy { SecureStorage(this) }

    val secureStorage by secureStorageDelegate

    private val tokenProvider: TokenProvider by lazy {
        SecureTokenProvider(secureStorage)
    }

    private fun provideUserViewModel(): UserViewModel {
        return userViewModel
    }


    private val apiClient: ApiClient by lazy {
        try {
            ApiClient(tokenProvider) { provideUserViewModel() }
        } catch (e: Exception) {
            Log.e("UniMatchApplication", "Error initializing ApiClient: ${e.message}")
            throw e
        }
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
            messageController = apiClient.retrofit.create(MessageController::class.java),
            matchingController = apiClient.retrofit.create(MatchingController::class.java),
            chatMessageDao = database.chatMessageDao(),
            profileService = apiProfileService
        )
    }

    private val apiUserService by lazy {
        ApiUserService(
            userController = apiClient.retrofit.create(UserController::class.java),
            secureStorage = secureStorage
        )
    }

    private val apiProfileService by lazy {
        ApiProfileService(
            profileController = apiClient.retrofit.create(ProfileController::class.java),
            profileDao = database.profileDao(),
            contentResolver = contentResolver
        )
    }

    private val apiMatchingService by lazy {
        try {
            val matchingController = apiClient.retrofit.create(MatchingController::class.java)
            ApiMatchingService(
                matchingController = matchingController,
                profileService = apiProfileService,
                profileDao = database.profileDao()
            )
        } catch (e: Exception) {
            Log.e("UniMatchApplication", "Error initializing ApiMatchingService: ${e.message}")
            null
        }
    }

    private val apiNotificationService by lazy {
        ApiNotificationService(
            notificationController = apiClient.retrofit.create(NotificationController::class.java),
            notificationDao = database.notificationsDao()
        )
    }

    // ----------------------------------- Services -----------------------------------
    private val userService by lazy { apiUserService }
    private val profileService by lazy { apiProfileService }
    private val matchingService by lazy { apiMatchingService }
    private val notificationService by lazy { apiNotificationService }
    private val chatService by lazy { apiChatService }

    // ----------------------------------- ViewModels -----------------------------------
    val errorViewModel: ErrorViewModel by lazy { ErrorViewModel() }

    val userViewModel: UserViewModel by lazy {
        UserViewModel(userService, profileService, errorViewModel, secureStorage)
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

    val eventbus by lazy { WebSocketEventBus() }

    val notificationsViewModel: NotificationsViewModel by lazy {
        NotificationsViewModel(notificationService, errorViewModel, eventbus, userViewModel)
    }

    val chatViewModel: ChatViewModel by lazy {
        val userStatusSocket =
            userViewModel.userId?.let { UserStatusSocket("localhost", 8081, it, eventbus) }
        if (userStatusSocket == null) {
            throw IllegalStateException("UserViewModel is not initialized")
        }
        ChatViewModel(
            chatService,
            profileService,
            errorViewModel,
            userViewModel,
            eventbus,
            userStatusSocket
        )
    }


    fun initializeWebSocket(userId: String, eventbus: WebSocketEventBus) {
        val userStatusSocket = UserStatusSocket("localhost", 8081, userId, eventbus)
        val notificationsSocket = NotificationSocket("localhost", 8080, userId, eventbus)
        userStatusSocket.connect()
        notificationsSocket.connect()
    }

    override fun onCreate() {
        super.onCreate()

        Log.i("UniMatchApplication", "Application initialized")

        // Eliminar manualmente el archivo de la base de datos
        val db = applicationContext.getDatabasePath("uniMatch_database")
        if (db.exists()) {
            db.delete()
        }
    }
}
