package com.leebeebeom.clothinghelper.data

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.util.StateDelegator

data class TextFieldAttr(
    val labelId: Int,
    private val _visualTransformation: MutableState<VisualTransformation> =
        mutableStateOf(VisualTransformation.None),
    val placeHolderId: Int = R.string.empty,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    private val visibleIconEnable: Boolean = false,
    val textFieldManager: TextFieldManager?
) {
    init {
        textFieldManager?.addAttr(this)
    }

    private val _text = mutableStateOf("")
    var text by StateDelegator(_text)

    val isEmpty get() = text.isBlank()

    private val _errorTextId = mutableStateOf(ERROR_OFF)
    var errorTextId by StateDelegator(_errorTextId)
        private set

    var visualTransformation by StateDelegator(_visualTransformation)
        private set

    var onValueChange: (String) -> Unit = ::onValueChange

    fun onValueChange(newText: String) {
        text = newText
        if (isErrorEnable) errorDisable()
    }

    val isErrorEnable get() = errorTextId != ERROR_OFF

    fun errorEnable(resId: Int) {
        errorTextId = resId
    }

    fun errorDisable() {
        errorTextId = ERROR_OFF
    }

    val visibleIcon = @Composable {
        if (visibleIconEnable)
            IconButton(onClick = { visibleToggle() }) {
                if (isVisible) SimpleIcon(drawableId = R.drawable.ic_eye_close)
                else SimpleIcon(drawableId = R.drawable.ic_eye_open)
            }
    }

    private fun visibleToggle() {
        visualTransformation =
            if (isVisible) PasswordVisualTransformation() else VisualTransformation.None
    }

    private val isVisible get() = visualTransformation == VisualTransformation.None

    companion object Factory {
        private const val ERROR_OFF = -1

        fun signInEmail(textFieldManager: TextFieldManager?) = TextFieldAttr(
            labelId = R.string.email,
            placeHolderId = R.string.example_email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            textFieldManager = textFieldManager
        )

        fun signInPassword(textFieldManager: TextFieldManager?) = TextFieldAttr(
            labelId = R.string.password,
            _visualTransformation = mutableStateOf(PasswordVisualTransformation()),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visibleIconEnable = true,
            textFieldManager = textFieldManager
        )

        fun signUpName(textFieldManager: TextFieldManager?) = TextFieldAttr(
            labelId = R.string.name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            textFieldManager = textFieldManager
        )

        fun signUpPasswordConfirm(textFieldManager: TextFieldManager?) =
            signInPassword(null).copy(
                labelId = R.string.password_confirm,
                textFieldManager = textFieldManager
            )

        fun signUpPassword(textFieldManager: TextFieldManager?) =
            signInPassword(null).copy(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                textFieldManager = textFieldManager
            )

        fun resetPasswordEmail(textFieldManager: TextFieldManager?) =
            signInEmail(null).copy(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                textFieldManager = textFieldManager
            )
    }

    class TextFieldManager {
        private val attrs by lazy { mutableListOf<TextFieldAttr>() }

        val anyFieldEmpty get() = attrs.any { it.isEmpty }
        val anyFieldErrorEnable get() = attrs.any { it.isErrorEnable }

        fun addAttr(attr: TextFieldAttr) = attrs.add(attr)
    }
}