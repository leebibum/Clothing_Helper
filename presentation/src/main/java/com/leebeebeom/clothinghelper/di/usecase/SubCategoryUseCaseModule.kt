package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SubCategoryUseCaseModule {
    @Provides
    fun provideAddSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        AddSubCategoryUseCase(subCategoryRepository)

    @Provides
    fun provideLoadAndGetSubCategoriesUseCase(
        loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
        getSubCategoriesUseCase: GetSubCategoriesUseCase
    ) = LoadAndGetSubCategoriesUseCase(loadSubCategoriesUseCase, getSubCategoriesUseCase)

    @Provides
    fun provideDeleteSubCategoryUseCase(subCategoryUseCase: SubCategoryRepositoryImpl) =
        DeleteSubCategoryUseCase(subCategoryUseCase)
}