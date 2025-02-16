package com.ulpgc.uniMatch.ui.screens.core


import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ChatViewModel
import com.ulpgc.uniMatch.ui.screens.CoreRoutes
import com.ulpgc.uniMatch.ui.screens.core.topBars.ChatDetailTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ChatSectionTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.EventsTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.HomeTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.TopNavBar


@Composable
fun TopBar(
    currentRoute: String?,
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    Log.i("TopBar", "Current route: $currentRoute")


    when (currentRoute) {
        CoreRoutes.HOME -> HomeTopBar(navController)
        CoreRoutes.EVENTS -> EventsTopBar()

        CoreRoutes.EVENT -> TopNavBar(
            navController,
            stringResource(R.string.event_details)
        )
        CoreRoutes.ADD_EVENT -> TopNavBar(
            navController,
            stringResource(R.string.add_event)
        )

        CoreRoutes.EVENT_SURVEY -> TopNavBar(
            navController,
            stringResource(R.string.event_surveys)
        )

        CoreRoutes.CHAT_LIST -> ChatSectionTopBar(
            chatViewModel = chatViewModel
        )

        CoreRoutes.PROFILE -> ProfileTopBar(
            navController
        )

        CoreRoutes.PREFERENCES -> TopNavBar(
            navController,
            stringResource(R.string.edit_preferences)
        )

        CoreRoutes.NOTIFICATIONS -> TopNavBar(
            navController,
            stringResource(R.string.notifications)
        )

        CoreRoutes.CHAT_DETAIL -> ChatDetailTopBar(
            navController,
            chatViewModel
        )

        CoreRoutes.COOKIESPOLICIES -> TopNavBar(
            navController,
            stringResource(R.string.cookies_policy)
        )

        CoreRoutes.PRIVACYPOLICIES -> TopNavBar(
            navController,
            stringResource(R.string.privacy_policy)
        )

        CoreRoutes.PROFILE_INTERESTS -> TopNavBar(
            navController,
            stringResource(R.string.edit_profile)
        )

        CoreRoutes.PROFILE_WALL -> TopNavBar(
            navController,
            stringResource(R.string.edit_profile)
        )

        CoreRoutes.ACCOUNT -> TopNavBar(
            navController,
            stringResource(R.string.account)
        )

        else -> {
            Modifier.padding(0.dp)
        }
    }
}
