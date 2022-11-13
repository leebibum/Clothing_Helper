package com.leebeebeom.clothinghelper.main.subcategory

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.AddSubCategoryVIewModel
import com.leebeebeom.clothinghelper.main.base.BaseIsAllExpandState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.taskAndReturnSet
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategoryAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    private val subCategoryAllExpandUseCase: SubCategoryAllExpandUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase,
    addSubCategoryUseCase: AddSubCategoryUseCase,
) : AddSubCategoryVIewModel(addSubCategoryUseCase) {

    private val uiStates = SubCategoryUIState()

    fun getUiStates(parent: SubCategoryParent): SubCategoryUIState {
        uiStates.setSubCategories(parent)
        return uiStates
    }

    init {
        viewModelScope.launch {
            getUserUseCase().collect(uiStates::updateUser)
        }

        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase().collect(uiStates::updateIsLoading)
        }

        viewModelScope.launch {
            getSubCategoriesUseCase().collect(uiStates::updateAllSubCategories)
        }

        viewModelScope.launch {
            subCategoryAllExpandUseCase.isAllExpand.collect(uiStates::updateIsAllExpand)
        }

        viewModelScope.launch {
            subCategorySortUseCase.sortPreferences.collect(uiStates::updateSort)
        }
    }

    override val user get() = uiStates.user

    override fun showToast(text: Int) {
        uiStates.showToast(text)
    }

    fun toggleAllExpand() {
        viewModelScope.launch {
            subCategoryAllExpandUseCase.toggleAllExpand()
        }
    }

    fun editSubCategoryName(newName: String) {
        viewModelScope.launch {
            uiStates.user?.let { user ->
                uiStates.firstSelectedSubCategoryKey?.let { key ->
                    uiStates.selectModeOff()
                    val result = editSubCategoryNameUseCase(
                        parent = uiStates.parent,
                        key = key,
                        newName = newName.trim(),
                        uid = user.uid
                    )

                    if (result is FirebaseResult.Fail) {
                        uiStates.showToast(R.string.add_category_failed)
                        Log.d(TAG, "taskFailed: $result")
                    }
                } ?: uiStates.showToast(R.string.add_category_failed)
            } ?: uiStates.showToast(R.string.add_category_failed)
        }
    }

    fun changeSort(sort: SubCategorySort) {
        viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }
    }

    fun changeOrder(order: SortOrder) {
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }
    }
}

class SubCategoryUIState : BaseIsAllExpandState() {
    var parent = SubCategoryParent.TOP
        private set
    var sort by mutableStateOf(SubCategorySortPreferences())
        private set

    fun updateSort(sort: SubCategorySortPreferences) {
        this.sort = sort
    }

    var isSelectMode by mutableStateOf(false)
        private set

    var selectedSubCategoryKeys by mutableStateOf(emptySet<String>().toImmutableSet())
        private set
    val selectedSubCategoryKeysSize by derivedStateOf { selectedSubCategoryKeys.size }
    val firstSelectedSubCategoryKey by derivedStateOf { selectedSubCategoryKeys.firstOrNull() }

    var subCategories by mutableStateOf(emptyList<StableSubCategory>().toImmutableList())
        private set
    var subCategoryNames by mutableStateOf(emptyList<String>().toImmutableList())
        private set
    val selectedSubCategoryName by derivedStateOf {
        val firstKey = selectedSubCategoryKeys.firstOrNull()
        firstKey?.let { key -> subCategories.firstOrNull { it.key == key }?.name ?: "" } ?: ""
    }

    val isAllSelected by derivedStateOf { selectedSubCategoryKeys.size == subCategories.size }
    val showEditIcon by derivedStateOf { selectedSubCategoryKeysSize == 1 }
    val showDeleteIcon by derivedStateOf { selectedSubCategoryKeysSize > 0 }

    fun setSubCategories(parent: SubCategoryParent) {
        this.parent = parent
        subCategories = getSubCategories(parent)
        subCategoryNames = getSubCategoryNames(parent)
    }

    fun toggleAllSelect() {
        selectedSubCategoryKeys =
            if (selectedSubCategoryKeysSize == subCategories.size) emptySet<String>().toImmutableSet()
            else subCategories.map { it.key }.toImmutableSet()

    }

    fun onSelect(key: String) {
        selectedSubCategoryKeys =
            if (selectedSubCategoryKeys.contains(key))
                selectedSubCategoryKeys.taskAndReturnSet { it.remove(key) }
            else selectedSubCategoryKeys.taskAndReturnSet { it.add(key) }
    }

    fun selectModeOn(key: String) {
        onSelect(key)
        isSelectMode = true
    }

    suspend fun selectModeOff() {
        isSelectMode = false
        delay(200)
        clearSelectedSubCategories()
    }

    private fun clearSelectedSubCategories() {
        selectedSubCategoryKeys = selectedSubCategoryKeys.taskAndReturnSet { it.clear() }
    }
}