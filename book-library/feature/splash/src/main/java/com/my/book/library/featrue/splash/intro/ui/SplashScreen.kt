package com.my.book.library.featrue.splash.intro.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.my.book.library.featrue.splash.intro.intent.SplashViewModelEvent
import com.my.book.library.featrue.splash.intro.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    commonMainViewModel: ViewModel,
    onMoveToMain: () -> Unit,
    onMoveToSelectLibraryRegion: () -> Unit,
    modifier: Modifier
) {
    val localContext = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val splashViewModel = hiltViewModel<SplashViewModel>()

    LaunchedEffect(key1 = true) {
        splashViewModel.sideEffectEvent.collect {
            when(it) {
                is SplashViewModel.SideEffectEvent.ShowToast -> {
                    Toast.makeText(localContext, it.message, Toast.LENGTH_SHORT).show()
                }
                is SplashViewModel.SideEffectEvent.OnMoveToMain -> {
                    onMoveToMain()
                }
                is SplashViewModel.SideEffectEvent.OnMoveToSelectLibrary -> {
                    onMoveToSelectLibraryRegion()
                }
            }
        }
    }

    SplashContent(
        onMoveToMain = onMoveToMain,
        modifier = Modifier,
        splashViewModelEvent = {
            when(it) {
                is SplashViewModelEvent.CheckMyLibraryInfo -> {
                    splashViewModel.intentAction(it)
                }
            }
        }
    )
}

@Composable
fun SplashContent(
    onMoveToMain: () -> Unit,
    modifier: Modifier,
    splashViewModelEvent: (SplashViewModelEvent) -> Unit
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
        delay(1000L)
        splashViewModelEvent.invoke(SplashViewModelEvent.CheckMyLibraryInfo())
    }
}

@Preview(showBackground = true)
@Composable
fun SplashUIPreview() {
    SplashContent(
        onMoveToMain = {},
        modifier = Modifier,
        splashViewModelEvent = {},
    )
}