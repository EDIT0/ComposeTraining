package com.my.book.library.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.compose.composable
import com.my.book.library.feature.search_library.ui.SearchLibraryScreen
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.featrue.splash.intro.ui.SplashScreen
import com.my.book.library.feature.main.ui.MainScreen
import com.my.book.library.feature.select_library.region.ui.SelectLibraryRegionScreen

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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                SplashScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToMain = {
                        navHostController.popBackStack()
                        navHostController.navigate(route = Screen.Main.name)
                    },
                    onMoveToSelectLibraryRegion = {
                        navHostController.popBackStack()
                        navHostController.navigate(route = Screen.SelectLibraryRegion.name)
                    },
                    modifier = modifier,
                )
            }
        )

        // Main
        composable(
            route = Screen.Main.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                MainScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToSearchLibrary = {
                        navHostController.navigate(route = Screen.SearchLibrary.name)
                    },
                    modifier = modifier
                )
            }
        )

        // SearchLibrary
        composable(
            route = Screen.SearchLibrary.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                SearchLibraryScreen(
                    commonMainViewModel = commonMainViewModel,
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        )

        // SelectLibraryRegion
        composable(
            route = Screen.SelectLibraryRegion.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                SelectLibraryRegionScreen(
                    commonMainViewModel = commonMainViewModel,
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        )
    }

}