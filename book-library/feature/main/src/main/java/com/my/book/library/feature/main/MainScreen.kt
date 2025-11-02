package com.my.book.library.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel

@Composable
fun MainScreen(
    commonMainViewModel: ViewModel,
    modifier: Modifier
) {

    val commonMainViewModel = commonMainViewModel
    val mainViewModel = hiltViewModel<MainViewModel>()

    MainContent(
        modifier = Modifier
    )
}

@Composable
fun MainContent(
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text("Main")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainUIPreview() {
    MainContent(
        modifier = Modifier
    )
}