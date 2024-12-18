package com.ulpgc.uniMatch.ui.screens.core.topBars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.screens.CoreRoutes
import com.ulpgc.uniMatch.ui.theme.AppPadding
import com.ulpgc.uniMatch.ui.theme.AppSize
import com.ulpgc.uniMatch.ui.theme.MainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavHostController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unimatch_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(48.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = stringResource(id = R.string.home),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(CoreRoutes.NOTIFICATIONS) }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_bell),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(AppSize.iconSize),
                    tint = MaterialTheme.colorScheme.secondary
                )

            }
            IconButton(onClick = { navController.navigate(CoreRoutes.PREFERENCES) }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_filter),
                    contentDescription = "Filter",
                    modifier = Modifier.size(AppSize.iconSize),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}