package com.ulpgc.uniMatch.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Define las formas (shapes) con esquinas redondeadas
val shapes = Shapes(
    small = RoundedCornerShape(Dimens.smallRadius),   // Esquinas redondeadas pequeñas
    medium = RoundedCornerShape(Dimens.mediumRadius), // Esquinas redondeadas medianas
    large = RoundedCornerShape(Dimens.largeRadius)    // Sin esquinas redondeadas (rectas)
)


// Define los esquemas de color para los temas claros y oscuros con tonalidades rosadas
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFDB5FFF),   // Rosado fuerte
    onPrimary = Color.Black,
    secondary= Color(0xFFCECECE),  // Gris oscuro
    onSecondary = Color.Black,
    tertiary = Color(0xFFF1C9FF), // Rosado pastel
    onTertiary = Color.White,
    background = Color(0xFF282828),
    onBackground = Color.White,
    surface = Color(0xFF343434),
    onSurface = LightGrey
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFDB5FFF),   // Rosado fuerte
    onPrimary = Color.White,
    secondary = Color(0xFF545454),  // Gris oscuro
    onSecondary = Color.White,
    tertiary = Color(0xFFE58FFF), // Rosado pastel
    onTertiary = Color.Black,
    background = Color(0xFFFFFBFA),
    onBackground = Color.Black,
    surface = Color(0xFFFFFBFA),
    onSurface = LightGrey
)

@Composable
fun UniMatchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes
    ) {
        // Aplica el fondo aquí al contenedor principal de tu pantalla
        Surface(
            color = MaterialTheme.colorScheme.background // El color de fondo se aplica automáticamente
        ) {
            content()
        }
    }
}
