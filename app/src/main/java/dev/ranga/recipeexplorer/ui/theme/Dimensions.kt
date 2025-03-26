package dev.ranga.recipeexplorer.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Dimensions(
    val default: Dp = 0.dp,
    val tiny: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val xLarge: Dp = 32.dp,
    val xxLarge: Dp = 64.dp,
    val xxxLarge: Dp = 128.dp,
    val xxxxLarge: Dp = 160.dp,
    val xxxxxLarge: Dp = 256.dp,
    val dim200: Dp = 200.dp,

    val fontSizeTiny: TextUnit = 4.sp,
    val fontSizeSmall: TextUnit = 8.sp,
    val fontSizeMedium: TextUnit = 12.sp,
    val fontSizeLarge: TextUnit = 16.sp,
    val fontSizeXLarge: TextUnit = 20.sp,
)
