package com.my.book.library.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.compose.composable
import com.my.book.library.feature.search_library.ui.SearchLibraryScreen
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.featrue.splash.intro.ui.SplashScreen
import com.my.book.library.feature.main.ui.MainScreen
import com.my.book.library.feature.select_library.detail_region.ui.SelectLibraryDetailRegionScreen
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
                    onMoveToDetailRegion = { region: LibraryData.Region ->
                        navHostController.currentBackStackEntry?.savedStateHandle?.set(Data.Region.name, region)
                        navHostController.navigate(route = Screen.SelectLibraryDetailRegion.name)
                    },
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        )

        // SelectLibraryDetailRegion
        composable(
            route = Screen.SelectLibraryDetailRegion.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                var region by remember {
                    mutableStateOf<LibraryData.Region?>(null)
                }

                // 한 번만 실행되도록 LaunchedEffect 사용
                LaunchedEffect(Unit) {
                    region = navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<LibraryData.Region>(Data.Region.name)

                    LogUtil.d_dev("가져온 region: $region")

                    navHostController.previousBackStackEntry?.savedStateHandle?.remove<LibraryData.Region>(Data.Region.name)

                    if(region == null) {
                        navHostController.popBackStack()
                    }
                }

                // region이 null이 아닐 때만 화면 표시
                region?.let { nonNullRegion ->
                    SelectLibraryDetailRegionScreen(
                        commonMainViewModel = commonMainViewModel,
                        onMoveToLibrary = {

                        },
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        modifier = modifier,
                        region = nonNullRegion
                    )
                }
            }
        )
    }

}