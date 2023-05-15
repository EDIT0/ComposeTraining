package com.edit.composenavdemo1

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun DemoAppNavHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = "home_screen/{value1}/{value2}",
        builder = {
            composable(
                route = "home_screen/{value1}/{value2}",
                content = {
                    HomeScreen(
                        onNavigateToSecondScreen = {
                            navController.navigate(
                                route = "second_screen/${it}"
                            )
                        },
                        dataValue1 = if(it.arguments?.getString("value1") == null) "" else it.arguments?.getString("value1").toString(),
                        dataValue2 = if(it.arguments?.getString("value2") == null) "" else it.arguments?.getString("value2").toString()
                    )
                }
            )

            composable(
                route = "second_screen/{inputName}",
                arguments = listOf(
                    navArgument(
                        name = "inputName",
                        builder = {
                            type = NavType.StringType
                        }
                    )
                ),
                content = {
                    SecondScreen(
                        textToDisplay = it.arguments?.getString("inputName").toString(),
                        onNavigateToBackStackScreen = {
//                            val bundle = bundleOf("key" to "value1")
//                            navController.popBackStack(
//                                route = "home_screen",
//                                inclusive = false
//                            )
                            val value1 = "value1"
                            Log.i("MYTAG", "return ${value1} ${it}")
                            navController.navigate("home_screen/${value1}/${it}") {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
//                                    saveState = true
                                }
                            }
//                            navController.popBackStack(navController.graph.id, true)
                        }
                    )
                }
            )
        }
    )

}