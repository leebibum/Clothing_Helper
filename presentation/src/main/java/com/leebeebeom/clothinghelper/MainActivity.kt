package com.leebeebeom.clothinghelper

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.Anime.screenSlideInBottom
import com.leebeebeom.clothinghelper.base.Anime.screenSlideOutBottom
import com.leebeebeom.clothinghelper.main.base.MainNavHost
import com.leebeebeom.clothinghelper.signin.SignInNavHost
import com.leebeebeom.clothinghelperdomain.usecase.user.GetSignInStateUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "TAG"

@HiltAndroidApp
class ClothingHelper : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }
}

@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel = hiltViewModel()) {
    Box {
        MainNavHost()
        AnimatedVisibility(
            visible = !viewModel.isSignIn,
            enter = screenSlideInBottom, // TODO 작동 안함
            exit = screenSlideOutBottom
        ) {
            SignInNavHost()
        }
    }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(getSignInStateUseCase: GetSignInStateUseCase) :
    ViewModel() {
    var isSignIn by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            getSignInStateUseCase().collect { isSignIn = it }
        }
    }
}