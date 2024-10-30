package com.ulpgc.uniMatch.ui.screens.core


import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.screens.CoreRoutes
import com.ulpgc.uniMatch.ui.screens.core.topBars.ChatDetailTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.HomeTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileTopBar
import com.ulpgc.uniMatch.ui.screens.core.topBars.SearchTopBar

@Composable
fun TopBar(currentRoute: String?, navController: NavHostController) {
    Log.i("TopBar", "Current route: $currentRoute")

    when (currentRoute) {
        CoreRoutes.HOME -> HomeTopBar(navController)
        CoreRoutes.SEARCH -> SearchTopBar()
        CoreRoutes.CHAT_LIST -> ChatDetailTopBar()
        CoreRoutes.PROFILE -> ProfileTopBar(
            navController,
            onClickSettings = {
                navController.navigate(CoreRoutes.PROFILE_SETTINGS)
            }
        )
        else -> {
            Modifier.padding(0.dp)
        }
    }
}
