package com.leebeebeom.clothinghelper.ui.signin.ui.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.ui.signin.base.setFireBaseError
import com.leebeebeom.clothinghelper.ui.signin.state.GoogleButtonUIState
import com.leebeebeom.clothinghelper.ui.signin.state.GoogleButtonUIStateImpl
import com.leebeebeom.clothinghelper.ui.signin.state.PasswordUIState
import com.leebeebeom.clothinghelper.ui.signin.state.PasswordUIStateImpl
import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase, googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    override val uiState = SignUpUIState()

    fun signUpWithEmailAndPassword() =
        viewModelScope.launch {
            val result = signUpUseCase.signUp(
                email = uiState.email,
                password = uiState.password,
                name = uiState.name.trim(),
                onSubCategoryLoadFail = { uiState.showToast(R.string.data_load_failed) }
            )
            when (result) {
                is AuthResult.Success -> uiState.showToast(R.string.sign_up_complete)
                is AuthResult.Fail -> setFireBaseError(
                    errorCode = result.errorCode,
                    updateEmailError = uiState::updateEmailError,
                    showToast = uiState::showToast
                )
                is AuthResult.UnknownFail -> uiState.showToast(R.string.unknown_error)
                else -> {}
            }
        }
}

class SignUpUIState(
    private val passwordUIState: PasswordUIStateImpl = PasswordUIStateImpl()
) : PasswordUIState by passwordUIState,
    GoogleButtonUIState by GoogleButtonUIStateImpl() {
    var passwordConfirmError: Int? by mutableStateOf(null)
        private set

    var name by mutableStateOf("")
        private set
    var passwordConfirm by mutableStateOf("")
        private set

    fun onNameChange(name: String) {
        this.name = name
    }

    override fun onPasswordChange(password: String) {
        passwordUIState.onPasswordChange(password)

        if (password.isNotBlank()) {
            if (password.length < 6) updatePasswordError(R.string.error_weak_password)

            if (passwordConfirm.isNotBlank() && password != passwordConfirm)
                updatePasswordConfirmError(R.string.error_password_confirm_not_same)
            else updatePasswordConfirmError(null)
        }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        this.passwordConfirm = passwordConfirm

        if (passwordConfirm.isNotBlank() && password.isNotBlank() && passwordConfirm != password)
            updatePasswordConfirmError(R.string.error_password_confirm_not_same)
    }

    fun updatePasswordConfirmError(@StringRes error: Int?) {
        passwordConfirmError = error
    }

    override val buttonEnabled by derivedStateOf {
        passwordUIState.buttonEnabled && passwordConfirm.isNotBlank() && passwordConfirmError == null && name.isNotBlank()
    }
}