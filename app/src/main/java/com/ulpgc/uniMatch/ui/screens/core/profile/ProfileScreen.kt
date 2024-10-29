package com.ulpgc.uniMatch.ui.screens.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenu

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
) {

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val profile = viewModel.profileData.collectAsState().value
    val context = LocalContext.current
    val facts = context.resources.getStringArray(R.array.funny_questions).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {}

        // Nombre y Edad
        Text(
            text = "${profile?.name ?: "Nombre no disponible"}, ${profile?.age ?: "--"}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Sección "Sobre mí"
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sobre mí",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = profile?.aboutMe ?: "",
            onValueChange = { /* Aquí puedes llamar a viewModel.updateAboutMe(it) si tienes este método en el ViewModel */ },
            modifier = Modifier.fillMaxWidth(),
        )

        // Sección "Preguntas" (Dropdown)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Preguntas",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )

        profile?.fact?.let {
            DropdownMenu(
                items = facts,
                selectedItem = it,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Intereses",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )


        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = profile?.interests.toString() ?: "Selecciona tus intereses",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Aquí puedes llamar a viewModel.saveChanges() si tienes este método en el ViewModel */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }
    }
}
