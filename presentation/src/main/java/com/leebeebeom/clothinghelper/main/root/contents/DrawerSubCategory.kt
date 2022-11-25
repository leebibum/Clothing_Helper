package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.components.DrawerCount
import com.leebeebeom.clothinghelper.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus.DrawerSubCategoryDropDownMenu
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.theme.DarkGray
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerSubCategory(
    subCategory: () -> StableSubCategory,
    onClick: () -> Unit,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit,
    onAddFolderPositiveClick: (StableFolder) -> Unit,
    folders: (key: String) -> ImmutableList<StableFolder>,
    onFolderClick: (StableFolder) -> Unit,
    onEditFolderPositiveClick: (StableFolder) -> Unit
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }

    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(start = 8.dp),
        onClick = onClick,
        onLongClick = { showDropDownMenu = true }
    ) {
        Row(
            Modifier
                .padding(start = 4.dp)
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SingleLineText(
                    text = subCategory().name,
                    style = MaterialTheme.typography.subtitle1
                )
                DrawerSubCategoryDropDownMenu(
                    show = { showDropDownMenu },
                    onDismiss = { showDropDownMenu = false },
                    subCategories = subCategories,
                    subCategory = subCategory,
                    onEditSubCategoryPositiveClick = onEditSubCategoryNamePositiveClick,
                    onAddFolderPositiveClick = onAddFolderPositiveClick,
                    folders = { folders(subCategory().key) },
                )
                DrawerCount { folders(subCategory().key) }
            }
            DrawerExpandIcon(
                isLoading = { false },
                isExpanded = { isExpand },
                onClick = { isExpand = !isExpand },
                items = { folders(subCategory().key) }
            )
        }
    }

    DrawerItems(
        show = { isExpand },
        items = { folders(subCategory().key) },
        backGround = DarkGray
    ) {
        DrawerFolder(
            folder = { it },
            onClick = { onFolderClick(it) },
            startPadding = 24.dp,
            folders = folders,
            onAddFolderPositiveClick = { newName ->
                onAddFolderPositiveClick(
                    StableFolder(parent = subCategory().parent, parentKey = it.key, name = newName)
                )
            },
            onEditFolderPositiveClick = { newName ->
                onEditFolderPositiveClick(it.copy(name = newName))
            }
        )
    }
}