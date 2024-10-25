package com.ulpgc.uniMatch.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.ui.components.ButtonComponent

@Composable
fun AuthOptionsScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween, // Distribuir los elementos con espacio entre ellos
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen y nombre de la aplicación en la parte superior
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.unimatch_logo), // Logo de UniMatch
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge
            )

            // Slogan o mensaje inspirador
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.find_your_ideal_partner_near_you), // Añade el string en strings.xml para el slogan
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centrar el texto
            )
        }

        // Botones de Login y Register en la parte inferior
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonComponent(
                onClick = onLoginClick,
                text = stringResource(R.string.login),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ButtonComponent(
                onClick = onRegisterClick,
                text = stringResource(R.string.register),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
