package com.ulpgc.uniMatch

import NotificationsSocket
import UserStatusSocket
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ulpgc.uniMatch.data.infrastructure.events.WebSocketEventBus
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorState
import com.ulpgc.uniMatch.ui.components.ErrorDialog
import com.ulpgc.uniMatch.ui.screens.AuthScreen
import com.ulpgc.uniMatch.ui.screens.CoreScreen
import com.ulpgc.uniMatch.ui.theme.UniMatchTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as UniMatchApplication
        enableEdgeToEdge()
        setContent {
            UniMatchTheme {
                // Observar el estado de autenticación usando collectAsState
                val authState by app.userViewModel.authState.collectAsState()
                val errorState by app.errorViewModel.errorState.collectAsState()


                when (authState) {
                    is AuthState.Authenticated -> {
                        val userId = (authState as AuthState.Authenticated).user.id
                        initializeWebSocket(userId)
                        CoreScreen(
                            app.userViewModel,
                            app.chatViewModel,
                            app.profileViewModel,
                            app.homeViewModel,
                            app.notificationsViewModel
                        )
                    }

                    is AuthState.Unauthenticated -> AuthScreen(
                        app.userViewModel,
                        app.errorViewModel
                    )
                }

                if (errorState is ErrorState.Error) {
                    ErrorDialog(
                        errorMessage = (errorState as ErrorState.Error).message,
                        onDismiss = {
                            app.errorViewModel.clearError()
//                            app.userViewModel.logout()
                        }
                    )
                }
            }
        }
    }

    private fun initializeWebSocket(userId: String) {
        val webSocketEventBus = WebSocketEventBus()
        val userStatusSocket = UserStatusSocket("localhost", 8081, userId, webSocketEventBus)
        val notificationsSocket = NotificationsSocket("localhost", 8080, userId)
        userStatusSocket.connect()
        notificationsSocket.connect()
    }
}