package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.base.BackHandler
import com.leebeebeom.clothinghelper.base.SimpleToast
import com.leebeebeom.clothinghelper.main.base.BaseIsAllExpandState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
제스쳐로 드로어 열리는 지 확인

헤더에 유저 이름, 이메일 표시 확인

최초 구동 시 로딩 확인

설정 아이콘 클릭 시 리플 확인, 세팅 스크린으로 이동 확인
드로어 아이템 클릭 시 드로어 닫히는 지 확인

에센셜 메뉴 클릭 시 이동 확인 (메인 메뉴 빼고 아직 미구현)

메인 카테고리 클릭 시 이동 확인
서브 카테고리 클릭 시 이동 확인(미구현)

메인 카테고리 익스팬드 아이콘 로테이션 및 동작 확인

드로어가 열려있을때 뒤로 가기 시 드로어 닫히는지 확인
 */

@Composable
fun MainRoot(
    onSettingIconClick: () -> Unit,
    onEssentialMenuClick: (essentialMenu: EssentialMenus) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    viewModel: MainRootViewModel = hiltViewModel(),
    uiStates: BaseIsAllExpandState = viewModel.uiStates,
    state: MainRootState = rememberMainRootState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerClose = remember {
        {
            coroutineScope.launch { state.onDrawerClose() }
            Unit
        }
    }

    ClothingHelperTheme {
        Scaffold(
            scaffoldState = state.scaffoldState,
            drawerContent = {
                DrawerContents(
                    user = { uiStates.user },
                    isLoading = { uiStates.isLoading },
                    isAllExpand = { uiStates.isAllExpand },
                    subCategories = uiStates::getSubCategories,
                    subCategoriesSize = uiStates::getSubCategoriesSize,
                    onEssentialMenuClick = {
                        onEssentialMenuClick(it)
                        drawerClose()
                    },
                    onMainCategoryClick = {
                        onMainCategoryClick(it)
                        drawerClose()
                    },
                    onSubCategoryClick = {
                        onSubCategoryClick(it)
                        drawerClose()
                    },
                    onSettingIconClick = {
                        onSettingIconClick()
                        drawerClose()
                    },
                    allExpandIconClick = viewModel::toggleAllExpand,
                    subCategoryNames = uiStates::getSubCategoryNames,
                    onAddSubCategoryPositiveButtonClick = viewModel::addSubCategory,
                    onEditSUbCategoryNamePositiveClick = viewModel::editSubCategoryName
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
        ) {
            content(it)
            BackHandler(enabled = { state.drawerState.isOpen }, task = drawerClose)
        }
        SimpleToast(text = { uiStates.toastText }, shownToast = uiStates::toastShown)
    }
}

data class MainRootState(
    val scaffoldState: ScaffoldState,
    val drawerState: DrawerState = scaffoldState.drawerState
) {
    suspend fun onDrawerClose() {
        drawerState.close()
    }
}

@Composable
fun rememberMainRootState(scaffoldState: ScaffoldState = rememberScaffoldState()): MainRootState {
    return remember { MainRootState(scaffoldState = scaffoldState) }
}