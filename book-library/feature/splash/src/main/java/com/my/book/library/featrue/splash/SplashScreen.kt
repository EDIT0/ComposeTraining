package com.my.book.library.featrue.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    commonMainViewModel: ViewModel,
    onMoveToMain: () -> Unit,
    modifier: Modifier
) {

    val commonMainViewModel = commonMainViewModel
    val splashViewModel = hiltViewModel<SplashViewModel>()

    SplashContent(
        onMoveToMain = onMoveToMain,
        modifier = Modifier
    )
}

@Composable
fun SplashContent(
    onMoveToMain: () -> Unit,
    modifier: Modifier
) {

    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Splash"
            )
        }
    }

    // TODO 임의 코드
    LaunchedEffect(Unit) {
        delay(2000L)
        onMoveToMain.invoke()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashUIPreview() {
    SplashContent(
        onMoveToMain = {},
        modifier = Modifier
    )
}