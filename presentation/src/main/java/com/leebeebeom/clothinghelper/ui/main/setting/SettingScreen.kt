package com.leebeebeom.clothinghelper.ui.main.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.MaxWidthButton

@Composable
fun SettingScreen(viewModel: SettingViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        SignOutButton(onSignOutClick = viewModel.signOut)
    }
}

@Composable
private fun SignOutButton(onSignOutClick: () -> Unit) {
    MaxWidthButton(
        text = R.string.sign_out,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
        onClick = onSignOutClick
    )
}