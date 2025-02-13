package com.ulpgc.uniMatch.ui.screens.core


import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.screens.CoreRoutes
import com.ulpgc.uniMatch.ui.screens.core.topBars.AccountTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ChatDetailTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ChatSectionTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.HomeTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.NotificationTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.PoliciesTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.PreferencesTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.SearchTopBar

@Composable
fun TopBar(
    currentRoute: String?,
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    Log.i("TopBar", "Current route: $currentRoute")


    when (currentRoute) {
        CoreRoutes.HOME -> HomeTopBar(navController)
        CoreRoutes.SEARCH -> SearchTopBar()
        CoreRoutes.CHAT_LIST -> ChatSectionTopBar(
            chatViewModel = chatViewModel
        )

        CoreRoutes.PROFILE -> ProfileTopBar(
            navController
        )

        CoreRoutes.PREFERENCES -> PreferencesTopBar(
            navController
        )

        CoreRoutes.NOTIFICATIONS -> NotificationTopBar(
            navController
        )

        CoreRoutes.CHAT_DETAIL -> ChatDetailTopBar(
            navController,
            chatViewModel
        )

        CoreRoutes.COOKIESPOLICIES -> PoliciesTopBar(
            navController
        )

        CoreRoutes.PRIVACYPOLICIES -> PoliciesTopBar(
            navController
        )

        CoreRoutes.PROFILE_INTERESTS -> ProfileSettingsTopBar(
            navController
        )

        CoreRoutes.PROFILE_WALL -> ProfileSettingsTopBar(
            navController
        )

        CoreRoutes.ACCOUNT -> AccountTopBar(
            navController
        )

        else -> {
            Modifier.padding(0.dp)
        }
    }
}
