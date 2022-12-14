package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.BaseDataRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelperdomain.repository.preference.SubCategoryPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl(private val baseDataRepositoryImpl: BaseDataRepositoryImpl<SubCategory>) :
    BaseDataRepository<SubCategory> by baseDataRepositoryImpl, SubCategoryRepository {

    @Inject
    constructor(
        @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository
    ) : this(
        baseDataRepositoryImpl = BaseDataRepositoryImpl(
            sortFlow = subCategoryPreferencesRepository.sort,
            refPath = DatabasePath.SUB_CATEGORIES
        )
    )

    override suspend fun pushInitialSubCategories(uid: String) = withContext(Dispatchers.IO) {
        val subCategoryRef = baseDataRepositoryImpl.root.getContainerRef(
            uid = uid,
            path = DatabasePath.SUB_CATEGORIES
        )

        getInitialSubCategories().forEach {
            val subCategoryWithKey = it.copy(key = baseDataRepositoryImpl.getKey(subCategoryRef))
            val result = async { baseDataRepositoryImpl.push(subCategoryRef, subCategoryWithKey) }
            result.await()
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()
        return listOf(
            SubCategory(
                parent = SubCategoryParent.TOP,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "?????? ??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "?????? ??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "?????? ?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "????????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "????????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "??????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "?????????",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "??????",
                createDate = timeStamp,
                editDate = timeStamp
            )
        )
    }
}
