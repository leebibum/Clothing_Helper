package com.leebeebeom.clothinghelperdata.repository.preference

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdomain.repository.preference.SortPreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderPreferencesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    SortPreferenceRepository by SortPreferenceRepositoryImpl(context.folderDatastore)

private const val FOLDER = "folder_preferences"
private val Context.folderDatastore by preferencesDataStore(name = FOLDER)