package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort

@Composable
fun SubCategoryContent(
    mainCategoryName: String,
    allExpandIconClick: () -> Unit,
    isAllExpand: Boolean,
    subCategories: List<SubCategory>,
    onLongClick: (SubCategory) -> Unit,
    isSelectMode: Boolean,
    onSubCategoryClick: (SubCategory) -> Unit,
    selectedSubCategories: Set<SubCategory>,
    selectedSort: SubCategorySort,
    selectedOder: SortOrder,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Header(
                mainCategoryName = mainCategoryName,
                allExpandIconClick = allExpandIconClick,
                allExpand = isAllExpand,
                selectedSort = selectedSort,
                selectedOder = selectedOder,
                onSortClick = onSortClick,
                onOrderClick = onOrderClick
            )
        }
        items(items = subCategories,
            key = { subCategory -> subCategory.key }) {
            SubCategoryCard(
                subCategory = it,
                onLongClick = { onLongClick(it) },
                isSelectMode = isSelectMode,
                onSubCategoryClick = { onSubCategoryClick(it) },
                isAllExpand = isAllExpand,
                isChecked = selectedSubCategories.contains(it)
            )
        }
    }
}

@Composable
private fun Header(
    mainCategoryName: String,
    allExpandIconClick: () -> Unit,
    allExpand: Boolean,
    selectedSort: SubCategorySort,
    selectedOder: SortOrder,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(id = getHeaderStringRes(mainCategoryName)),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )

    // Header Icons
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))

        AllExpandIcon(
            allExpandIconClick = allExpandIconClick,
            allExpand = allExpand
        )
        SortIcon(
            selectedSort = selectedSort,
            selectedOder = selectedOder,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick
        )
    }
}

fun getHeaderStringRes(mainCategoryName: String) =
    when (mainCategoryName) {
        SubCategoryParent.TOP.name -> R.string.top
        SubCategoryParent.BOTTOM.name -> R.string.bottom
        SubCategoryParent.OUTER.name -> R.string.outer
        else -> R.string.etc
    }

@Composable
private fun AllExpandIcon(allExpandIconClick: () -> Unit, allExpand: Boolean) {
    Box(modifier = Modifier.offset(4.dp, 0.dp)) {
        AllExpandIcon(
            size = 22.dp,
            allExpandIconClick = allExpandIconClick,
            tint = LocalContentColor.current.copy(0.5f),
            allExpand = allExpand,
            rippleSize = 2.dp
        )
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AllExpandIcon(
    size: Dp,
    allExpandIconClick: () -> Unit,
    tint: Color,
    allExpand: Boolean,
    rippleSize: Dp = 4.dp
) {
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.all_expand_anim
        ), atEnd = allExpand
    )

    CustomIconButton(
        modifier = Modifier.size(size),
        onClick = allExpandIconClick,
        painter = painter,
        tint = tint,
        rippleSize = rippleSize
    )
}

@Composable
private fun SortIcon(
    selectedSort: SubCategorySort,
    selectedOder: SortOrder,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
) {
    var showDropDownMenu by remember { mutableStateOf(false) }

    Box {
        CustomIconButton(
            modifier = Modifier.size(22.dp),
            onClick = { showDropDownMenu = true },
            drawable = R.drawable.ic_sort,
            tint = LocalContentColor.current.copy(0.5f),
            rippleSize = 2.dp
        )

        SortDropdownMenu(
            showDropDownMenu = showDropDownMenu,
            selectedSort = selectedSort,
            selectedOrder = selectedOder,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick
        ) { showDropDownMenu = false }
    }
}