package com.my.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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