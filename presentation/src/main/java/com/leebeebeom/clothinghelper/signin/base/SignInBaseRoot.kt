package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.base.SimpleToast

@Composable
fun SignInBaseRoot(
    isLoading: Boolean,
    @StringRes toastText: Int?,
    toastShown: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val focusManager = LocalFocusManager.current

    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .clickable { focusManager.clearFocus() } // TODO 인터렉션 소스 삭제 동작 확인
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }

    toastText?.let { SimpleToast(text = it, shownToast = toastShown) }

    if (isLoading) CenterDotProgressIndicator()
}