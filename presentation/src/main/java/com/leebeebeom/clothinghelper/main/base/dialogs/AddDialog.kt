package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.BaseTextFieldDialogState
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.TextFieldDialog
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    items: () -> ImmutableList<BaseModel>,
    @StringRes existNameError: Int,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    if (show()) {
        val names = remember { items().map { it.name } }
        val state = rememberSaveable(saver = AddDialogState.Saver) { AddDialogState() }

        TextFieldDialog(label = label,
            placeHolder = placeHolder,
            title = title,
            error = { state.error },
            textFieldValue = { state.textFieldValue },
            positiveButtonEnabled = { state.positiveButtonEnabled },
            onValueChange = {
                state.onValueChange(it)
                if (names.contains(it.text.trim())) state.updateError(existNameError)
            },
            onFocusChanged = state::onFocusChange,
            onPositiveButtonClick = { onPositiveButtonClick(state.text.trim()) },
            onDismiss = onDismiss
        )
    }

}

class AddDialogState(
    initialText: String = "", initialError: Int? = null
) : BaseTextFieldDialogState(initialText, initialError) {
    companion object {
        val Saver: Saver<AddDialogState, *> = listSaver(save = { listOf(it.text, it.error) },
            restore = { AddDialogState(it[0] as String, it[1] as? Int) })
    }
}