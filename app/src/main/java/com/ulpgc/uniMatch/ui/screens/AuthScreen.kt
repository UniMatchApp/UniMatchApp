package com.ulpgc.uniMatch.ui.screens

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.PermissionsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.ui.screens.auth.AuthOptionsScreen
import com.ulpgc.uniMatch.ui.screens.auth.VerifyCodeScreen
import com.ulpgc.uniMatch.ui.screens.auth.forgot.ForgotPasswordScreen
import com.ulpgc.uniMatch.ui.screens.auth.forgot.ResetPasswordScreen
import com.ulpgc.uniMatch.ui.screens.auth.login.LoginScreen
import com.ulpgc.uniMatch.ui.screens.auth.register.RegisterProfileScreen
import com.ulpgc.uniMatch.ui.screens.auth.register.RegisterScreen
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper

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
    errorViewModel: ErrorViewModel,
    permissionsViewModel: PermissionsViewModel
) {
    val navController = rememberNavController()

    val forgotPasswordUserId = userViewModel.forgotPasswordUserId.collectAsState()
    val registeredUserId = userViewModel.registeredUserId.collectAsState()
    val loginUserId = userViewModel.loginUserId.collectAsState()

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val activity = LocalContext.current as Activity

    val hasPermission = permissionsViewModel.hasLocationPermission.collectAsState()

    val temporaryEmail = userViewModel.temporaryEmail.collectAsState()
    val email = userViewModel.email.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionsViewModel.updateLocationPermissionStatus(isGranted)
    }

    suspend fun askForPermission() {
        if (!hasPermission.value) {
            val canRequestAgain = shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            if (canRequestAgain) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            LocationHelper.getCurrentLocation(activity)?.let { userLocation ->
                val latitude = userLocation.first
                val longitude = userLocation.second
                location = Pair(latitude, longitude)
            }
        }
    }

    LaunchedEffect(Unit) {
        askForPermission()
    }

    LaunchedEffect (hasPermission.value) {
        if (hasPermission.value) {
            LocationHelper.getCurrentLocation(activity)?.let { userLocation ->
                val latitude = userLocation.first
                val longitude = userLocation.second
                location = Pair(latitude, longitude)
            }
        }
    }

    NavHost(navController = navController, startDestination = AuthRoutes.OPTIONS) {
        composable(AuthRoutes.OPTIONS) {

            AuthOptionsScreen(
                onLoginClick = {
                    navController.navigate(AuthRoutes.LOGIN)
                },
                onRegisterClick = {
                    navController.navigate(AuthRoutes.REGISTER)
                }
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
                loginUserId.value?.let {
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
            )

            LaunchedEffect(registeredUserId.value) {
                registeredUserId.value?.let {
                    navController.navigate(AuthRoutes.REGISTER_VERIFY_CODE)
                }
            }
        }
        composable(AuthRoutes.REGISTER_PROFILE) {
            registeredUserId.value?.let { userId ->
                RegisterProfileScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = userId,
                    onCompleteProfile = {
                        navController.navigate(AuthRoutes.OPTIONS)
                        userViewModel.resetRegisteredUserId()
                    },
                    onBack = {
                        navController.navigate(AuthRoutes.REGISTER)
                        userViewModel.resetRegisteredUserId()
                    },
                    location = location
                )
                }
        }

        composable(AuthRoutes.LOGIN_PROFILE) {
            loginUserId.value?.let { userId ->
                RegisterProfileScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = userId,
                    onCompleteProfile = {
                        navController.navigate(AuthRoutes.OPTIONS)
                        userViewModel.resetLoginUserId()
                    },
                    onBack = {
                        navController.navigate(AuthRoutes.LOGIN)
                        userViewModel.resetLoginUserId()
                    },
                    location = location
                )
            }
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                userViewModel = userViewModel,
                errorViewModel = errorViewModel,
                onSubmit = {
                    navController.navigate(AuthRoutes.FORGOT_VERIFY_CODE)
                    userViewModel.resetForgotPasswordResult()
                           },
                onBack = {
                    navController.navigate(AuthRoutes.LOGIN)
                }
            )

            LaunchedEffect(forgotPasswordUserId.value) {
                forgotPasswordUserId.value?.let {
                    navController.navigate(AuthRoutes.FORGOT_VERIFY_CODE)
                }
            }
        }

        composable(AuthRoutes.FORGOT_VERIFY_CODE) {
            temporaryEmail.value?.let {
                VerifyCodeScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    email = it,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.RESET_PASSWORD) },
                    onBack = {
                        navController.navigate(AuthRoutes.FORGOT_PASSWORD)
                        userViewModel.resetTemporaryEmail()
                        userViewModel.resetForgotPasswordUserId()
                    }
                )
            }
        }

        composable(AuthRoutes.REGISTER_VERIFY_CODE) {
            email.value?.let {
                VerifyCodeScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    email = it,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.REGISTER_PROFILE) },
                    onBack = {
                        navController.navigate(AuthRoutes.REGISTER)
                        userViewModel.resetTemporaryEmail()
                        userViewModel.resetRegisteredUserId()
                    }
                )
            }
        }

        composable(AuthRoutes.LOGIN_VERIFY_CODE) {
            email.value?.let {
                VerifyCodeScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    email = it,
                    onVerificationSuccess = { navController.navigate(AuthRoutes.LOGIN_PROFILE) },
                    onBack = {
                        navController.navigate(AuthRoutes.LOGIN)
                        userViewModel.resetEmail()
                        userViewModel.resetLoginUserId()
                    }
                )
            }
        }

        composable(AuthRoutes.RESET_PASSWORD) {
            forgotPasswordUserId.value?.let {
                ResetPasswordScreen(
                    userViewModel = userViewModel,
                    errorViewModel = errorViewModel,
                    userId = it,
                    onPasswordReset = {
                        navController.navigate(AuthRoutes.LOGIN)
                        userViewModel.resetTemporaryEmail()
                        userViewModel.resetForgotPasswordUserId()
                    },
                    onBack = {
                        navController.navigate(AuthRoutes.LOGIN)
                        userViewModel.resetTemporaryEmail()
                        userViewModel.resetForgotPasswordUserId()
                    }
                )
            }
        }
    }
}
