package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User

open class BaseMainUIState : BaseUIState() {
    private val _isLoading = mutableStateOf(false)
    private val _allSubCategories = mutableStateOf(List(4) { emptyList<SubCategory>() })

    val isLoading by derivedStateOf { _isLoading.value }
    val allSubCategories by derivedStateOf { _allSubCategories.value }

    fun subCategoriesSize(subCategoryParent: SubCategoryParent) =
        derivedStateOf { allSubCategories[subCategoryParent.ordinal].size }

    fun updateIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        _allSubCategories.value = allSubCategories
    }
}

open class BaseIsAllExpandState : BaseMainUIState() {
    private val _user: MutableState<User?> = mutableStateOf(null)
    private val _isAllExpand = mutableStateOf(false)

    val user by derivedStateOf { _user.value }
    val isAllExpand by derivedStateOf { _isAllExpand.value }

    fun updateUser(user: User?) {
        _user.value = user
    }

    fun updateIsAllExpand(isAllExpand: Boolean) {
        _isAllExpand.value = isAllExpand
    }
}