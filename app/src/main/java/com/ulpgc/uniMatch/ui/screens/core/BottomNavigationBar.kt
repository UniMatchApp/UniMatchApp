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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.screens.CoreRoutes
import com.ulpgc.uniMatch.ui.theme.AppSize
import com.ulpgc.uniMatch.ui.theme.MainColor
import com.ulpgc.uniMatch.ui.theme.White

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

        // Obtener la entrada de la pila de navegación actual
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Verifica si la ruta actual está en los elementos permitidos
        if (items.any { it.route == currentRoute }) {
            NavigationBar(
                containerColor = MainColor,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (isSelected) item.iconFilled else item.icon
                                ),
                                contentDescription = stringResource(id = item.labelResId),
                                modifier = Modifier.size(AppSize.iconSize),
                                tint = if (isSelected) White else MaterialTheme.colorScheme.secondary
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = item.labelResId),
                                color = if (isSelected) White else MaterialTheme.colorScheme.secondary
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = White,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = White,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val iconFilled: Int, // Icono con relleno para el estado seleccionado
    val labelResId: Int
) {
    data object Home : BottomNavItem(
        CoreRoutes.HOME,
        R.drawable.icon_home_clear,
        R.drawable.icon_home_filled, // Icono con relleno
        R.string.home
    )
    data object Search : BottomNavItem(
        CoreRoutes.SEARCH,
        R.drawable.icon_search_clear,
        R.drawable.icon_search_filled, // Icono con relleno
        R.string.search
    )
    data object Chat : BottomNavItem(
        CoreRoutes.CHAT_LIST,
        R.drawable.icon_chat_clear,
        R.drawable.icon_chat_filled, // Icono con relleno
        R.string.chat
    )
    data object Profile : BottomNavItem(
        CoreRoutes.PROFILE,
        R.drawable.icon_user_clear,
        R.drawable.icon_user_filled, // Icono con relleno
        R.string.profile
    )
}