package com.my.composepermissiondemo1.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.composepermissiondemo1.util.LogUtil

@Composable
fun TwoScreen(
    navController: NavController,
    twoViewModel: TwoViewModel = hiltViewModel()
) {

    LogUtil.d_dev("Start TwoScreen")

}