package com.my.composebottomsheetdemo1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.my.composebottomsheetdemo1.screen.FirstBottomSheetScreen
import com.my.composebottomsheetdemo1.screen.HomeScreen
import com.my.composebottomsheetdemo1.screen.SecondBottomSheetScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    startDestination: String = NavigationScreenName.HomeScreen.name
) {

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(
            route = NavigationScreenName.HomeScreen.name
        ) {
            HomeScreen(navController = navHostController)
        }

        composable(
            route = NavigationScreenName.FirstBottomSheetScreen.name
        ) {
            FirstBottomSheetScreen(navController = navHostController)
        }

        composable(
            route = NavigationScreenName.SecondBottomSheetScreen.name
        ) {
            SecondBottomSheetScreen(navController = navHostController)
        }

    }
}