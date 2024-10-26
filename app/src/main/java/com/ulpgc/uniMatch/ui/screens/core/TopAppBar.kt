package com.ulpgc.uniMatch.ui.screens.core


import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
        CoreRoutes.PROFILE -> ProfileTopBar()
//        else -> {
//            Text(
//                text = stringResource(id = R.string.app_name),
//                style = MaterialTheme.typography.titleLarge
//            )
//        }
    }
}
