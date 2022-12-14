package com.leebeebeom.clothinghelper.composable

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.theme.*
import com.leebeebeom.clothinghelper.util.Anime
import com.leebeebeom.clothinghelper.util.Anime.Error.errorIn
import com.leebeebeom.clothinghelper.util.Anime.Error.errorOut
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    modifier: Modifier = Modifier,
    state: MaxWidthTextFieldState,
    textFieldValue: () -> TextFieldValue,
    error: () -> Int?,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    isVisible: () -> Boolean = { true },
    cancelIconEnabled: Boolean = true,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        MaxWidthTextField(
            state = state,
            onFocusChanged = onFocusChanged,
            textFieldValue = textFieldValue,
            onValueChange = onValueChange,
            error = error,
            isVisible = isVisible,
            trailingIcon = trailingIcon,
            cancelIconEnabled = cancelIconEnabled,
            enabled = enabled
        )
        ErrorText(error)
    }

    ShowKeyboard(showKeyboardEnabled = state.showKeyboard, showKeyboard = state::showKeyboard)
}

@Composable
private fun MaxWidthTextField(
    state: MaxWidthTextFieldState,
    textFieldValue: () -> TextFieldValue,
    error: () -> Int?,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    isVisible: () -> Boolean,
    trailingIcon: @Composable (() -> Unit)?,
    cancelIconEnabled: Boolean,
    enabled: Boolean
) {
    val focusManager = LocalFocusManager.current
    var hasFocus by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(state.focusRequester)
            .onFocusChanged {
                hasFocus = it.hasFocus
                onFocusChanged(it)
            },
        value = textFieldValue(),
        onValueChange = onValueChange,
        label = { SingleLineText(text = state.label) },
        placeholder = { SingleLineText(text = state.placeholder) },
        isError = error() != null,
        visualTransformation = if (isVisible()) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = state.keyboardOptions,
        trailingIcon = {
            trailingIcon?.run { invoke() } ?: CancelIcon(
                show = { cancelIconEnabled && textFieldValue().text.isNotEmpty() && hasFocus },
                onClick = { onValueChange(TextFieldValue()) }
            )
        },
        singleLine = true,
        keyboardActions =
        if (state.keyboardOptions.imeAction == ImeAction.Done)
            KeyboardActions(onDone = { focusManager.clearFocus() })
        else KeyboardActions.Default,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gainsboro,
            unfocusedLabelColor = DarkGrayishBlue,
            backgroundColor = LightGrayishBlue,
            placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        ),
        enabled = enabled
    )
}

@Composable
private fun ErrorText(@StringRes error: () -> Int?) {
    AnimatedVisibility(
        visible = error() != null, enter = errorIn, exit = errorOut
    ) {
        SingleLineText(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = error,
            style = errorTextStyle()
        )
    }
}

data class MaxWidthTextFieldState @OptIn(ExperimentalComposeUiApi::class) constructor(
    @StringRes val label: Int,
    @StringRes val placeholder: Int,
    val showKeyboard: Boolean,
    val keyboardOptions: KeyboardOptions,
    val focusRequester: FocusRequester,
    val keyboardController: SoftwareKeyboardController?
) {
    @OptIn(ExperimentalComposeUiApi::class)
    suspend fun showKeyboard() {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@NonRestartableComposable
fun rememberMaxWidthTextFieldState(
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    showKeyboard: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    focusRequester: FocusRequester = remember { FocusRequester() },
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) = remember {
    MaxWidthTextFieldState(
        label = label,
        placeholder = placeholder,
        showKeyboard = showKeyboard,
        keyboardOptions = keyboardOptions,
        focusRequester = focusRequester,
        keyboardController = keyboardController
    )
}

@Composable
private fun ShowKeyboard(showKeyboardEnabled: Boolean, showKeyboard: suspend () -> Unit) {
    var keyboardShown by rememberSaveable { mutableStateOf(false) }

    if (!keyboardShown && showKeyboardEnabled) {
        LaunchedEffect(key1 = Unit) { showKeyboard() }
        keyboardShown = true
    }
}

@Composable
private fun CancelIcon(show: () -> Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = show(),
        enter = Anime.CancelIcon.fadeIn,
        exit = Anime.CancelIcon.fadeOut,
    ) {
        CustomIconButton(
            onClick = onClick,
            drawable = R.drawable.ic_cancel,
            tint = VeryDarkGray,
            size = 20.dp
        )
    }
}