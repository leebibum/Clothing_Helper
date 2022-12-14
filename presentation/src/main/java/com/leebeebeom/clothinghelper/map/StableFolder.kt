package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.data.BaseFolderModel
import com.leebeebeom.clothinghelperdomain.model.data.Folder
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

data class StableFolder(
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val name: String = "",
    override val key: String = "",
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseFolderModel() {
    override fun addKey(key: String) = copy(key = key)
}

fun Folder.toStable() = StableFolder(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    createDate = createDate,
    name = name,
    key = key,
    editDate = editDate,
    parent = parent
)

fun StableFolder.toUnstable() = Folder(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    createDate = createDate,
    name = name,
    key = key,
    editDate = editDate,
    parent = parent
)