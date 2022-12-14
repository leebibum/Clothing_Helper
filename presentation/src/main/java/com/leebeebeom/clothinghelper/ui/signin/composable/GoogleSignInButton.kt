package com.leebeebeom.clothinghelper.ui.signin.composable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.MaxWidthButton

@Composable
fun GoogleSignInButton(
    enabled: () -> Boolean,
    onActivityResult: (ActivityResult) -> Unit,
    onClick: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = onActivityResult
    )

    val intent = GoogleSignIn.getClient(LocalContext.current, gso()).signInIntent

    MaxWidthButton(
        text = R.string.starts_with_google_email,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        icon = { GoogleIcon() },
        onClick = {
            onClick()
            launcher.launch(intent)
        }
    )
}

@Composable
private fun gso(): GoogleSignInOptions {
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(id = R.string.default_web_client_id)).requestEmail().build()
}

@Composable
fun GoogleIcon() {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_icon),
        contentDescription = null
    )
}