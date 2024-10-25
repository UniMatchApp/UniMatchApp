package com.ulpgc.uniMatch.ui.screens.core

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.screens.CoreRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentRoute: String?) {
    Log.i("TopBar", "Current route: $currentRoute")
    val title = when (currentRoute) {
        CoreRoutes.HOME -> stringResource(id = R.string.home)
        CoreRoutes.SEARCH -> stringResource(id = R.string.search)
        CoreRoutes.CHAT_DETAIL -> stringResource(id = R.string.chat)
        CoreRoutes.PROFILE -> stringResource(id = R.string.profile)
        else -> stringResource(id = R.string.app_name) // Default title
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() // Asegura que el contenido ocupe todo el ancho
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unimatch_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(0.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 8.dp) // Ajusta el padding si es necesario
                )
            }
        },
        modifier = Modifier.fillMaxWidth() // Hace que la TopAppBar ocupe todo el ancho
    )
}
