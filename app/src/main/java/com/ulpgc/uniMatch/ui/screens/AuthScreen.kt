package com.ulpgc.uniMatch.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.ui.screens.auth.AuthOptionsScreen
import com.ulpgc.uniMatch.ui.screens.auth.forgot.ForgotPasswordScreen
import com.ulpgc.uniMatch.ui.screens.auth.login.LoginScreen
import com.ulpgc.uniMatch.ui.screens.auth.register.RegisterScreen
import com.ulpgc.uniMatch.ui.screens.auth.forgot.ResetPasswordScreen
import com.ulpgc.uniMatch.ui.screens.auth.VerifyCodeScreen
import com.ulpgc.uniMatch.ui.screens.auth.register.RegisterProfileScreen

object AuthRoutes {
    const val OPTIONS = "options"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val REGISTER_PROFILE = "register/profile/{userId}"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val REGISTER_VERIFY_CODE = "register/verifyCode/{userId}"
    const val FORGOT_VERIFY_CODE = "forgotPassword/verifyCode/{userId}"
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
                onBackClick = { navController.navigate(AuthRoutes.OPTIONS) },
                onLoginClick = { navController.navigate(AuthRoutes.LOGIN) },
                continueRegister = { userId -> navController.navigate("register/verifyCode/$userId")}
            )
        }

        composable(AuthRoutes.REGISTER_PROFILE) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                RegisterProfileScreen(
                    authViewModel = authViewModel,
                    userId = userId,
                    onCompleteProfile = { navController.navigate(AuthRoutes.OPTIONS) }
                )
            }
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable(AuthRoutes.FORGOT_VERIFY_CODE) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                VerifyCodeScreen(
                    authViewModel = authViewModel,
                    navController = navController,
                    userId = userId,
                    onVerifyCode = { navController.navigate("resetPassword/$userId") }
                )
            }
        }

        composable(AuthRoutes.REGISTER_VERIFY_CODE) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                VerifyCodeScreen(
                    authViewModel = authViewModel,
                    navController = navController,
                    userId = userId,
                    onVerifyCode = { navController.navigate("register/profile/$userId") }
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
