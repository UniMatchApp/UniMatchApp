package com.ulpgc.uniMatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ulpgc.uniMatch.data.infrastructure.services.user.MockUserService
import com.ulpgc.uniMatch.data.infrastructure.services.chat.MockChatService
import com.ulpgc.uniMatch.data.infrastructure.services.matching.MockMatchingService
import com.ulpgc.uniMatch.data.infrastructure.services.notification.MockNotificationService
import com.ulpgc.uniMatch.data.infrastructure.services.profile.MockProfileService
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.NotificationsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
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
        val userService = MockUserService()
        val matchingService = MockMatchingService()
        val notificationService = MockNotificationService()
        val errorViewModel = ErrorViewModel()
        val authViewModel = AuthViewModel(userService, errorViewModel)
        val profileService = MockProfileService()
        val profileViewModel = ProfileViewModel(profileService, errorViewModel, authViewModel)
        val homeViewModel = HomeViewModel(profileService, errorViewModel, authViewModel, matchingService, userService)
        val notificationsViewModel = NotificationsViewModel(notificationService, errorViewModel, authViewModel)

        val chatService = MockChatService()
        val chatViewModel = ChatViewModel(chatService, errorViewModel, authViewModel)

        enableEdgeToEdge()

        setContent {
            UniMatchTheme {
                // Observar el estado de autenticación usando collectAsState
                val authState by authViewModel.authState.collectAsState()
                val errorState by errorViewModel.errorState.collectAsState()

                // Decidir si mostrar AuthScreen o CoreScreen basado en el estado
                when (authState) {
                    is AuthState.Authenticated -> CoreScreen(
                        authViewModel,
                        chatViewModel,
                        profileViewModel,
                        homeViewModel,
                        notificationsViewModel
                    )
                    is AuthState.Unauthenticated -> AuthScreen(authViewModel)
                }

                // Mostrar popup cuando hay un error en el estado de autenticación
                if (errorState is ErrorState.Error) {
                    ErrorDialog(
                        errorMessage = (errorState as ErrorState.Error).message,
                        onDismiss = {
                            errorViewModel.clearError()
                            authViewModel.logout()
                        }
                    )
                }
            }
        }
    }
}


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Manual DI: Crear instancias de las dependencias
//        val authService = ApiClient.retrofit.create(AuthService::class.java)
//        val inMemoryAuthRepository = InMemoryAuthRepository(authService)
//        val authViewModel = AuthViewModel(inMemoryAuthRepository)
//        enableEdgeToEdge()
//
//        setContent {
//            UniMatchTheme {
//                // Pasar el authViewModel manualmente a la pantalla
//                AuthScreen(authViewModel = authViewModel)
//            }
//        }
//    }