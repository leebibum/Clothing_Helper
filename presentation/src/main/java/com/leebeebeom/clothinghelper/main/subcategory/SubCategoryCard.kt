package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubCategoryCard(
    subCategory: SubCategory,
    onLongClick: () -> Unit,
    isSelectMode: Boolean,
    onSelect: () -> Unit,
    allExpand: Boolean,
    isChecked: Boolean
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    isExpanded = allExpand

    Card(elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .combinedClickable(onClick = {
                    if (!isSelectMode) { /*TODO 이동*/
                    } else onSelect()
                }, onLongClick = {
                    onLongClick()
                    onSelect()
                })
        ) {
            SubCategoryTitle(
                title = subCategory.name,
                isExpanded = isExpanded,
                onExpandIconClick = { isExpanded = !isExpanded },
                isSelectMode = isSelectMode,
                isChecked = isChecked
            )
            SubCategoryInfo(isExpanded)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SubCategoryTitle(
    title: String,
    isExpanded: Boolean,
    onExpandIconClick: () -> Unit,
    isSelectMode: Boolean,
    isChecked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
            .animateContentSize(animationSpec = tween(350)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val scale by animateFloatAsState(
                targetValue = if (isSelectMode) 1f else 0f,
                animationSpec = tween(250)
            )
            val size by animateDpAsState(
                targetValue = if (isSelectMode) 18.dp else 0.dp,
                animationSpec = tween(250)
            )
            CircleCheckBox(
                isChecked = isChecked,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(size)
                    .scale(scale)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        ExpandIcon(
            isExpanded = isExpanded, onExpandIconClick = onExpandIconClick
        )
    }
}

@Composable
fun ExpandIcon(
    modifier: Modifier = Modifier, isExpanded: Boolean, onExpandIconClick: () -> Unit
) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    IconButton(onClick = onExpandIconClick) {
        SimpleIcon(
            drawable = R.drawable.ic_expand_more,
            modifier = modifier.rotate(rotate),
            tint = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    }
}

@Composable
private fun SubCategoryInfo(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(250)),
        exit = shrinkVertically(animationSpec = tween(250))
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val weightModifier = Modifier.weight(1f)

                SubCategoryInfoText( // TODO
                    modifier = weightModifier,
                    infoTitle = R.string.average_size,
                    info = R.string.top_info
                )

                SubCategoryInfoText(
                    modifier = weightModifier,
                    infoTitle = R.string.most_have_size,
                    info = R.string.top_info
                )
            }
        }
    }
}

@Composable
private fun SubCategoryInfoText(
    modifier: Modifier, @StringRes infoTitle: Int, @StringRes info: Int
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column(modifier = modifier.padding(start = 8.dp)) {
            Text(text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold)
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}