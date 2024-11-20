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
import com.ulpgc.uniMatch.data.infrastructure.viewModels.AuthState
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.HomeViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.NotificationsViewModel
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.screens.core.BottomNavigationBar
import com.ulpgc.uniMatch.ui.screens.core.SearchScreen
import com.ulpgc.uniMatch.ui.screens.core.TopBar
import com.ulpgc.uniMatch.ui.screens.core.account.AccountSettingsScreen
import com.ulpgc.uniMatch.ui.screens.core.chat.ChatDetailScreen
import com.ulpgc.uniMatch.ui.screens.core.chat.ChatListScreen
import com.ulpgc.uniMatch.ui.screens.core.home.HomeScreen
import com.ulpgc.uniMatch.ui.screens.core.notifications.NotificationsScreen
import com.ulpgc.uniMatch.ui.screens.core.policies.CookiesPolicyScreen
import com.ulpgc.uniMatch.ui.screens.core.policies.PrivacyPolicyScreen
import com.ulpgc.uniMatch.ui.screens.core.preferences.PreferencesScreen
import com.ulpgc.uniMatch.ui.screens.core.profile.ProfileInterests
import com.ulpgc.uniMatch.ui.screens.core.profile.ProfileScreen
import com.ulpgc.uniMatch.ui.screens.core.profile.ProfileWall

object CoreRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val CHAT_LIST = "chatList"
    const val CHAT_DETAIL = "chatDetail/{chatId}"
    const val PROFILE = "profile"
    const val PROFILE_WALL = "profile-wall/{userId}"
    const val PROFILE_INTERESTS = "profile-interests/{userId}"
    const val NOTIFICATIONS = "home/notifications"
    const val PREFERENCES = "home/preferences"
    const val PRIVACYPOLICIES = "privacy-policies"
    const val COOKIESPOLICIES = "cookies-policies"
    const val ACCOUNT = "account"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoreScreen(
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    notificationsViewModel: NotificationsViewModel
) {
    val authState by userViewModel.authState.collectAsState()

    if (authState !is AuthState.Authenticated) {
        Log.e("CoreScreen", "User is not authenticated")
        return
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isPaddingRequired = when (currentRoute) {
        CoreRoutes.HOME, CoreRoutes.SEARCH, CoreRoutes.CHAT_LIST, CoreRoutes.CHAT_DETAIL, CoreRoutes.PROFILE,
        CoreRoutes.PREFERENCES, CoreRoutes.NOTIFICATIONS, CoreRoutes.PRIVACYPOLICIES,
        CoreRoutes.COOKIESPOLICIES, CoreRoutes.PROFILE_INTERESTS, CoreRoutes.PROFILE_WALL,
        CoreRoutes.ACCOUNT -> true

        else -> false
    }

    Scaffold(
        topBar = {
            TopBar(
                currentRoute = currentRoute,
                navController = navController,
                chatViewModel = chatViewModel
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        val paddingModifier = if (isPaddingRequired) {
            Modifier.padding(innerPadding)
        } else {
            Modifier.padding(0.dp)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(paddingModifier)
        ) {
            CoreNavHost(
                navController,
                userViewModel,
                chatViewModel,
                profileViewModel,
                homeViewModel,
                notificationsViewModel
            )
        }
    }
}


@Composable
fun CoreNavHost(
    navController: NavHostController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    notificationsViewModel: NotificationsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = CoreRoutes.HOME,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
    ) {
        composable(CoreRoutes.HOME) {
            HomeScreen(
                homeViewModel = homeViewModel,
                userViewModel = userViewModel
            )
        }
        composable(CoreRoutes.SEARCH) { SearchScreen() }
        composable(CoreRoutes.PROFILE) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onEditClick = { userId ->
                    navController.navigate(CoreRoutes.PROFILE_WALL.replace("{userId}", userId))
                },
                onEditInterestsClick = { userId ->
                    navController.navigate(CoreRoutes.PROFILE_INTERESTS.replace("{userId}", userId))
                },
                onCookiesClick = {
                    navController.navigate(CoreRoutes.COOKIESPOLICIES)
                },
                onPrivacyClick = {
                    navController.navigate(CoreRoutes.PRIVACYPOLICIES)
                }
            )
        }
        composable(CoreRoutes.CHAT_LIST) {
            ChatListScreen(
                viewModel = chatViewModel,
                onChatClick = { chatId ->
                    navController.navigate(CoreRoutes.CHAT_DETAIL.replace("{chatId}", chatId))
                }
            )
        }

        composable(CoreRoutes.PREFERENCES) {
            PreferencesScreen(
                profileViewModel = profileViewModel
            )
        }

        composable(CoreRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                notificationsViewModel = notificationsViewModel,
                profileViewModel = profileViewModel
            )
        }

        composable("chatDetail/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatDetailScreen(
                chatId = chatId,
                chatViewModel = chatViewModel,
                userViewModel = userViewModel
            )
        }


        composable("profile-wall/{userId}") {
            ProfileWall(
                profileViewModel = profileViewModel,
            )
        }

        composable("profile-interests/{userId}") {
            ProfileInterests(
                profileViewModel = profileViewModel,
            )
        }

        composable("privacy-policies") {
            PrivacyPolicyScreen()
        }

        composable("cookies-policies") {
            CookiesPolicyScreen()
        }

        composable("account") {
            AccountSettingsScreen(userViewModel)
        }
    }
}
