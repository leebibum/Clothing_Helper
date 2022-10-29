package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TODO EXPAND 프리퍼런스로 이동??

class SubCategoryRepositoryImpl(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) :
    SubCategoryRepository {
    private val root = Firebase.database.reference
    private val user = MutableStateFlow(userRepository.user.value)

    private val _allSubCategories = List(4) { MutableStateFlow(emptyList<SubCategory>()) }
    override val allSubCategories: List<StateFlow<List<SubCategory>>>
        get() = _allSubCategories

    override suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: () -> Unit,
        onSubCategoriesLoadingCancelled: (errorCode: Int, message: String) -> Unit
    ) {
        userRepository.user.collect {
            this.user.value = it
            it?.let {
                root.getSubCategoriesRef(it.uid).orderByChild("parent")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val temp = List(4) { mutableListOf<SubCategory>() }

                            for (child in snapshot.children) child.getValue<SubCategory>()
                                ?.let { subCategory ->
                                    temp[subCategory.parent.ordinal].add(subCategory)
                                }
                            _allSubCategories.forEachIndexed { i, subCategoriesFlow ->
                                subCategoriesFlow.value = temp[i]
                            }
                            onSubCategoriesLoadingDone()
                        }

                        override fun onCancelled(error: DatabaseError) =
                            onSubCategoriesLoadingCancelled(error.code, error.message)
                    })
            }
        }

    }

    override suspend fun sortSubCategories() {
        preferencesRepository.subCategoryPreferences.collect {
            when (it.sort) {
                SubCategorySort.NAME -> sortByName(it.sortOrder)
                SubCategorySort.CREATE -> sortByCreate(it.sortOrder)
            }
        }
    }

    private fun sortByName(sortOrder: SortOrder) {
        when (sortOrder) {
            SortOrder.ASCENDING -> {
                _allSubCategories.forEach { subCategories ->
                    subCategories.taskAndAssign {
                        it.sortBy { temp -> temp.name }
                    }
                }
            }
            SortOrder.DESCENDING -> {
                _allSubCategories.forEach { subCategories ->
                    subCategories.taskAndAssign {
                        it.sortByDescending { temp -> temp.name }
                    }
                }
            }
        }
    }

    private fun sortByCreate(sortOrder: SortOrder) {
        when (sortOrder) {
            SortOrder.ASCENDING -> {
                _allSubCategories.forEach { subCategories ->
                    subCategories.taskAndAssign {
                        it.sortBy { temp -> temp.createDate }
                    }
                }
            }
            SortOrder.DESCENDING -> {
                _allSubCategories.forEach { subCategories ->
                    subCategories.taskAndAssign {
                        it.sortByDescending { temp -> temp.createDate }
                    }
                }
            }
        }
    }

    override fun pushInitialSubCategories(uid: String) {
        val subCategoryRef = root.getSubCategoriesRef(uid)

        for (subCategory in getInitialSubCategories()) pushInitialData(subCategoryRef, subCategory)
    }

    private fun pushInitialData(subCategoryRef: DatabaseReference, subCategory: SubCategory) {
        val key = subCategoryRef.push().key ?: return
        subCategoryRef.child(key).setValue(subCategory.copy(key = key))
    }

    override fun addSubCategory(
        subCategoryParent: SubCategoryParent, name: String, taskFailed: (Exception?) -> Unit
    ) {
        val key = root.getSubCategoriesRef(user.value!!.uid).push().key
        if (key == null) {
            taskFailed(NullPointerException("키 생성 실패"))
            return
        }

        val newSubCategory = SubCategory(
            parent = subCategoryParent,
            key = key,
            name = name,
            createDate = System.currentTimeMillis()
        )

        user.value?.let {
            root.getSubCategoriesRef(it.uid).child(key).setValue(newSubCategory)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _allSubCategories[subCategoryParent.ordinal].taskAndAssign { temp ->
                            temp.add(
                                newSubCategory
                            )
                        }
                    } else taskFailed(task.exception)
                }
        }

    }

    override fun editSubCategoryName(subCategory: SubCategory, newName: String) {
        user.value?.run {
            root.getSubCategoriesRef(uid).child(subCategory.key).child("name").setValue(newName)
                .addOnSuccessListener {
                    _allSubCategories[subCategory.parent.ordinal].taskAndAssign {
                        val index = it.indexOf(subCategory)
                        it.remove(subCategory)
                        it.add(index, subCategory.copy(name = newName))
                    }
                }
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()
        return listOf(
            SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "긴팔", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "셔츠", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 셔츠", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 니트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트 베스트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "데님", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "반바지", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "슬랙스", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "스웻", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "나일론", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "치노", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "코트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "자켓", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "바람막이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "항공점퍼", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "블루종", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "점퍼", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "야상", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "신발", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "목걸이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "팔찌", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "귀걸이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "볼캡", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "비니", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "머플러", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "장갑", createDate = timeStamp
            )
        )
    }

    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        child(uid).child(DatabasePath.SUB_CATEGORIES)
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}

inline fun <T> MutableStateFlow<List<T>>.taskAndAssign(crossinline task: (MutableList<T>) -> Unit) {
    val temp = this.value.toMutableList()
    task(temp)
    this.value = temp
}