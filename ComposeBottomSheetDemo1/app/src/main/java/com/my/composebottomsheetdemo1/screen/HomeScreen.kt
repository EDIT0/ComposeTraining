package com.my.composebottomsheetdemo1.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.my.composebottomsheetdemo1.navigation.NavigationScreenName

@Composable
fun HomeScreen(
    navController: NavController
) {
    /**
     * BottomSheetScffold
     * ModalBottomSheet
     * */

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(route = NavigationScreenName.FirstBottomSheetScreen.name) }) {
            Text(text = "First")
        }
        Button(onClick = { navController.navigate(route = NavigationScreenName.SecondBottomSheetScreen.name)  }) {
            Text(text = "Second")
        }
    }

}