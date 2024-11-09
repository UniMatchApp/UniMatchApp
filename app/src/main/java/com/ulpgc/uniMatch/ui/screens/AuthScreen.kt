package com.ulpgc.uniMatch.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.screens.auth.AuthOptionsScreen
import com.ulpgc.uniMatch.ui.screens.auth.ForgotPasswordScreen
import com.ulpgc.uniMatch.ui.screens.auth.LoginScreen
import com.ulpgc.uniMatch.ui.screens.auth.RegisterScreen
import com.ulpgc.uniMatch.ui.screens.auth.ResetPasswordScreen
import com.ulpgc.uniMatch.ui.screens.auth.VerifyCodeScreen

object AuthRoutes {
    const val OPTIONS = "options"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val VERIFY_CODE = "forgotPassword/verifyCode/{email}"
    const val RESET_PASSWORD = "resetPassword/{email}"
}

@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AuthRoutes.OPTIONS) {
        composable(AuthRoutes.OPTIONS) {
            AuthOptionsScreen(
                onLoginClick = { navController.navigate(AuthRoutes.LOGIN) },
                onRegisterClick = { navController.navigate(AuthRoutes.REGISTER) }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onBackClick = { navController.navigate(AuthRoutes.OPTIONS) },
                onForgotPasswordClick = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) },
                onSignUpClick = { navController.navigate(AuthRoutes.REGISTER) }
            )
        }

        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onBack = { navController.navigate(AuthRoutes.OPTIONS) }
            )
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable(AuthRoutes.VERIFY_CODE) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (email != null) {
                VerifyCodeScreen(
                    authViewModel = authViewModel,
                    navController = navController,
                    email = email
                )
            }
        }

        composable(AuthRoutes.RESET_PASSWORD) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (email != null) {
                ResetPasswordScreen(
                    authViewModel = authViewModel,
                    navController = navController,
                    email = email
                )
            }
        }

    }
}
