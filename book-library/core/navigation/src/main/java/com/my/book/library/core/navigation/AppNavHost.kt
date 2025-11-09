package com.my.book.library.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.featrue.splash.SplashScreen
import com.my.book.library.feature.main.ui.MainScreen

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    commonMainViewModel: CommonMainViewModel,
    modifier: Modifier
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.Splash.name,
        modifier = modifier,
    ) {

        // Splash
        composable(
            route = Screen.Splash.name,
            content = {
                SplashScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToMain = {
                        navHostController.navigate(route = Screen.Main.name)
                    },
                    modifier = modifier,
                )
            }
        )

        // Main
        composable(
            route = Screen.Main.name,
            content = {
                MainScreen(
                    commonMainViewModel = commonMainViewModel,
                    modifier = modifier
                )
            }
        )
    }

}