package com.my.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IsLoading(
    isLoading: Boolean
) {
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

@Composable
fun IsLoadMoreLoading(
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0x80ffffff))
            .clickable {

            },
        contentAlignment = Alignment.Center
    ) {
        if(isLoading) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIsLoading() {
    IsLoading(isLoading = true)
}

@Preview(showBackground = true)
@Composable
fun PreviewIsLoadMoreLoading() {
    IsLoadMoreLoading(isLoading = true)
}