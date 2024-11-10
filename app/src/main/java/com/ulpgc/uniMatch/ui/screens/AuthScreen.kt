package com.ulpgc.uniMatch.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
    const val REGISTER_PROFILE = "register/profile"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val REGISTER_VERIFY_CODE = "register/verifyCode"
    const val FORGOT_VERIFY_CODE = "forgotPassword/verifyCode"
    const val RESET_PASSWORD = "resetPassword"
}

@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    val registeredUserId = authViewModel.registeredUserId.collectAsState()
    val forgotPasswordUserId = authViewModel.forgotPasswordUserId.collectAsState()



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
                continueRegister = {
                    authViewModel.register()
                }
            )

            LaunchedEffect(registeredUserId.value) {
                if (registeredUserId.value != null) {
                    navController.navigate(AuthRoutes.REGISTER_VERIFY_CODE)
                }
            }
        }

        composable(AuthRoutes.REGISTER_PROFILE) {
            if (registeredUserId.value != null) {
                RegisterProfileScreen(
                    authViewModel = authViewModel,
                    userId = registeredUserId.value!!,
                    onCompleteProfile = {
                        navController.navigate(AuthRoutes.OPTIONS)
                        authViewModel.resetRegisteredUserId()
                    }
                )
            }
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                onSubmit = { navController.navigate(AuthRoutes.FORGOT_VERIFY_CODE) },
                onBack = { navController.navigate(AuthRoutes.LOGIN) }
            )

            LaunchedEffect(forgotPasswordUserId.value) {
                if (forgotPasswordUserId.value != null) {
                    navController.navigate(AuthRoutes.FORGOT_VERIFY_CODE)
                }
            }
        }

        composable(AuthRoutes.FORGOT_VERIFY_CODE) {
            if (forgotPasswordUserId.value != null) {
                VerifyCodeScreen(
                    authViewModel = authViewModel,
                    userId = forgotPasswordUserId.value!!,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.RESET_PASSWORD) },
                    onBack = {
                        navController.navigate(AuthRoutes.FORGOT_PASSWORD)
                        authViewModel.resetForgotPasswordUserId()
                    }
                )
            }

        }

        composable(AuthRoutes.REGISTER_VERIFY_CODE) {
            if (registeredUserId.value != null) {
                VerifyCodeScreen(
                    authViewModel = authViewModel,
                    userId = registeredUserId.value!!,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.REGISTER_PROFILE) },
                    onBack = {
                        navController.navigate(AuthRoutes.REGISTER)
                        authViewModel.resetRegisteredUserId()
                    }
                )
            }

        }


        composable(AuthRoutes.RESET_PASSWORD) {
            if (forgotPasswordUserId.value != null) {
                ResetPasswordScreen(
                    authViewModel = authViewModel,
                    userId = forgotPasswordUserId.value!!,
                    onPasswordReset = {
                        navController.navigate(AuthRoutes.LOGIN)
                        authViewModel.resetForgotPasswordUserId()
                    }
                )
            }
        }

    }
}
