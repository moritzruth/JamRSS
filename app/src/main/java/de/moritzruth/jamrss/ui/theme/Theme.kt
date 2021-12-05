package de.moritzruth.jamrss.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.moritzruth.jamrss.R

private val RubikFontFamily = FontFamily(
    Font(R.font.rubik),
    Font(R.font.rubik_italic, style = FontStyle.Italic),
    Font(R.font.rubik_bold, weight = FontWeight.Bold)
)

private val white = Color(0xFFF5F5F5)
private val black = Color(0xFF1B1B1B)

private val DarkColorPalette = darkColors(
    primary = white,
    secondary = white,
    secondaryVariant = white,
    background = Color(0xFF292A2E)
)

private val LightColorPalette = lightColors(
    primary = black,
    secondary = black,
    secondaryVariant = black,
    background = Color(0xFFFFFDF9)
)

@Composable
fun JamRSSTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colors.background,
            darkIcons = colors.isLight
        )
    }

    MaterialTheme(
        colors = colors,
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(0.dp)
        ),
        typography = Typography(
            defaultFontFamily = RubikFontFamily
        ),
        content = content
    )
}

val VERTICAL_CONTENT_PADDING = 12.dp
val HORIZONTAL_CONTENT_PADDING = 16.dp
val CONTENT_PADDING = PaddingValues(HORIZONTAL_CONTENT_PADDING, VERTICAL_CONTENT_PADDING)