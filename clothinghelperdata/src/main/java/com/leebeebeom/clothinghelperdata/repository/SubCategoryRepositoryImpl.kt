package com.leebeebeom.clothinghelperdata.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SubCategoryRepositoryImpl : SubCategoryRepository {
    private val root = Firebase.database.reference

    private val _isLoading = MutableStateFlow(true)
    override val isLoading get() = _isLoading.asStateFlow()

    private val _allSubCategories = MutableStateFlow(List(4) { emptyList<SubCategory>() })
    override val allSubCategories get() = _allSubCategories.asStateFlow()

    override suspend fun loadSubCategories(user: User?) = withContext(Dispatchers.IO) {
        try {
            loadingOn()

            user?.let {
                val temp = List(4) { mutableListOf<SubCategory>() }

                root.getSubCategoriesRef(it.uid).get().await().children.forEach { data ->
                    val subCategory = data.getValue(SubCategory::class.java)!!
                    temp[subCategory.parent.ordinal].add(subCategory)
                }
                _allSubCategories.update { temp }
            }
            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Fail(e)
        } finally {
            Log.d("TAG", "loadSubCategories: 호출")
            loadingOff()
        }
    }

    override suspend fun pushInitialSubCategories(uid: String): SubCategoryPushResult =
        withContext(Dispatchers.IO) {
            try {
                val subCategoryRef = root.getSubCategoriesRef(uid)

                for (subCategory in getInitialSubCategories()) {
                    loadingOn()
                    val pushResult = pushSubCategory(subCategoryRef, subCategory)
                    if (pushResult is SubCategoryPushResult.Fail) {
                        return@withContext pushResult
                    }
                }

                SubCategoryPushResult.Success(SubCategory()) // dummy
            } catch (e: Exception) {
                SubCategoryPushResult.Fail(e)
            } finally {
                loadingOff()
            }
        }

    private suspend fun pushSubCategory(
        subCategoryRef: DatabaseReference, subCategory: SubCategory
    ): SubCategoryPushResult = withContext(Dispatchers.IO) {
        val key = subCategoryRef.push().key ?: return@withContext SubCategoryPushResult.Fail(
            Exception("key 생성 실패")
        )

        val subCategoryWithKey = subCategory.copy(key = key)

        try {
            subCategoryRef.child(key).setValue(subCategoryWithKey).await()
            SubCategoryPushResult.Success(subCategoryWithKey)
        } catch (e: Exception) {
            SubCategoryPushResult.Fail(e)
        }
    }

    override suspend fun addSubCategory(
        subCategoryParent: SubCategoryParent, name: String, uid: String
    ): FirebaseResult = withContext(Dispatchers.IO) {
        try {
            val newSubCategory = SubCategory(
                parent = subCategoryParent, name = name, createDate = System.currentTimeMillis()
            )

            val subCategoryRef = root.getSubCategoriesRef(uid)

            when (val result = pushSubCategory(subCategoryRef, newSubCategory)) {
                is SubCategoryPushResult.Success -> {
                    _allSubCategories.update {
                        val allSubCategories = it.toMutableList()
                        val addedSubCategories =
                            allSubCategories[subCategoryParent.ordinal].toMutableList()
                        addedSubCategories.add(result.subCategory)
                        allSubCategories[subCategoryParent.ordinal] = addedSubCategories
                        allSubCategories
                    }
                    FirebaseResult.Success
                }
                is SubCategoryPushResult.Fail -> FirebaseResult.Fail(result.exception)
            }
        } catch (e: Exception) {
            FirebaseResult.Fail(e)
        }

    }

    override suspend fun editSubCategoryName(
        subCategory: SubCategory, newName: String, uid: String
    ): FirebaseResult = withContext(Dispatchers.IO) {
        try {
            root.getSubCategoriesRef(uid).child(subCategory.key).child("name").setValue(newName)
                .await()
            val allSubCategorisesTemp = _allSubCategories.value.toMutableList()
            val editedSubCategoriesTemp =
                allSubCategorisesTemp[subCategory.parent.ordinal].toMutableList()
            editedSubCategoriesTemp.remove(subCategory)
            editedSubCategoriesTemp.add(subCategory.copy(name = newName))
            allSubCategorisesTemp[subCategory.parent.ordinal] = editedSubCategoriesTemp
            _allSubCategories.value = allSubCategorisesTemp
            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Fail(e)
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

    private suspend fun loadingOn() = withContext(Dispatchers.Main) {
        _isLoading.value = true
    }

    private suspend fun loadingOff() = withContext(Dispatchers.IO) {
        _isLoading.value = false
    }

    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        child(uid).child(DatabasePath.SUB_CATEGORIES)
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}