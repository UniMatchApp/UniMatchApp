package com.ulpgc.uniMatch.ui.screens.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import com.ulpgc.uniMatch.ui.components.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ulpgc.uniMatch.R
import com.ulpgc.uniMatch.data.infrastructure.viewModels.ProfileViewModel
import com.ulpgc.uniMatch.ui.components.DropdownMenuShorter
import com.ulpgc.uniMatch.ui.screens.core.topBars.ProfileSettingsTopBar

@Composable
fun ProfileSettings(
    profileViewModel: ProfileViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val profile = profileViewModel.profileData.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileSettingsTopBar(onBackPressed = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ProfileDropdownField(label = "Intereses", options = listOf("Fútbol", "Baloncesto", "Minecraft"))
            ProfileDropdownField(label = "Sexo", options = context.resources.getStringArray(R.array.genders).toList())
            ProfileDropdownField(label = "Altura", options = listOf("170cm", "175cm", "180cm", "185cm", "190cm"))
            ProfileDropdownField(label = "Peso", options = listOf("70kg", "75kg", "80kg", "85kg", "90kg"))
            ProfileDropdownField(label = "Orientación sexual", options = context.resources.getStringArray(R.array.sexual_orientation).toList())
            ProfileDropdownField(label = "Puesto", options = listOf("Ingeniero", "Médico", "Profesor", "Diseñador"))
            ProfileDropdownField(label = "¿Qué tipo de relación buscas?", options = context.resources.getStringArray(R.array.relationship_type).toList())

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Más sobre mí
            ProfileSection(
                title = "Más sobre mí",
                rowTitles = listOf(
                    "Horóscopo" to profile?.horoscope.toString(),
                    "Educación" to profile?.education,
                    "Tipo de personalidad" to profile?.personalityType
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Estilo de vida
            ProfileSection(
                title = "Estilo de vida",
                rowTitles = listOf(
                    "Mascotas" to profile?.pets,
                    "¿Bebes?" to profile?.drinks,
                    "¿Fumas?" to profile?.smokes,
                    "¿Haces deporte?" to profile?.doesSports,
                    "Valores y creencias" to profile?.valuesAndBeliefs
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LegalSection()
        }
    }
}


@Composable
fun ProfileDropdownField(label: String, options: List<String>) {
    var selectedOption by remember { mutableStateOf(options.firstOrNull() ?: "Seleccionar") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        DropdownMenu(options, selectedOption)
    }
}

@Composable
fun ProfileSection(title: String, rowTitles: List<Pair<String, String?>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        rowTitles.forEach { (rowTitle, defaultValue) ->
            ProfileOptionRow(rowTitle, defaultValue ?: "Seleccionar")
            Spacer(modifier = Modifier.height(8.dp)) // Separador entre filas
        }
    }
}

@Composable
fun ProfileOptionRow(title: String, option: String) {
    val traducciones = mapOf(
        "Horóscopo" to "horoscopes",
        "Educación" to "school",
        "Tipo de personalidad" to "personality_type",
        "Mascotas" to "pets",
        "¿Bebes?" to "drinks",
        "¿Fumas?" to "smokes",
        "¿Haces deporte?" to "sports",
        "Valores y creencias" to "religion"
    )

    val context = LocalContext.current
    val iconName = "icon_${traducciones[title] ?: "default"}"
    val iconId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    val listName = traducciones[title] ?: "default"
    val listId = context.resources.getIdentifier(listName, "array", context.packageName) // Obtenemos el ID del array de strings

    val options = if (listId != 0) {
        context.resources.getStringArray(listId).toList()
    } else {
        listOf("Sin opciones disponibles")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            if (iconId != 0) { // Verificamos que iconId sea válido
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "Icono de usuario",
                    tint = Color.DarkGray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person, // Icono por defecto
                    contentDescription = "Icono por defecto",
                    tint = Color.DarkGray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(text = title, color = Color.DarkGray, fontSize = 12.sp)
        }

        DropdownMenuShorter(items = options, selectedItem = option)
    }
}


@Composable
fun LegalSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
    ) {
        LegalOptionButton("Política de cookies")
        LegalOptionButton("Política de privacidad")
        Button(
            onClick = { /* Acción de eliminar cuenta */ },
            colors = ButtonDefaults.buttonColors(Color(0xFFD7A2C3)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Eliminar cuenta", color = Color.White)
        }
    }
}

@Composable
fun LegalOptionButton(text: String) {
    Button(
        onClick = { /* Acción de legalidad */ },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}

