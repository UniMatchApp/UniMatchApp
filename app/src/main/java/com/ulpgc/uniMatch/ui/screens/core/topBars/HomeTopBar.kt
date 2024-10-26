package com.ulpgc.uniMatch.ui.screens.core.topBars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.screens.CoreRoutes
import com.ulpgc.uniMatch.ui.theme.AppPadding
import com.ulpgc.uniMatch.ui.theme.AppSize

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
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = stringResource(id = R.string.profile),
                    color = Color.Black,
                    modifier = Modifier.padding(start = AppPadding.Small)
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(CoreRoutes.NOTIFICATIONS) }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_bell),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(AppSize.iconSize)
                )

            }
            IconButton(onClick = { navController.navigate(CoreRoutes.FILTER) }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_filter),
                    contentDescription = "Filter",
                    modifier = Modifier.size(AppSize.iconSize)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}