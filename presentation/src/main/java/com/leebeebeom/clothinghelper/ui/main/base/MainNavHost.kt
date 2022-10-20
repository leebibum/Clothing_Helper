package com.leebeebeom.clothinghelper.ui.main.base

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.ui.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.ui.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.BOTTOM
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.ETC
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.FAVORITE
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.MAIN_SCREEN
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.OUTER
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.SEE_ALL
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.TOP
import com.leebeebeom.clothinghelperdomain.model.BaseMenu.TRASH

object MainDestinations {
    const val MAIN_CATEGORY = "mainCategory"
    const val SUB_CATEGORY = "subCategory"
    const val SETTING = "setting"
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    MainScreenRoot(
        onSettingIconClick = { navController.mainNavigate(MainDestinations.SETTING) },
        onDrawerContentClick = { id -> navController.drawerNavigate(id) }
    ) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.MAIN_CATEGORY,
            modifier = Modifier.padding(it)
        ) {
            composable(MainDestinations.MAIN_CATEGORY) {
                MainCategoryScreen(
                    onMainCategoryClick = { navController.mainNavigate(MainDestinations.SUB_CATEGORY) })
            }
            composable(MainDestinations.SUB_CATEGORY) { SubCategoryScreen() }
            composable(MainDestinations.SETTING) { SettingScreen() }
        }
    }
}

fun NavController.mainNavigate(destination: String) =
    navigate(destination) {
        popUpTo(MainDestinations.MAIN_CATEGORY)
        launchSingleTop = true
    }

fun NavController.drawerNavigate(id: Int) {
    when (id) {
        MAIN_SCREEN -> mainNavigate(MainDestinations.MAIN_CATEGORY)
        FAVORITE -> {/*TODO*/
        }
        SEE_ALL -> { /*TODO*/
        }
        TRASH -> { /*TODO*/
        }
        TOP -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        BOTTOM -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        OUTER -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        ETC -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        else -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
    }
}