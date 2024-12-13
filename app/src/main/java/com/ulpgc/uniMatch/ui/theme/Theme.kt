package com.ulpgc.uniMatch.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define las formas (shapes) con esquinas redondeadas
val shapes = Shapes(
    small = RoundedCornerShape(Dimens.smallRadius),   // Esquinas redondeadas pequeñas
    medium = RoundedCornerShape(Dimens.mediumRadius), // Esquinas redondeadas medianas
    large = RoundedCornerShape(Dimens.largeRadius)    // Sin esquinas redondeadas (rectas)
)


// Define los esquemas de color para los temas claros y oscuros con tonalidades rosadas
private val DarkColorScheme = darkColorScheme(
    primary = MainColor,   // Rosado fuerte
    onPrimary = Color(0xFFDB5FFF),
    secondary= Color(0xFFFFFFFF),  // Gris oscuro
    onSecondary = Color.LightGray,
    tertiary = Color(0xFFF1C9FF), // Rosado pastel
    onTertiary = Color.White,
    background = Color(0xFF282828),
    onBackground = Color.White,
    surface = Color(0xFF343434),
    onSurface = Color(0xFFECECEC),
    inversePrimary = Color.Black

)

private val LightColorScheme = lightColorScheme(
    primary = MainColor,   // Rosado fuerte
    onPrimary = Color(0xFFDB5FFF),
    secondary = Color(0xFF545454),  // Gris oscuro
    onSecondary =  Color(0xFFABABAB), // Rosado pastel
    tertiary = Color(0xFFDADADA), // Rosado pastel
    onTertiary =  Color(0xFF424242), // Rosado pastel
    background = Color(0xFFFFFBFA),
    onBackground = Color.Black,
    surface = Color(0xFFFFFBFA),
    onSurface = Color(0xFF343434),
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
