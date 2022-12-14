package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.root.model.*
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelper.theme.DarkGray
import com.leebeebeom.clothinghelper.theme.Disabled
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.AddSubCategory
import com.leebeebeom.clothinghelper.util.EditFolder
import com.leebeebeom.clothinghelper.util.EditSubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerContents(
    state: DrawerContentsState = remember { DrawerContentsState() },
    user: () -> StableUser?,
    isLoading: () -> Boolean,
    subCategories: (SubCategoryParent) -> ImmutableList<StableSubCategory>,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    onEssentialMenuClick: (essentialMenu: EssentialMenuType) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onSettingIconClick: () -> Unit,
    onAddSubCategoryPositiveButtonClick: AddSubCategory,
    onEditSubCategoryPositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick : EditFolder
) {
    Column {
        DrawerHeader(user = user, onSettingIconClick = onSettingIconClick)

        Surface(color = DarkGray) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                items(state.essentialMenus, key = { it.name }) {
                    EssentialMenu(
                        essentialMenu = it,
                        onClick = onEssentialMenuClick,
                    )
                }

                item {
                    Divider(
                        color = Disabled,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(state.mainCategories, key = { it.name }) {
                    DrawerMainCategory(
                        mainCategory = it,
                        subCategories = subCategories,
                        isLoading = isLoading,
                        onMainCategoryClick = onMainCategoryClick,
                        onSubCategoryClick = onSubCategoryClick,
                        onAddSubCategoryPositiveClick = onAddSubCategoryPositiveButtonClick,
                        onEditSubCategoryPositiveClick = onEditSubCategoryPositiveClick,
                        onAddFolderPositiveClick = onAddFolderPositiveClick,
                        folders = folders,
                        onFolderClick = onFolderClick,
                        onEditFolderPositiveClick = onEditFolderPositiveClick
                    )
                }
            }
        }
    }
}

data class DrawerContentsState(
    val essentialMenus: ImmutableList<EssentialMenu> = getEssentialMenus(),
    val mainCategories: ImmutableList<MainCategory> = getMainCategories()
)