package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.expandIn
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.shrinkOut
import com.leebeebeom.clothinghelper.base.CircleCheckBox
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryStates
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun SubCategoryBottomAppBar(
    state: State<SubCategoryBottomAppbarState>,
    onAllSelectCheckBoxClick: () -> Unit,
    onEditSubCategoryNameClick: () -> Unit
) {
    AnimatedVisibility(
        visible = state.value.isSelectMode,
        enter = expandIn,
        exit = shrinkOut
    ) {
        BottomAppBar {
            SimpleWidthSpacer(dp = 4)
            CircleCheckBox(
                isChecked = state.value.isAllSelected,
                modifier = Modifier.size(22.dp),
                onClick = onAllSelectCheckBoxClick
            )
            SimpleWidthSpacer(dp = 10)
            Text(
                text = stringResource(
                    id = R.string.count_selected,
                    formatArgs = arrayOf(state.value.selectedSubCategoriesSize)
                ), modifier = Modifier.offset((-8).dp, 1.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {

                if (state.value.showEditName)
                    BottomAppBarIcon(
                        onClick = onEditSubCategoryNameClick,
                        drawable = R.drawable.ic_edit,
                        text = R.string.change_name
                    )

                if (state.value.showDeleted)
                    BottomAppBarIcon(
                        onClick = { /*TODO*/ },
                        drawable = R.drawable.ic_delete2,
                        text = R.string.delete
                    )
            }
            SimpleWidthSpacer(dp = 4)
        }
    }
}

@Composable
fun BottomAppBarIcon(
    onClick: () -> Unit,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomIconButton(onClick = onClick, drawable = drawable)
        SimpleHeightSpacer(dp = 4)
        Text(text = stringResource(id = text), style = MaterialTheme.typography.caption)
    }
}

data class SubCategoryBottomAppbarState(
    val isSelectMode: Boolean,
    val selectedSubCategoriesSize: Int,
    val subCategoriesSize: Int,
) {
    val isAllSelected get() = selectedSubCategoriesSize == subCategoriesSize
    val showEditName get() = selectedSubCategoriesSize == 1
    val showDeleted get() = selectedSubCategoriesSize > 0
}

@Composable
fun rememberSubCategoryBottomAppbarState(
    subCategoryStates: SubCategoryStates,
    subCategoriesState: State<List<SubCategory>>
) = remember {
    derivedStateOf {
        SubCategoryBottomAppbarState(
            isSelectMode = subCategoryStates.isSelectMode,
            selectedSubCategoriesSize = subCategoryStates.selectedSubCategoriesSize,
            subCategoriesSize = subCategoriesState.value.size
        )
    }
}