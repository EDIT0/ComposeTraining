package com.my.book.library.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.my.book.library.feature.search_library.ui.SearchLibraryScreen
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.featrue.splash.intro.ui.SplashScreen
import com.my.book.library.feature.main.ui.MainScreen
import com.my.book.library.feature.select_library.detail_region.ui.SelectLibraryDetailRegionScreen
import com.my.book.library.feature.select_library.library.ui.SelectLibraryListScreen
import com.my.book.library.feature.select_library.library_detail.ui.SelectLibraryListDetailScreen
import com.my.book.library.feature.select_library.region.ui.SelectLibraryRegionScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    commonMainViewModel: CommonMainViewModel,
    modifier: Modifier
) {

    val animSpeed = 700

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
            enterTransition = {
                // 새 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            exitTransition = {
                // 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popEnterTransition = {
                // 뒤로가기 시 이전 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popExitTransition = {
                // 뒤로가기 시 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            content = {
                SelectLibraryRegionScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToDetailRegion = { region: LibraryData.Region ->
                        val regionString = URLEncoder.encode(
                            Gson().toJson(region),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.SelectLibraryDetailRegion.name + "/${regionString}")
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
            route = Screen.SelectLibraryDetailRegion.name + "/{${Data.Region.name}}",
            enterTransition = {
                // 새 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            exitTransition = {
                // 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popEnterTransition = {
                // 뒤로가기 시 이전 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popExitTransition = {
                // 뒤로가기 시 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            content = {
                val regionString = it.arguments?.getString(Data.Region.name)
                val region = Gson().fromJson(URLDecoder.decode(regionString, StandardCharsets.UTF_8.name()), LibraryData.Region::class.java)

                if(region == null) {
                    navHostController.popBackStack()
                    return@composable
                }

                SelectLibraryDetailRegionScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToLibrary = { detailRegion: LibraryData.DetailRegion ->
                        val detailRegionString = URLEncoder.encode(
                            Gson().toJson(detailRegion),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.SelectLibraryList.name + "/${detailRegionString}")
                    },
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    modifier = modifier,
                    region = region
                )
            }
        )

        // SelectLibraryList
        composable(
            route = Screen.SelectLibraryList.name + "/{${Data.DetailRegion.name}}",
            enterTransition = {
                // 새 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            exitTransition = {
                // 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popEnterTransition = {
                // 뒤로가기 시 이전 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popExitTransition = {
                // 뒤로가기 시 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            content = {
                val detailRegionString = it.arguments?.getString(Data.DetailRegion.name)
                val detailRegion = Gson().fromJson(
                    URLDecoder.decode(
                        detailRegionString,
                        StandardCharsets.UTF_8.name()),
                    LibraryData.DetailRegion::class.java
                )

                if(detailRegion == null) {
                    navHostController.popBackStack()
                    return@composable
                }

                SelectLibraryListScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToLibraryDetail = { libraryInfo, detailRegion ->
                        val detailRegionString = URLEncoder.encode(
                            Gson().toJson(detailRegion),
                            StandardCharsets.UTF_8.name()
                        )

                        val libraryInfoString = URLEncoder.encode(
                            Gson().toJson(libraryInfo),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.SelectLibraryListDetail.name + "/${detailRegionString}" + "/${libraryInfoString}")
                    },
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    modifier = modifier,
                    detailRegion = detailRegion
                )
            }
        )

        // SelectLibraryListDetail
        composable(
            route = Screen.SelectLibraryListDetail.name + "/{${Data.DetailRegion.name}}" + "/{${Data.LibraryInfo.name}}",
            enterTransition = {
                // 새 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            exitTransition = {
                // 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popEnterTransition = {
                // 뒤로가기 시 이전 스크린이 들어올 때
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            popExitTransition = {
                // 뒤로가기 시 현재 스크린이 나갈 때
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = animSpeed)
                )
            },
            content = {
                val detailRegionString = it.arguments?.getString(Data.DetailRegion.name)
                val detailRegion = Gson().fromJson(
                    URLDecoder.decode(
                        detailRegionString,
                        StandardCharsets.UTF_8.name()),
                    LibraryData.DetailRegion::class.java
                )

                if(detailRegion == null) {
                    navHostController.popBackStack()
                    return@composable
                }

                val libraryInfoString = it.arguments?.getString(Data.LibraryInfo.name)
                val libraryInfo = Gson().fromJson(
                    URLDecoder.decode(
                        libraryInfoString,
                        StandardCharsets.UTF_8.name()),
                    ResSearchBookLibrary.ResponseData.LibraryWrapper::class.java
                )

                if(libraryInfo == null) {
                    navHostController.popBackStack()
                    return@composable
                }

                SelectLibraryListDetailScreen(
                    commonMainViewModel = commonMainViewModel,
                    onMoveToMain = {

                    },
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    modifier = modifier,
                    detailRegion = detailRegion,
                    libraryInfo = libraryInfo
                )
            }
        )
    }

}