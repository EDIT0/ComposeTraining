package com.my.composexmldemo2.compose.component

import android.view.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import com.my.composexmldemo2.compose.ui.theme.white

/**
 * backgroundColor - StatusBar 색
 * statusBarPadding - true: 패딩o, false: 패딩x
 * navigationBarPadding - true: 패딩o, false: 패딩x
 * */
@Composable
fun CustomScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = white(),
    statusBarPadding: Boolean = true,
    navigationBarPadding: Boolean = true,
    window: Window,
    statusBarIconBlack: Boolean = true,
    navigationBarIconBlack: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {

    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = statusBarIconBlack // true: 어두운 아이콘, false: 밝은 아이콘
    wic.isAppearanceLightNavigationBars = navigationBarIconBlack // true: 어두운 아이콘, false: 밝은 아이콘

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = backgroundColor
            )
            .padding(
                top = if (statusBarPadding) { WindowInsets.statusBars.asPaddingValues().calculateTopPadding() } else { 0.dp },
                bottom = if (navigationBarPadding) { WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() } else { 0.dp },
            ),
        content = content
    )

}