package com.ulpgc.uniMatch.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.screens.core.BottomNavigationBar
import com.ulpgc.uniMatch.ui.screens.core.profile.ProfileScreen
import com.ulpgc.uniMatch.ui.screens.core.SearchScreen
import com.ulpgc.uniMatch.ui.screens.core.TopBar
import com.ulpgc.uniMatch.ui.screens.core.chat.ChatDetailScreen
import com.ulpgc.uniMatch.ui.screens.core.chat.ChatListScreen
import com.ulpgc.uniMatch.ui.screens.core.home.HomeScreen
import com.ulpgc.uniMatch.ui.screens.core.profile.ProfileWall

object CoreRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val CHAT_LIST = "chatList"
    const val CHAT_DETAIL = "chatDetail/{chatId}"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val FILTER = "filter"
    const val PROFILE_WALL = "profile-wall/{userId}"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoreScreen(
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    if (authState !is AuthState.Authenticated) {
        Log.e("CoreScreen", "User is not authenticated")
        return
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determina si necesitas padding o no
    val isPaddingRequired = when (currentRoute) {
        CoreRoutes.HOME, CoreRoutes.SEARCH, CoreRoutes.CHAT_LIST, CoreRoutes.PROFILE -> true
        else -> false
    }

    Scaffold(
        topBar = {
            TopBar(currentRoute = currentRoute, navController = navController)
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        // Ajusta el padding del Box basado en la ruta actual
        val paddingModifier = if (isPaddingRequired) {
            Modifier.padding(innerPadding)
        } else {
            Modifier.padding(0.dp)
        }

        Box(modifier = Modifier.fillMaxSize().then(paddingModifier)) {
            CoreNavHost(navController, authViewModel, chatViewModel, profileViewModel, homeViewModel)
        }
    }
}



@Composable
fun CoreNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = CoreRoutes.HOME,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
    ) {
        composable(CoreRoutes.HOME) { HomeScreen(
            homeViewModel = homeViewModel,
            profileViewModel = profileViewModel,
            authViewModel = authViewModel
        ) }
        composable(CoreRoutes.SEARCH) { SearchScreen() }
        composable(CoreRoutes.PROFILE) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onEditClick = {
                    // Navegar a la pantalla de edición de perfil
                    navController.navigate("profile-wall/{userId}")
                }
        ) }
        composable(CoreRoutes.CHAT_LIST) {
            ChatListScreen(
                viewModel = chatViewModel,
                onChatClick = { chatId ->
                    // Navegar a la pantalla de detalles del chat usando un subenrutamiento
                    navController.navigate(CoreRoutes.CHAT_DETAIL.replace("{chatId}", chatId))
                }
            )
        }

        // Anidamos un NavHost secundario para la pantalla de detalles del chat
        navigation(startDestination = "chatDetail/{chatId}", route = "chat_navigation") {
            composable("chatDetail/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatDetailScreen(
                    chatId = chatId,
                    chatViewModel = chatViewModel,
                    authViewModel = authViewModel,
                    navController = navController,
                    profileViewModel = profileViewModel

                )
            }
        }
        navigation(startDestination = "profile-wall/{userId}", route = "wall") {
            composable("profile-wall/{userId}") {
                ProfileWall(
                    profileViewModel = profileViewModel,
                    navController = navController,
                    onAddImageClick = {
                        navController.navigate("chat-detail/{chatId}")
                    }
                )
            }
        }
    }
}
