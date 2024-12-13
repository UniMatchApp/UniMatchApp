package com.ulpgc.uniMatch

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ErrorState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.PermissionsViewModel
import com.ulpgc.uniMatch.ui.components.ErrorDialog
import com.ulpgc.uniMatch.ui.screens.AuthScreen
import com.ulpgc.uniMatch.ui.screens.CoreScreen
import com.ulpgc.uniMatch.ui.screens.utils.LocationHelper
import com.ulpgc.uniMatch.ui.theme.UniMatchTheme
import dev.shreyaspatil.permissionFlow.PermissionFlow


class MainActivity : ComponentActivity() {

    val permissionsViewModel = PermissionsViewModel()

    val permissionFlow = PermissionFlow.getInstance()

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            permissionFlow.notifyPermissionsChanged(Manifest.permission.ACCESS_FINE_LOCATION)
            if (isGranted) {
                permissionsViewModel.updateLocationPermissionStatus(true)
            } else {
                permissionsViewModel.updateLocationPermissionStatus(false)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as UniMatchApplication

        app.userViewModel.checkUserSession()

        // Utiliza la API de SplashScreen
        installSplashScreen().setKeepOnScreenCondition {
            app.userViewModel.isCheckingSession.value
        }

        // Se lanza el observable del permiso de ubicación
        lifecycleScope.launchWhenStarted {
            permissionFlow.getPermissionState(Manifest.permission.ACCESS_FINE_LOCATION).collect { state ->
                if (state.isGranted) {
                    permissionsViewModel.updateLocationPermissionStatus(true)
                } else {
                    permissionsViewModel.updateLocationPermissionStatus(false)
                }
            }
        }


        enableEdgeToEdge()
        setContent {
            UniMatchTheme {
                val authState by app.userViewModel.authState.collectAsState()
                val errorState by app.errorViewModel.errorState.collectAsState()

                when (authState) {

                    is AuthState.Authenticated -> {

                        val userId = (authState as AuthState.Authenticated).user.id
                        app.initializeWebSocket(userId, app.eventbus)

                        if (!LocationHelper.checkLocationPermission(this)) {
                            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        else {
                            permissionsViewModel.updateLocationPermissionStatus(true)
                        }

                        CoreScreen(
                            app.userViewModel,
                            app.chatViewModel,
                            app.profileViewModel,
                            app.homeViewModel,
                            app.notificationsViewModel,
                            app.errorViewModel,
                            permissionsViewModel
                        )
                    }

                    is AuthState.Unauthenticated -> AuthScreen(
                        app.userViewModel,
                        app.errorViewModel,
                        permissionsViewModel
                    )
                }

                if (errorState is ErrorState.Error) {
                    ErrorDialog(
                        errorMessage = (errorState as ErrorState.Error).message,

                        onDismiss = {
                            app.errorViewModel.clearError()
                        }
                    )
                }
            }
        }
    }
}
