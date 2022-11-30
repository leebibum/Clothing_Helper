package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdomain.model.data.Folder
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    folderPreferencesRepository: FolderPreferencesRepository
) : BaseDataRepositoryImpl<Folder>(),
    FolderRepository {
    override val allFolders = getSortedContainers(folderPreferencesRepository.sort)

    override val refPath = DatabasePath.FOLDERS

    override fun getNewContainer(t: Folder, key: String, createDate: Long) =
        t.copy(key = key, createDate = createDate, editDate = createDate)

    override fun getContainerWithNewEditDate(newT: Folder, editDate: Long) =
        newT.copy(editDate = editDate)
}