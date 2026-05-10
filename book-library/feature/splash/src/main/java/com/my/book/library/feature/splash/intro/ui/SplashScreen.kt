package com.my.book.library.feature.splash.intro.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.Transparent
import com.my.book.library.feature.splash.intro.intent.SplashViewModelEvent
import com.my.book.library.feature.splash.intro.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    commonViewModel: CommonViewModel,
    onMoveToMain: () -> Unit,
    onMoveToSelectLibraryRegion: () -> Unit,
    modifier: Modifier
) {
    val localContext = LocalContext.current

    val commonViewModel = commonViewModel
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

    SystemBarController.Setup(
        config = SystemBarConfig(
            statusBarColor = Transparent,
            statusBarDarkIcons = true,
            useStatusBarSpace = true,
            navigationBarColor = Transparent,
            navigationBarDarkIcons = true,
            useNavigationBarSpace = true
        )
    ) { state ->
        Box(
            modifier = modifier
                .padding(top = state.statusBarHeight, bottom = state.navigationBarHeight)
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars)
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