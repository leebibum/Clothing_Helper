package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.base.composables.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.base.composables.SimpleToast
import com.leebeebeom.clothinghelper.main.base.composables.SelectModeBackHandler
import com.leebeebeom.clothinghelper.main.base.composables.selectmodebottomappbar.SelectModeBottomAppBar
import com.leebeebeom.clothinghelper.main.base.dialogs.EditSubCategoryDialog
import com.leebeebeom.clothinghelper.main.subcategory.content.SubCategoryContent
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

/*
최초 구동 시 로딩 확인
타이틀 확인

올 익스팬드 아이콘 동작 확인
올 익스팬드 혹은 올 폴드 시 카드 리컴포즈돼도 유지되는 지 확인
화면 회전 시 유지 확인

소트 아이콘 동작 화인 (미구현)

익스팬드 아이콘 동작 확인
화면 밖으로 사라져도 이전 상태 유지되는지 확인

인포 텍스트 확인

애드 카테고리 다이얼로그 동작 확인 TODO 컬러지정
화면 방향 혹은 다크모드 변경 시 커서 맨 뒤에 있는지 확인
이미 존재하는 카테고리 명일시 "이미 존재하는 카테고리 입니다." 에러 표시
텍스트 변경 시 에러 숨김
카테고리 추가 시 올 익스팬드 상태에 따라 익스팬드 상태로 추가
카테고리 추가 시 정렬 지키는지 확인
트림 확인

카드 롱 클릭 시 선택모드 활성화 확인
체크 박스 애니메이션 확인
바텀 앱바 애니메이션 확인
하나 초과 혹은 미만 선택 시 이름 수정 애니메이션 확인
하나 미만 선택 시 삭제 애니메이션 확인
롱클릭 된 카드는 셀렉트 되어야 함
선택모드 종료 시 셀렉티드 카테고리 초기화 되어야 함

선택 갯수에 따라 바텀 앱바에 선택된 갯수 표시
전체 선택 시 바텀앱바 체크박스 체크
전체 선택에서 하나라도 선택해제시 체크박스 체크 해제
바텀 앱바 체크박스 클릭 시 전체선택 토글

이름 수정 다이얼로그 동작 확인
트림 확인
본래 이름일 시 에러 표시 x 확인 버튼만 disable
이름 변경 시 정렬 지키는 지 확인
 */


@Composable
fun SubCategoryScreen(
    parent: SubCategoryParent,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    uiState: SubCategoryScreenUIState = viewModel.getUiState(parent),
    state: SubCategoryState = rememberSubCategoryState(),
    onSubCategoryClick: (StableSubCategory) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SubCategoryContent(
            parent = parent,
            subCategories = { uiState.items },
            sort = { uiState.sort },
            onLongClick = uiState::selectModeOn,
            onSubCategoryClick = onSubCategoryClick,
            onSortClick = viewModel::changeSort,
            onOrderClick = viewModel::changeOrder,
            selectedSubCategoryKey = { uiState.selectedKeys },
            isSelectMode = { uiState.isSelectMode },
            onSelect = uiState::onSelect,
            folders = uiState::getFolders
        )

        EditSubCategoryDialog(
            subCategories = { uiState.items },
            onPositiveButtonClick = viewModel::editSubCategoryName,
            onDismiss = state::dismissEditDialog,
            show = { state.showEditDialog },
            selectedSubCategory = { uiState.firstSelectedItem }
        )

        SelectModeBottomAppBar(
            selectedSize = { uiState.selectedSize },
            isAllSelected = { uiState.isAllSelected },
            onAllSelectCheckBoxClick = uiState::toggleAllSelect,
            onEditIconClick = state::showEditDialog,
            isSelectMode = { uiState.isSelectMode },
            showEditIcon = { uiState.showEditIcon },
            showDeleteIcon = { uiState.showDeleteIcon }
        )

        CenterDotProgressIndicator(
            backGround = MaterialTheme.colors.background,
            show = { uiState.isLoading })

        val addSubCategory = remember<(String) -> Unit> { { viewModel.addSubCategory(it, parent) } }
        SubCategoryFab(
            onPositiveButtonClick = addSubCategory,
            subCategories = { uiState.items },
            isSelectMode = { uiState.isSelectMode }
        )
    }

    SimpleToast(text = { uiState.toastText }, toastShown = uiState::toastShown)

    SelectModeBackHandler(
        isSelectMode = { uiState.isSelectMode },
        selectModeOff = uiState::selectModeOff
    )
}

class SubCategoryState(initialShowDialog: Boolean = false) {
    var showEditDialog by mutableStateOf(initialShowDialog)
        private set

    fun showEditDialog() {
        showEditDialog = true
    }

    fun dismissEditDialog() {
        showEditDialog = false
    }

    companion object {
        val saver: Saver<SubCategoryState, *> = listSaver(
            save = { listOf(it.showEditDialog) },
            restore = { SubCategoryState(it[0]) }
        )
    }
}

@Composable
fun rememberSubCategoryState() =
    rememberSaveable(saver = SubCategoryState.saver) { SubCategoryState() }