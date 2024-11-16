package com.ulpgc.uniMatch

import NotificationSocket
import UserStatusSocket
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ulpgc.uniMatch.data.application.api.ApiClient
import com.ulpgc.uniMatch.data.infrastructure.controllers.UserController
import com.ulpgc.uniMatch.data.infrastructure.events.WebSocketEventBus
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage
import com.ulpgc.uniMatch.data.infrastructure.services.chat.MockChatService
import com.ulpgc.uniMatch.data.infrastructure.services.matching.MockMatchingService
import com.ulpgc.uniMatch.data.infrastructure.services.notification.MockNotificationService
import com.ulpgc.uniMatch.data.infrastructure.services.profile.MockProfileService
import com.ulpgc.uniMatch.data.infrastructure.services.user.ApiUserService
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.NotificationsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.components.ErrorDialog
import com.ulpgc.uniMatch.ui.screens.AuthScreen
import com.ulpgc.uniMatch.ui.screens.CoreScreen
import com.ulpgc.uniMatch.ui.theme.UniMatchTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual DI: Crear instancias de las dependencias
//        ApiClient.retrofit.create(AuthController::class.java)
//        ApiClient.retrofit.create(MessageController::class.java)
//        ApiClient.retrofit.create(ProfileController::class.java)
//        ApiClient.retrofit.create(UserController::class.java)
//        val authService = ApiAuthService(
//            authController = ApiClient.retrofit.create(AuthController::class.java),
//            secureStorage = SecureStorage(this)
//        )

        val userService = ApiUserService(
            userController = ApiClient.retrofit.create(UserController::class.java),
            secureStorage = SecureStorage(this)
        )

        val webSocketEventBus = WebSocketEventBus()

//        val userService = MockUserService()
        val matchingService = MockMatchingService()
        val notificationService = MockNotificationService()
        val errorViewModel = ErrorViewModel()
        val profileService = MockProfileService()
        val userViewModel = UserViewModel(userService, profileService, errorViewModel)
        val profileViewModel = ProfileViewModel(profileService, errorViewModel, userViewModel)
        val homeViewModel = HomeViewModel(
            profileService,
            errorViewModel,
            userViewModel,
            matchingService,
            userService
        )
        val notificationsViewModel =
            NotificationsViewModel(notificationService, errorViewModel, webSocketEventBus, userViewModel)

        val chatService = MockChatService()
        val chatViewModel =
            ChatViewModel(chatService, profileService, errorViewModel, userViewModel, webSocketEventBus)

        enableEdgeToEdge()

        setContent {
            UniMatchTheme {
                // Observar el estado de autenticación usando collectAsState
                val authState by userViewModel.authState.collectAsState()
                val errorState by errorViewModel.errorState.collectAsState()


                when (authState) {
                    is AuthState.Authenticated -> {
                        val userId = (authState as AuthState.Authenticated).user.id
                        initializeWebSocket(userId, webSocketEventBus)
                        CoreScreen(
                            userViewModel,
                            chatViewModel,
                            profileViewModel,
                            homeViewModel,
                            notificationsViewModel
                        )
                    }

                    is AuthState.Unauthenticated -> AuthScreen(userViewModel, errorViewModel)
                }

                if (errorState is ErrorState.Error) {
                    ErrorDialog(
                        errorMessage = (errorState as ErrorState.Error).message,
                        onDismiss = {
                            errorViewModel.clearError()
                            userViewModel.logout()
                        }
                    )
                }
            }
        }
    }

    private fun initializeWebSocket(userId: String, webSocketEventBus: WebSocketEventBus) {
        val userStatusSocket = UserStatusSocket("localhost", 8081, userId, webSocketEventBus)
        val notificationsSocket = NotificationSocket("localhost", 8081, userId, webSocketEventBus)
        userStatusSocket.connect()
        notificationsSocket.connect()
    }
}