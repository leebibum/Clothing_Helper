package com.leebeebeom.clothinghelper.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = Black,
    primaryVariant = Black,
    secondary = Teal200,
    background = WhiteSmoke
)

@Composable
fun ClothingHelperTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography(),
        shapes = Shapes,
        content = content,
    )
}