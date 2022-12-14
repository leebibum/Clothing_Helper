package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.composables.ScrollToTopFab
import com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.SubCategoryCard
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.dragSelect.ListDragSelector
import com.leebeebeom.clothinghelper.util.dragSelect.dragSelect
import com.leebeebeom.clothinghelper.util.scrollToTop
import com.leebeebeom.clothinghelper.util.showScrollToTopFab
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

// TODO 오토스크롤 구현

@Composable
fun SubCategoryContent(
    parent: SubCategoryParent,
    subCategories: () -> ImmutableList<StableSubCategory>,
    sort: () -> SortPreferences,
    state: LazyListState = rememberLazyListState(),
    onLongClick: (key: String) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit,
    selectedSubCategoryKey: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    onSelect: (String) -> Unit,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    folders: (parentKey: String) -> ImmutableList<StableFolder>
) {
    var dragSelectStart by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SubCategoryPlaceHolder(subCategories)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .dragSelect(
                    dragSelector = remember { ListDragSelector(state, haptic) },
                    onDragStart = { dragSelectStart = it },
                    onSelect = onSelect,
                    onLongClick = onLongClick
                ),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state,
            userScrollEnabled = !dragSelectStart
        ) {
            item {
                SubCategoryHeader(
                    parent = parent,
                    sort = sort,
                    onSortClick = onSortClick,
                    onOrderClick = onOrderClick
                )
            }
            subCategories(
                subCategories = subCategories,
                isSelectMode = isSelectMode,
                onSelect = onSelect,
                onSubCategoryClick = onSubCategoryClick,
                selectedSubCategoryKey = selectedSubCategoryKey,
                folders = folders
            )
        }
        ScrollToTopFab(show = { state.showScrollToTopFab }, toTop = state::scrollToTop)
    }
}

@Composable
private fun SubCategoryPlaceHolder(subCategories: () -> ImmutableList<StableSubCategory>) {
    val isEmpty by remember { derivedStateOf { subCategories().isEmpty() } }
    AnimatedVisibility(visible = isEmpty, enter = fadeIn(), exit = fadeOut()) {
        SingleLineText(
            text = R.string.sub_category_place_holder,
            style = MaterialTheme.typography.body1.copy(LocalContentColor.current.copy(0.6f))
        )
    }
}

private fun LazyListScope.subCategories(
    subCategories: () -> ImmutableList<StableSubCategory>,
    isSelectMode: () -> Boolean,
    onSelect: (String) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    selectedSubCategoryKey: () -> ImmutableSet<String>,
    folders: (parentKey: String) -> ImmutableList<StableFolder>
) {
    items(items = subCategories(), key = { it.key }) {
        SubCategoryCard(subCategory = { it }, onClick = {
            if (isSelectMode()) onSelect(it.key) else onSubCategoryClick(it)
        }, selectedCategoryKeys = selectedSubCategoryKey, isSelectMode = isSelectMode,
            folders = { folders(it.key) }
        )
    }
}