package com.ulpgc.uniMatch.ui.screens

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.screens.core.BottomNavigationBar
import com.ulpgc.uniMatch.ui.screens.core.HomeScreen
import com.ulpgc.uniMatch.ui.screens.core.ProfileScreen
import com.ulpgc.uniMatch.ui.screens.core.SearchScreen
import com.ulpgc.uniMatch.ui.screens.core.TopBar
import com.ulpgc.uniMatch.ui.screens.core.chat.ChatDetailScreen
import com.ulpgc.uniMatch.ui.screens.core.chat.ChatListScreen

object CoreRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val CHAT_LIST = "chatList"
    const val CHAT_DETAIL = "chatDetail/{chatId}"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val FILTER = "filter"
}

@Composable
fun CoreScreen(
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    if (authState !is AuthState.Authenticated) {
        Log.e("CoreScreen", "User is not authenticated")
        return
    }

    // Definir NavController para manejar la navegación en el Core
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopBar(currentRoute = currentRoute, navController = navController)
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            CoreNavHost(navController, authViewModel, chatViewModel)
        }
    }
}


@Composable
fun CoreNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel
) {
    NavHost(
        navController = navController,
        startDestination = CoreRoutes.HOME,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
    ) {
        composable(CoreRoutes.HOME) { HomeScreen() }
        composable(CoreRoutes.SEARCH) { SearchScreen() }
        composable(CoreRoutes.PROFILE) { ProfileScreen() }
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
                    navController = navController
                )
            }
        }
    }
}
