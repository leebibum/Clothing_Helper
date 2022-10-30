package com.leebeebeom.clothinghelper.signin.signin

import androidx.compose.ui.text.input.ImeAction
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInUpViewModel
import com.leebeebeom.clothinghelper.signin.base.GoogleSignInViewModelState
import com.leebeebeom.clothinghelper.signin.base.setFireBaseError
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.user.GoogleSignInUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    googleSignInUseCase: GoogleSignInUseCase
) : GoogleSignInUpViewModel(googleSignInUseCase) {

    override val viewModelState = SignInViewModelState()

    fun signInWithEmailAndPassword() {
        signInUseCase(viewModelState.email, viewModelState.password) {
            when (it) {
                is FirebaseResult.Success -> viewModelState.showToast(R.string.sign_in_complete)
                is FirebaseResult.Fail -> setFireBaseError(
                    exception = it.exception,
                    updateEmailError = viewModelState.emailState::updateError,
                    updatePasswordError = viewModelState.passwordState::updateError,
                    showToast = viewModelState::showToast
                )
            }
        }
    }
}

class SignInViewModelState : GoogleSignInViewModelState() {
    val emailState = MaxWidthTextFieldState.email(ImeAction.Next)
    val passwordState = MaxWidthTextFieldState.password(ImeAction.Done)

    val email get() = emailState.textFiled.text
    val password get() = passwordState.textFiled.text
}