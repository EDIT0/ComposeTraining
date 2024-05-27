package com.example.movieappdemo1.presentation.ui.screen.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.presentation.ui.navigation.AppNavigationScreen
import com.example.movieappdemo1.ui.theme.DeepBlue
import com.example.movieappdemo1.ui.theme.LightGray

@Preview
@Composable
fun IntroScreenPreview() {
    val navController = rememberNavController()
    val introScreenViewModel = IntroScreenViewModel()
    IntroScreen(navController = navController, introScreenViewModel = introScreenViewModel)
}

@Composable
fun IntroScreen(
    navController: NavController,
    introScreenViewModel: IntroScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = LightGray
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Intro",
            fontSize = 30.sp,
            color = DeepBlue
        )
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = "version")
        Spacer(modifier = modifier.height(30.dp))
    }

    moveToMain(navController = navController, introScreenViewModel = introScreenViewModel)

}

fun moveToMain(
    navController: NavController,
    introScreenViewModel: IntroScreenViewModel
) {
    if(introScreenViewModel.timerFinish.value) {
        LogUtil.d_dev("IntroScreen True")
        navController.navigate(route = AppNavigationScreen.HomeScreen.name + "/${introScreenViewModel.introData}") {
            popUpTo(AppNavigationScreen.IntroScreen.name) {
                inclusive = true
            }
        }
        introScreenViewModel.setTimerFinish(false)
    } else {
        LogUtil.d_dev("IntroScreen False")
    }
}