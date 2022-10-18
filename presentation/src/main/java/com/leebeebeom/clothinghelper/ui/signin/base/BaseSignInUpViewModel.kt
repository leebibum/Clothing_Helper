package com.leebeebeom.clothinghelper.ui.signin.base

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.repository.FireBaseListeners
import com.leebeebeom.clothinghelper.ui.TAG

abstract class BaseSignInUpViewModel : ViewModel() {
    abstract fun showToast(@StringRes toastText: Int)
    abstract fun loadingOn()
    abstract fun loadingOff()
    abstract fun googleButtonDisable()
    abstract fun googleButtonEnable()

    protected val googleSignInListener = object : FireBaseListeners.GoogleSignInListener {
        override fun googleSignInFailed(activityResult: ActivityResult) {
            if (activityResult.data == null) {
                showToast(R.string.unknown_error)
                Log.d(TAG, "googleSignInFailed: activityResult.data = null")
            } else
                if (activityResult.resultCode == Activity.RESULT_CANCELED) showToast(R.string.canceled)
                else {
                    showToast(R.string.unknown_error)
                    Log.d(TAG, "googleSignInFailed: $activityResult")
                }
        }

        override fun taskStart() {
            loadingOn()
            googleButtonDisable()
        }

        override fun taskSuccess() = showToast(R.string.google_login_complete)

        override fun taskFailed(exception: Exception?) {
            showToast(R.string.unknown_error)
            Log.d(TAG, "taskFailed: $exception")
        }

        override fun taskFinish() {
            loadingOff()
            googleButtonEnable()
        }

    }
}

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}