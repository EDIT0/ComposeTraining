package com.example.movieappdemo1.presentation.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.navigation.AppNavigationScreen
import com.example.movieappdemo1.presentation.ui.navigation.MainBottomNavigation
import com.example.movieappdemo1.presentation.util.ConvertUtil
import com.example.movieappdemo1.ui.theme.DeepBlue
import com.example.movieappdemo1.ui.theme.LightGray
import com.example.movieappdemo1.ui.theme.White
import com.google.gson.Gson

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val homeScreenViewModel = HomeScreenViewModel()
    HomeScreen(navController, homeScreenViewModel, "")
}

lateinit var mHomeScreenViewModel: HomeScreenViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
    intro: String?
) {
    val navHostController = rememberNavController()
    mHomeScreenViewModel = homeScreenViewModel

    LogUtil.d_dev("HomeScreen ${intro}")

    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navHostController)
        }
    ) {
        Box(Modifier.padding(it)) {
            MainBottomNavigation(navHostController, navController)
        }
    }
    IsLoading(homeScreenViewModel.isLoading.value)
}

@Composable
fun BottomNavigation(
    navController: NavController
) {
    BottomNavigation(
        backgroundColor = White,
        elevation = 10.dp
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination
        val currentRoute = currentDestination?.route

        val items = listOf<BottomNavItem>(
            BottomNavItem.MovieList,
            BottomNavItem.SearchMovie,
            BottomNavItem.SavedMovie
        )

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.title),
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = { Text(stringResource(id = item.title), fontSize = 9.sp) },
                selectedContentColor = DeepBlue,
                unselectedContentColor = LightGray,
                selected = currentRoute == item.screenRoute,
                alwaysShowLabel = true,
                onClick = {
//                    navController.navigate(item.screenRoute) {
//                        navController.graph.startDestinationRoute?.let {
//                            popUpTo(it) { saveState = true }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
                    navController.navigate(item.screenRoute) {
                        launchSingleTop = true
                        restoreState = true
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                                inclusive = false
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun IsLoading(isLoading: Boolean) {
    if(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x80000000))
                .clickable {

                },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

fun setLoading(isLoading: Boolean) {
    mHomeScreenViewModel.isLoading.value = isLoading
}

fun moveToMovieInfo(navController: NavController, movieModelResult: MovieModelResult) {
    val gson = Gson()
    val movieModelResultJson: String = gson.toJson(movieModelResult)
    val encodedJson = ConvertUtil.urlEncode(movieModelResultJson)
    LogUtil.i_dev("encodedJson: ${encodedJson}")
    navController.navigate(route = AppNavigationScreen.MovieInfoScreen.name + "/${encodedJson}")
}