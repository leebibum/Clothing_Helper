package com.leebeebeom.clothinghelper.ui.main.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.CustomIconButton
import com.leebeebeom.clothinghelper.util.Anime.ExpandIcon.rotateSpec

@Composable
fun ExpandIcon(
    modifier: Modifier = Modifier,
    isExpanded: () -> Boolean,
    onClick: () -> Unit
) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded()) 0f else 180f,
        animationSpec = rotateSpec
    )

    CustomIconButton(
        modifier = modifier.rotate(rotate),
        onClick = onClick,
        drawable = R.drawable.ic_expand_more,
        tint = LocalContentColor.current.copy(alpha = 0.6f),
    )
}