package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User

open class BaseMainUIState : BaseUIState() {
    var isLoading by mutableStateOf(false)
        private set
    var allSubCategories by mutableStateOf(List(4) { emptyList<SubCategory>() })
        private set

    private val topSubCategories by derivedStateOf { allSubCategories[0] }
    private val bottomSubCategories by derivedStateOf { allSubCategories[1] }
    private val outerSubCategories by derivedStateOf { allSubCategories[2] }
    private val etcSubCategories by derivedStateOf { allSubCategories[3] }

    private val topSubCategoriesSize by derivedStateOf { topSubCategories.size }
    private val bottomSubCategoriesSize by derivedStateOf { bottomSubCategories.size }
    private val outerSubCategoriesSize by derivedStateOf { outerSubCategories.size }
    private val etcSubCategoriesSize by derivedStateOf { etcSubCategories.size }

    fun getSubCategories(subCategoryParent: SubCategoryParent) = when (subCategoryParent) {
        SubCategoryParent.TOP -> topSubCategories
        SubCategoryParent.BOTTOM -> bottomSubCategories
        SubCategoryParent.OUTER -> outerSubCategories
        SubCategoryParent.ETC -> etcSubCategories
    }

    fun subCategoriesSize(subCategoryParent: SubCategoryParent) = when (subCategoryParent) {
        SubCategoryParent.TOP -> topSubCategoriesSize
        SubCategoryParent.BOTTOM -> bottomSubCategoriesSize
        SubCategoryParent.OUTER -> outerSubCategoriesSize
        SubCategoryParent.ETC -> etcSubCategoriesSize
    }

    fun updateIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        this.allSubCategories = allSubCategories
    }
}

open class BaseIsAllExpandState : BaseMainUIState() {
    var user: User? by mutableStateOf(null)
        private set
    var isAllExpand by mutableStateOf(false)
        private set

    fun updateUser(user: User?) {
        this.user = user
    }

    fun updateIsAllExpand(isAllExpand: Boolean) {
        this.isAllExpand = isAllExpand
    }
}