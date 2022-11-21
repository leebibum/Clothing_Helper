package com.leebeebeom.clothinghelper.util

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf

val LazyListState.showScrollToTopButton
    get() = run { derivedStateOf { firstVisibleItemScrollOffset > 0 }.value }

suspend fun LazyListState.scrollToTop() {
    val scrollAnimationSpec = tween<Float>(300, easing = FastOutLinearInEasing)
    val firstVisibleItemIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index

    firstVisibleItemIndex?.let {
        if (it <= 2) animateScrollBy(-1000f, scrollAnimationSpec)
        else {
            scrollToItem(2)
            animateScrollBy(-1000f, scrollAnimationSpec)
        }
    }
}