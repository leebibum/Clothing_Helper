package com.leebeebeom.clothinghelper.signin.base.textfields

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

open class TextFieldState(initialText: String) {
    var textFieldValue by mutableStateOf(TextFieldValue(initialText))
        protected set

    open fun onValueChange(
        newTextFieldValue: TextFieldValue,
        onValueChange: (String) -> Unit,
        updateError: (Int?) -> Unit
    ) {
        val text = newTextFieldValue.text.trim()
        if (textFieldValue.text != text) updateError(null)
        textFieldValue = newTextFieldValue.copy(text)
        onValueChange(text)
    }

    fun onFocusChange(focusState: FocusState) {
        if (focusState.hasFocus)
            textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
    }

    fun onCancelIconClick(onValueChange: (String) -> Unit) {
        textFieldValue = TextFieldValue("")
        onValueChange("")
    }
}