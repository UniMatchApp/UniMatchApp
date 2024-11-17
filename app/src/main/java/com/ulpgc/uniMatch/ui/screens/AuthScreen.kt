package com.ulpgc.uniMatch.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
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
    const val LOGIN_PROFILE = "login/completeProfile"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val REGISTER_VERIFY_CODE = "register/verifyCode"
    const val LOGIN_VERIFY_CODE = "login/verifyCode"
    const val FORGOT_VERIFY_CODE = "forgotPassword/verifyCode"
    const val RESET_PASSWORD = "resetPassword"
}

@Composable
fun AuthScreen(
    userViewModel: UserViewModel,
    errorViewModel: ErrorViewModel
) {
    val navController = rememberNavController()

    val registeredUserId = userViewModel.registeredUserId.collectAsState()
    val forgotPasswordUserId = userViewModel.forgotPasswordUserId.collectAsState()
    val loginUserId = userViewModel.loginUserId.collectAsState()



    NavHost(navController = navController, startDestination = AuthRoutes.OPTIONS) {
        composable(AuthRoutes.OPTIONS) {
            AuthOptionsScreen(
                onLoginClick = { navController.navigate(AuthRoutes.LOGIN) },
                onRegisterClick = { navController.navigate(AuthRoutes.REGISTER) }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                userViewModel = userViewModel,
                errorViewModel = errorViewModel,
                onBackClick = { navController.navigate(AuthRoutes.OPTIONS) },
                onForgotPasswordClick = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) },
                onSignUpClick = { navController.navigate(AuthRoutes.REGISTER) }
            )

            LaunchedEffect(loginUserId.value) {
                if (loginUserId.value != null) {
                    navController.navigate(AuthRoutes.LOGIN_VERIFY_CODE)
                }
            }
        }

        composable(AuthRoutes.REGISTER) {

            RegisterScreen(
                userViewModel = userViewModel,
                errorViewModel = errorViewModel,
                onBackClick = { navController.navigate(AuthRoutes.OPTIONS) },
                onLoginClick = { navController.navigate(AuthRoutes.LOGIN) },
                continueRegister = {
                    userViewModel.register()
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
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = registeredUserId.value!!,
                    onCompleteProfile = {
                        navController.navigate(AuthRoutes.OPTIONS)
                        userViewModel.resetRegisteredUserId()
                    }
                )
            }
        }

        composable(AuthRoutes.LOGIN_PROFILE) {
            if (loginUserId.value != null) {
                RegisterProfileScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = loginUserId.value!!,
                    onCompleteProfile = {
                        navController.navigate(AuthRoutes.LOGIN_VERIFY_CODE)
                        userViewModel.resetLoginUserId()
                    }
                )
            }
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                userViewModel = userViewModel,
                errorViewModel = errorViewModel,
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
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = forgotPasswordUserId.value!!,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.RESET_PASSWORD) },
                    onBack = {
                        navController.navigate(AuthRoutes.FORGOT_PASSWORD)
                        userViewModel.resetForgotPasswordUserId()
                    }
                )
            }

        }

        composable(AuthRoutes.REGISTER_VERIFY_CODE) {
            if (registeredUserId.value != null) {
                VerifyCodeScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = registeredUserId.value!!,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.REGISTER_PROFILE) },
                    onBack = {
                        navController.navigate(AuthRoutes.REGISTER)
                        userViewModel.resetRegisteredUserId()
                    }
                )
            }

        }

        composable(AuthRoutes.LOGIN_VERIFY_CODE) {
            if (loginUserId.value != null) {
                VerifyCodeScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = loginUserId.value!!,
                    onVerificationSuccess = {
                        navController.navigate(AuthRoutes.LOGIN_PROFILE)
                    },
                    onBack = {
                        navController.navigate(AuthRoutes.LOGIN)
                        userViewModel.resetLoginUserId()
                    }
                )
            }
        }


        composable(AuthRoutes.RESET_PASSWORD) {
            if (forgotPasswordUserId.value != null) {
                ResetPasswordScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = forgotPasswordUserId.value!!,
                    onPasswordReset = {
                        navController.navigate(AuthRoutes.LOGIN)
                        userViewModel.resetForgotPasswordUserId()
                    }
                )
            }
        }

    }
}
