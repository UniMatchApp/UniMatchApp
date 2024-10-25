package com.ulpgc.uniMatch.ui.screens.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.screens.CoreRoutes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )

    Column {
        // Divider on top of the navigation bar
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = Dp.Hairline,
            color = MaterialTheme.colorScheme.surface
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background, // Color de fondo
            contentColor = MaterialTheme.colorScheme.primary // Color de los iconos seleccionados
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.labelResId),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(text = stringResource(id = item.labelResId)) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,  // Color del icono seleccionado
                        unselectedIconColor = MaterialTheme.colorScheme.secondary, // Color del icono no seleccionado
                        selectedTextColor = MaterialTheme.colorScheme.primary, // Color del texto seleccionado
                        indicatorColor = MaterialTheme.colorScheme.tertiary // Color del indicador debajo del icono seleccionado
                    )
                )
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: Int, val labelResId: Int) {
    data object Home : BottomNavItem(CoreRoutes.HOME, R.drawable.icon_home_clear, R.string.home)
    data object Search :
        BottomNavItem(CoreRoutes.SEARCH, R.drawable.icon_search_clear, R.string.search)

    data object Chat :
        BottomNavItem(CoreRoutes.CHAT_LIST, R.drawable.icon_chat_clear, R.string.chat)

    data object Profile :
        BottomNavItem(CoreRoutes.PROFILE, R.drawable.icon_user_clear, R.string.profile)
}