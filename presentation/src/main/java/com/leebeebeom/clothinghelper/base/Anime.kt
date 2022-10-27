package com.leebeebeom.clothinghelper.base

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

object Anime {
    private const val screenSlideDuration = 350
    private val screenSlideEasing = CubicBezierEasing(a = 0.36f, b = 0f, c = 0.66f, d = -0.1f)

    val screenSlideInBottom = slideInVertically(animationSpec = tween(screenSlideDuration)) { it }

    val screenSlideOutBottom =
        slideOutVertically(
            animationSpec = tween(
                screenSlideDuration,
                easing = screenSlideEasing
            )
        ) { it }

    val errorSlideInBottom = slideInVertically(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val errorSlideOutBottom = slideOutVertically(tween(250)) { it }
}