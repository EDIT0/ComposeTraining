package com.edit.composenavdemo1

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SecondScreen(
    onNavigateToBackStackScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    textToDisplay: String = ""
) {
    Column(
        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textToDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            color = Color.DarkGray
        )

        Button(
            onClick = {
                Log.i("MYTAG", "Button onClick ${textToDisplay}")
                onNavigateToBackStackScreen(textToDisplay)
            }
        ) {

        }
    }
}
