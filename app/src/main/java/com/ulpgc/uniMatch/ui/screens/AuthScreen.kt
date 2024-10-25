package com.ulpgc.uniMatch.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.screens.auth.LoginScreen
import com.ulpgc.uniMatch.ui.screens.auth.RegisterScreen
import com.ulpgc.uniMatch.ui.screens.auth.AuthOptionsScreen


object AuthRoutes {
    const val OPTIONS = "options"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgotPassword"
}


@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    // Crear NavController para manejar la navegación interna
    val navController = rememberNavController()

    // Configurar NavHost para gestionar las rutas internas
    NavHost(navController = navController, startDestination = AuthRoutes.OPTIONS) {

        composable(AuthRoutes.OPTIONS) {
            AuthOptionsScreen(
                onLoginClick = { navController.navigate(AuthRoutes.LOGIN) },
                onRegisterClick = { navController.navigate(AuthRoutes.REGISTER) }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onLoginClick = authViewModel::login,
                onSignUpClick = { navController.navigate(AuthRoutes.REGISTER) },
                onForgotPasswordClick = {
                navController.navigate(AuthRoutes.FORGOT_PASSWORD)
            })
        }

        composable(AuthRoutes.REGISTER) {
            RegisterScreen(authViewModel = authViewModel, onBack = { navController.popBackStack() })
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(onSubmit = authViewModel::forgotPassword)
        }
    }
}
