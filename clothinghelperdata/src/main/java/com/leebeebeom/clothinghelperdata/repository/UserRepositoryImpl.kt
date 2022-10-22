package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TODO 중복 코드 제거, 코드 정리

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn: StateFlow<Boolean> get() = _isSignIn

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user: StateFlow<User?> get() = _user

    override fun googleSignIn(
        googleCredential: Any?,
        googleSignInListener: FirebaseListener,
        pushInitialSubCategories: (String) -> Unit
    ) {
        val authCredential = googleCredential as AuthCredential

        auth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user.toUser()!!
                it.result.additionalUserInfo?.isNewUser?.let { isNewUser ->
                    if (isNewUser) pushFirstUserData(user, pushInitialSubCategories)
                }
                signInSuccess(user)
                googleSignInListener.taskSuccess()
            } else googleSignInListener.taskFailed(it.exception)
        }
    }

    override fun signIn(email: String, password: String, signInListener: FirebaseListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signInSuccess(it.result.user.toUser()!!)
                signInListener.taskSuccess()
            } else signInListener.taskFailed(it.exception)
        }
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener,
        updateNameListener: FirebaseListener,
        pushInitialSubCategories: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser = it.result.user!!
                val user = firebaseUser.toUser()!!
                pushFirstUserData(user, pushInitialSubCategories)
                signInSuccess(user)
                signUpListener.taskSuccess()
                updateName(updateNameListener, firebaseUser, name)
            } else signUpListener.taskFailed(it.exception)
        }
    }

    private fun updateName(updateNameListener: FirebaseListener, user: FirebaseUser, name: String) {
        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) {
                val newNameUser = user.toUser()!!
                pushUser(newNameUser)
                updateUser(newNameUser)
                updateNameListener.taskSuccess()
            } else updateNameListener.taskFailed(null)
        }
    }

    override fun resetPasswordEmail(email: String, resetPasswordListener: FirebaseListener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
        }
    }

    private fun pushFirstUserData(user: User, writeInitialSubCategory: (String) -> Unit) {
        pushUser(user)
        writeInitialSubCategory(user.uid)
    }

    private fun pushUser(user: User) =
        FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
            .setValue(user).addOnCompleteListener {
                if (!it.isSuccessful) throw Exception("pushUser 실패")
            }

    override fun signOut() {
        auth.signOut()
        _isSignIn.value = false
        updateUser(null)
    }

    private fun signInSuccess(user: User) {
        _isSignIn.value = true
        updateUser(user)
    }

    private fun updateUser(user: User?) {
        this._user.value = user
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }