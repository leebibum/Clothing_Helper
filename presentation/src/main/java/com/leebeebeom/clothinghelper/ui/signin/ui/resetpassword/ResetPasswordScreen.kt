package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.MaxWidthButton
import com.leebeebeom.clothinghelper.composable.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.composable.SimpleToast
import com.leebeebeom.clothinghelper.ui.signin.composable.textfield.EmailTextField

/*
이메일 필드가 비어있거나 에러 메세지 표시 중일 경우 확인 버튼 비활성화
에러가 표시중인 경우 텍스트가 변경되면 에러 숨김

확인 버튼 클릭 시 로딩화면 출력, 끝나면 로딩화면 숨김

이메일 형식이 올바르지 않은 경우 "이메일 형식이 올바르지 않아요" 에러 표시
이메일을 찾을 수 없는 경우 "이메일을 찾을 수 없어요" 에러 표시

정상적으로 이메일이 발송된 경우 "이메일이 전송되었습니다" 토스트 출력
로그인 화면으로 이동

 */

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    uiState: ResetPasswordUIState = viewModel.uiState
) {
    TaskSuccess(
        taskSuccess = { uiState.isTaskSuccess },
        consumeIsTaskSuccess = uiState::consumeIsTaskSuccess
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.reset_password_text),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        EmailTextField(
            initialEmail = { uiState.email },
            error = { uiState.emailError },
            updateError = uiState::updateEmailError,
            onEmailChange = uiState::onEmailChange
        )

        SimpleHeightSpacer(dp = 12)
        MaxWidthButton(
            text = R.string.send,
            enabled = { uiState.buttonEnabled },
            onClick = viewModel::sendResetPasswordEmail
        )
    }

    SimpleToast(text = { uiState.toastText }, toastShown = uiState::toastShown)
}

@Composable
private fun TaskSuccess(taskSuccess: () -> Boolean, consumeIsTaskSuccess: () -> Unit) {
    if (taskSuccess()) {
        (LocalContext.current as ComponentActivity).onBackPressedDispatcher.onBackPressed()
        consumeIsTaskSuccess()
    }
}