package com.leebeebeom.clothinghelperdomain.usecase

import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDataLoadingStateUseCase @Inject constructor(
    subCategoryRepository: SubCategoryRepository,
    folderRepository: FolderRepository
) {
    var isLoading: Flow<Boolean>
        private set

    init {
        isLoading = combine(
            subCategoryRepository.isLoading,
            folderRepository.isLoading
        )
        { isSubCategoryLoading, isFolderLoading ->
            isSubCategoryLoading && isFolderLoading
        }
    }
}