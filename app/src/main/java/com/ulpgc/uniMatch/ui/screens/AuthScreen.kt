package com.ulpgc.uniMatch.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.screens.auth.LoginScreen
import com.ulpgc.uniMatch.ui.screens.auth.RegisterScreen
import com.ulpgc.uniMatch.ui.screens.auth.AuthOptionsScreen
import com.ulpgc.uniMatch.ui.screens.auth.VerifyCodeScreen


object AuthRoutes {
    const val OPTIONS = "options"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val VERIFY_CODE = "forgotPassword/verifyCode"
}


@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    val forgotPasswordResult = authViewModel.forgotPasswordResult.collectAsState()

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
                onForgotPasswordClick = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) },
                onBackClick = { navController.navigate(AuthRoutes.OPTIONS) }
            )
        }

        composable(AuthRoutes.REGISTER) {
            RegisterScreen(authViewModel = authViewModel, onBack = { navController.navigate(AuthRoutes.OPTIONS) })
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onSubmit = { email ->
                    authViewModel.forgotPassword(email)
                    if (forgotPasswordResult.value) {
                        navController.navigate(AuthRoutes.VERIFY_CODE)
                    } else {
                        // Show error message
                    }
                },
                onBack = { navController.navigate(AuthRoutes.OPTIONS) }
            )
        }

        composable(AuthRoutes.VERIFY_CODE) {
            VerifyCodeScreen()
        }
    }
}

