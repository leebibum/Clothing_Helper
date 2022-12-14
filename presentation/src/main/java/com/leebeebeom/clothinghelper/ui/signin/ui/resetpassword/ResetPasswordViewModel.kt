package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.state.ToastUIState
import com.leebeebeom.clothinghelper.state.ToastUIStateImpl
import com.leebeebeom.clothinghelper.ui.signin.base.setFireBaseError
import com.leebeebeom.clothinghelper.ui.signin.state.EmailUIState
import com.leebeebeom.clothinghelper.ui.signin.state.EmailUIStateImpl
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.user.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) :
    ViewModel() {

    val uiState = ResetPasswordUIState()

    fun sendResetPasswordEmail() =
        viewModelScope.launch {
            when (val result = resetPasswordUseCase.sendResetPasswordEmail(email = uiState.email)) {
                is AuthResult.Success -> {
                    uiState.showToast(R.string.email_send_complete)
                    uiState.updateIsTaskSuccess(true)
                }
                is AuthResult.Fail -> setFireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = uiState::updateEmailError,
                    showToast = uiState::showToast
                )
                is AuthResult.UnknownFail -> uiState.showToast(R.string.unknown_error)
            }
        }
}

class ResetPasswordUIState : EmailUIState by EmailUIStateImpl(),
    ToastUIState by ToastUIStateImpl() {
    var isTaskSuccess by mutableStateOf(false)
        private set

    fun updateIsTaskSuccess(taskSuccess: Boolean) {
        isTaskSuccess = taskSuccess
    }

    fun consumeIsTaskSuccess() {
        isTaskSuccess = false
    }
}