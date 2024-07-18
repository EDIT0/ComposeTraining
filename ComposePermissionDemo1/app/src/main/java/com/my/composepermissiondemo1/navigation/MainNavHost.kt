package com.my.composepermissiondemo1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.my.composepermissiondemo1.screen.OneScreen
import com.my.composepermissiondemo1.screen.TwoScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    startDestination: String = NavigationScreenName.OneScreen.name
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(
            route = NavigationScreenName.OneScreen.name
        ) {
            OneScreen(
                navController = navHostController
            )
        }

        composable(
            route = NavigationScreenName.TwoScreen.name
        ) {
            TwoScreen(
                navController = navHostController
            )
        }
    }
}