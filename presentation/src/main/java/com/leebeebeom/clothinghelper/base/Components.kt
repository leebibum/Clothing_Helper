package com.leebeebeom.clothinghelper.base

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.theme.Disabled

@Composable
fun SimpleIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) = Icon(
    modifier = modifier,
    painter = painterResource(id = drawable),
    contentDescription = null,
    tint = tint
)

@Composable
fun CenterDotProgressIndicator(backGroundColor: Color = Disabled) {
    Surface(color = backGroundColor) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) { }) {
            DotProgressIndicator(
                modifier = Modifier.align(Alignment.Center), dotSize = 8.dp,
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        }
    }
}

@Composable
fun SimpleHeightSpacer(dp: Int) = Spacer(modifier = Modifier.height(dp.dp))

@Composable
fun SimpleWidthSpacer(dp: Int) = Spacer(modifier = Modifier.width(dp.dp))

@Composable
fun SimpleToast(@StringRes text: Int?, shownToast: () -> Unit) {
    text?.let {
        Toast.makeText(LocalContext.current, stringResource(id = text), Toast.LENGTH_SHORT).show()
        shownToast()
    }
}