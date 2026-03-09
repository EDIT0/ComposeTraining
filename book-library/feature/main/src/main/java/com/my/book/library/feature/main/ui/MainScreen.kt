package com.my.book.library.feature.main.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.feature.main.ui.home.HomeScreen
import com.my.book.library.feature.main.ui.save.SaveScreen
import com.my.book.library.feature.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    commonViewModel: CommonViewModel,
    onMoveToSearchLibrary: () -> Unit,
    modifier: Modifier
) {

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val mainViewModel = hiltViewModel<MainViewModel>()

    MainContent(
        localContext = localContext,
        onMoveToSearchLibrary = onMoveToSearchLibrary,
        modifier = modifier,
        commonViewModel = commonViewModel,
        mainViewModel = mainViewModel
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "MainScreen",
        lifecycleResult = object : LifecycleResult {
            override fun onEnter() {

            }
            override fun onStart() {

            }
            override fun onResume() {

            }
            override fun onPause() {

            }
            override fun onStop() {

            }
            override fun onDispose() {

            }
        }
    )
}

@Composable
fun MainContent(
    localContext: Context,
    onMoveToSearchLibrary: () -> Unit,
    modifier: Modifier,
    commonViewModel: CommonViewModel,
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                MainDestination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == destination.route
                        } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = stringResource(destination.contentDescriptionResId)
                            )
                        },
                        label = {
                            Text(stringResource(destination.labelResId))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = MainDestination.HOME.route,
        ) {
            composable(MainDestination.HOME.route) {
                HomeScreen(
                    onMoveToSearchLibrary = onMoveToSearchLibrary,
                    commonViewModel = commonViewModel,
                    mainViewModel = mainViewModel
                )
            }
            composable(MainDestination.SAVE.route) {
                SaveScreen(
                    commonViewModel = commonViewModel,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainUIPreview() {
//    MainContent(
//        localContext = LocalContext.current,
//        onMoveToSearchLibrary = {},
//        modifier = Modifier
//    )
//}