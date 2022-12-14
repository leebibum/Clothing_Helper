package com.leebeebeom.clothinghelper.ui.signin.base

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.state.GoogleButtonUIState
import com.leebeebeom.clothinghelperdata.repository.A_NETWORK_ERROR
import com.leebeebeom.clothinghelperdata.repository.util.TAG
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import kotlinx.coroutines.launch

abstract class GoogleSignInUpViewModel(private val googleSignInUseCase: GoogleSignInUseCase) :
    ViewModel() {
    abstract val uiState: GoogleButtonUIState

    fun signInWithGoogleEmail(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            RESULT_OK -> googleSignIn(activityResult)
            RESULT_CANCELED -> {
                uiState.showToast(R.string.canceled)
                uiState.updateGoogleButtonEnabled(enabled = true)
            }
            else -> {
                Log.e(TAG, "signInWithGoogleEmail: resultCode = ${activityResult.resultCode}")
                unknownFail()
            }
        }
    }

    private fun googleSignIn(activityResult: ActivityResult) {
        viewModelScope.launch {
            val result =
                googleSignInUseCase.googleSignIn(
                    credential = getGoogleCredential(activityResult),
                    onSubCategoriesLoadFail = { uiState.showToast(R.string.data_load_failed) }
                )

            when (result) {
                is AuthResult.Success -> {
                    uiState.showToast(R.string.google_sign_in_complete)
                    uiState.updateGoogleButtonEnabled(enabled = true)
                }
                is AuthResult.Fail ->
                    if (result.errorCode == A_NETWORK_ERROR) uiState.showToast(R.string.network_error)
                    else unknownFail()
                is AuthResult.UnknownFail -> unknownFail()
            }
        }
    }

    private fun getGoogleCredential(activityResult: ActivityResult): AuthCredential {
        val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }

    private fun unknownFail() {
        uiState.showToast(R.string.unknown_error)
        uiState.updateGoogleButtonEnabled(enabled = true)
    }
}