package com.ulpgc.uniMatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ulpgc.uniMatch.data.application.api.ApiClient
import com.ulpgc.uniMatch.data.infrastructure.controllers.AuthController
import com.ulpgc.uniMatch.data.infrastructure.controllers.MessageController
import com.ulpgc.uniMatch.data.infrastructure.controllers.ProfileController
import com.ulpgc.uniMatch.data.infrastructure.controllers.UserController
import com.ulpgc.uniMatch.data.infrastructure.secure.SecureStorage
import com.ulpgc.uniMatch.data.infrastructure.services.auth.ApiAuthService
import com.ulpgc.uniMatch.data.infrastructure.services.auth.MockAuthService
import com.ulpgc.uniMatch.data.infrastructure.services.chat.MockChatService
import com.ulpgc.uniMatch.data.infrastructure.services.matching.MockMatchingService
import com.ulpgc.uniMatch.data.infrastructure.services.profile.MockProfileService
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
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
        val authService = MockAuthService()
        val matchingService = MockMatchingService()
        val errorViewModel = ErrorViewModel()
        val authViewModel = AuthViewModel(authService, errorViewModel)
        val profileService = MockProfileService()
        val profileViewModel = ProfileViewModel(profileService, errorViewModel, authViewModel)
        val homeViewModel = HomeViewModel(profileService, errorViewModel, authViewModel, matchingService)

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
                        homeViewModel
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