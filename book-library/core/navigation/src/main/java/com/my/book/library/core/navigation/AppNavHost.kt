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
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.feature.splash.intro.ui.SplashScreen
import com.my.book.library.feature.main.ui.MainScreen
import com.my.book.library.feature.search.book.ui.SearchScreen
import com.my.book.library.feature.search.library.ui.LibraryMapScreen
import com.my.book.library.feature.select_library.detail_region.ui.SelectLibraryDetailRegionScreen
import com.my.book.library.feature.select_library.library.ui.SelectLibraryListScreen
import com.my.book.library.feature.select_library.library_detail.ui.SelectLibraryListDetailScreen
import com.my.book.library.feature.select_library.region.ui.SelectLibraryRegionScreen
import com.my.book.library.feature.select_region.complete.ui.RegionSelectionCompleteScreen
import com.my.book.library.feature.select_region.guide.ui.RegionSelectGuideScreen
import com.my.book.library.feature.select_region.selection.ui.RegionSelectionScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    commonViewModel: CommonViewModel,
    onAppOff: () -> Unit,
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
                    commonViewModel = commonViewModel,
                    onMoveToMain = {
                        onlyPopBackStack(navHostController = navHostController)
                        navHostController.navigate(route = Screen.Main.name)
                    },
                    onMoveToSelectLibraryRegion = {
                        onlyPopBackStack(navHostController = navHostController)
                        navHostController.navigate(route = Screen.RegionSelectGuide.name)
                    },
                    modifier = modifier,
                )
            }
        )

        // Region Select Guide
        composable(
            route = Screen.RegionSelectGuide.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                RegionSelectGuideScreen(
                    commonViewModel = commonViewModel,
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    },
                    onMoveToRegionSelection = {
                        navHostController.navigate(route = Screen.RegionSelection.name)
                    },
                    modifier = modifier
                )
            }
        )

        // Region Selection
        composable(
            route = Screen.RegionSelection.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                RegionSelectionScreen(
                    commonViewModel = commonViewModel,
                    onBackPressed = {
                        onlyPopBackStack(navHostController = navHostController)
                    },
                    onMoveToRegionSelectionComplete = { detailRegion ->
                        val detailRegionString = URLEncoder.encode(
                            Gson().toJson(detailRegion),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.RegionSelectionComplete.name + "/${detailRegionString}")
                    },
                    modifier = modifier
                )
            }
        )

        // Region Selection Complete
        composable(
            route = Screen.RegionSelectionComplete.name + "/{${Data.DetailRegion.name}}",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                val detailRegionString = it.arguments?.getString(Data.DetailRegion.name)
                val detailRegion = Gson().fromJson(
                    URLDecoder.decode(
                        detailRegionString,
                        StandardCharsets.UTF_8.name()),
                    LibraryData.DetailRegion::class.java
                )

                if(detailRegion == null) {
                    onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    return@composable
                }

                RegionSelectionCompleteScreen(
                    commonViewModel = commonViewModel,
                    onBackPressed = {
                        onlyPopBackStack(navHostController = navHostController)
                    },
                    onMoveToMain = {
                        navHostController.navigate(route = Screen.Main.name) {
                            popUpTo(Screen.RegionSelectGuide.name) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = modifier,
                    detailRegion = detailRegion,
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
                    commonViewModel = commonViewModel,
                    onMoveToSearchLibrary = {
                        navHostController.navigate(route = Screen.Search.name)
                    },
                    modifier = modifier
                )
            }
        )

        // SearchScreen
        composable(
            route = Screen.Search.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                SearchScreen(
                    commonViewModel = commonViewModel,
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    },
                    onMoveToLibraryMap = { book: ResSearchBook.ResponseData.BookWrapper ->
                        val bookString = URLEncoder.encode(
                            Gson().toJson(book),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.LibraryMap.name + "/${bookString}")
                    },
                    modifier = modifier
                )
            }
        )

        // LibraryMapScreen
        composable(
            route = Screen.LibraryMap.name + "/{${Data.Book.name}}",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            content = {
                val bookString = it.arguments?.getString(Data.Book.name)
                val book = Gson().fromJson(URLDecoder.decode(bookString, StandardCharsets.UTF_8.name()), ResSearchBook.ResponseData.BookWrapper::class.java)

                if(book == null) {
                    onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    return@composable
                }

                LibraryMapScreen(
                    commonViewModel = commonViewModel,
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    },
                    modifier = modifier,
                    book = book
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
                    commonViewModel = commonViewModel,
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
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
                    commonViewModel = commonViewModel,
                    onMoveToDetailRegion = { region: LibraryData.Region ->
                        val regionString = URLEncoder.encode(
                            Gson().toJson(region),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.SelectLibraryDetailRegion.name + "/${regionString}")
                    },
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
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
                    onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    return@composable
                }

                SelectLibraryDetailRegionScreen(
                    commonViewModel = commonViewModel,
                    onMoveToLibrary = { detailRegion: LibraryData.DetailRegion ->
                        val detailRegionString = URLEncoder.encode(
                            Gson().toJson(detailRegion),
                            StandardCharsets.UTF_8.name()
                        )

                        navHostController.navigate(route = Screen.SelectLibraryList.name + "/${detailRegionString}")
                    },
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
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
                    onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    return@composable
                }

                SelectLibraryListScreen(
                    commonViewModel = commonViewModel,
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
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
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
                    onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
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
                    onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    return@composable
                }

                SelectLibraryListDetailScreen(
                    commonViewModel = commonViewModel,
                    onMoveToMain = {
                        // 모든 백스택 제거 후 메인으로 이동
//                        while (navHostController.popBackStack()) {
//                            // 모든 백스택을 비움
//                        }
                        navHostController.navigate(route = Screen.Main.name) {
                            popUpTo(Screen.SelectLibraryRegion.name) {
                                inclusive = true
                            }
                        }
                    },
                    onBackPressed = {
                        onBackPressed(navHostController = navHostController, onAppOff = onAppOff)
                    },
                    modifier = modifier,
                    detailRegion = detailRegion,
                    libraryInfo = libraryInfo
                )
            }
        )
    }
}

private fun onlyPopBackStack(navHostController: NavHostController) {
    navHostController.popBackStack()
}

private fun onBackPressed(navHostController: NavHostController, onAppOff: () -> Unit) {
    if (!navHostController.popBackStack()) {
        onAppOff.invoke()
    }
}